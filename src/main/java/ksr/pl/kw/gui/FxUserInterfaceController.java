package ksr.pl.kw.gui;

import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import ksr.pl.kw.service.MultiSubjectSummaryGenerator;
import ksr.pl.kw.service.calculator.Calculator;
import ksr.pl.kw.model.fuzzy.*;
import ksr.pl.kw.model.*;
import ksr.pl.kw.model.tanks.Tank;
import ksr.pl.kw.model.traits.EmptyTrait;
import ksr.pl.kw.model.traits.Trait;
import ksr.pl.kw.model.traits.TraitId;
import ksr.pl.kw.repository.TankRepository;
import ksr.pl.kw.service.OneSubjectSummaryGenerator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.apache.commons.lang3.StringUtils.isNumeric;


public class FxUserInterfaceController implements Initializable {

    private static final String QUANTIFIER_MESSAGE = "%s czołgów";
    private static final String QUALIFIER_MESSAGE = " których %s jest %s,";
    private static final String SUMMARIZER_MESSAGE = " ma %s %s";

    public static final ExecutorService es = Executors.newFixedThreadPool(1);
    public static Calculator calculator;
    private TankRepository tankRepository;
    private FuzzySet selectedSet;
    //private static final String TANK_TYPE_QUALIFIER = "typ czołgu";
    //private static final String TIER_QUALIFIER = "tier";
    //private static final String NATION_QUALIFIER = "kraj";


                //Podsumowania
    public ListView<FuzzySet> quantifierListView;
    public ListView<Trait> summarizerListView;
    public ListView<FuzzySet> summarizerFuzzySetsListView;
    public ListView<Trait> qualifierListView;
    public ListView<FuzzySet> qualifierFuzzySetsListView;
    public ToggleGroup quantifierTypeToggleGroup;
    public RadioButton relativeQuantifierToggle;
    public RadioButton absoluteQuantifierToggle;
    public TextArea summaryDisplayField;
    public Text summarizationTimerDisplay;
    public Text t0Display;
    public Text t1Display;
    public Text t2Display;
    public Text t3Display;
    public Text t4Display;
    public Text t5Display;
    public Text t6Display;
    public Text t7Display;
    public Text t8Display;
    public Text t9Display;
    public Text t10Display;
    public Text t11Display;
    public TextField t1WeightField;
    public TextField t2WeightField;
    public TextField t3WeightField;
    public TextField t4WeightField;
    public TextField t5WeightField;
    public TextField t6WeightField;
    public TextField t7WeightField;
    public TextField t8WeightField;
    public TextField t9WeightField;
    public TextField t10WeightField;
    public TextField t11WeightField;

                //Wielopodmiotowe
    public Text multiSubjectSummaryResultDisplay;
    public ToggleGroup alliesOrAxisToggleGroup;
    public RadioButton alliesToggle;
    public RadioButton axisToggle;

                //Panel Użytkownika Zaawansowanego
    public ListView<TraitListItem> traitsListView;
    public ListView<FuzzySet> fuzzySetsListView;
    public TextField nameField;
    public TextField aField;
    public TextField bField;
    public TextField cField;
    public TextField dField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tankRepository = new TankRepository();
        calculator = Calculator.getInstance();
        refreshTraitsList();
        refreshQuantifierList();
        refreshVariableLists();
    }

    private void refreshVariableLists() {
        ObservableList<Trait> summarizerListViewItems = summarizerListView.getItems();
        ObservableList<Trait> qualifierListViewItems = qualifierListView.getItems();
        summarizerListViewItems.clear();
        qualifierListViewItems.clear();
        summarizerListViewItems.addAll(calculator.getTraits());
        qualifierListViewItems.add(EmptyTrait.getInstance());
        qualifierListViewItems.addAll(calculator.getTraits());
    }

    private void refreshTraitsList() {
        ObservableList<TraitListItem> items = traitsListView.getItems();
        items.clear();
        items.addAll(calculator.getTraits());
        items.add(calculator.getRelativeQuantifiers());
        items.add(calculator.getAbsoluteQuantifiers());
    }

    public void refreshFuzzySetsList() {
        TraitListItem selectedItem = traitsListView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            ObservableList<FuzzySet> items = fuzzySetsListView.getItems();
            items.clear();
            items.addAll(selectedItem.getSets());
        }
    }

    public void refreshQuantifierList() {
        Quantifier selectedQuantifier;
        if (quantifierTypeToggleGroup.getSelectedToggle() == relativeQuantifierToggle) {
            selectedQuantifier = calculator.getRelativeQuantifiers();
        } else {
            selectedQuantifier = calculator.getAbsoluteQuantifiers();
        }
        if (selectedQuantifier != null) {
            ObservableList<FuzzySet> items = quantifierListView.getItems();
            items.clear();
            items.addAll(selectedQuantifier.getSets());
        }
    }

    public void refreshSummarizerSetsListView() {
        Trait selectedSumarizer = summarizerListView.getSelectionModel().getSelectedItem();
        if (selectedSumarizer != null) {
            summarizerFuzzySetsListView.getItems().removeAll(summarizerFuzzySetsListView.getItems());
            for (FuzzySet set : selectedSumarizer.getSets()) {
                summarizerFuzzySetsListView.getItems().add(set);
            }
        }
    }

    public void refreshQualifierSetsListView() {
        Trait selectedQualifier = qualifierListView.getSelectionModel().getSelectedItem();
        if (selectedQualifier != null) {
            ObservableList<FuzzySet> items = qualifierFuzzySetsListView.getItems();
            items.clear();
            if (!(selectedQualifier instanceof EmptyTrait)) {
                items.addAll(selectedQualifier.getSets());
            }
        }
    }

    public void addSet() {
        TraitListItem selectedTrait = traitsListView.getSelectionModel().getSelectedItem();
        if (selectedTrait != null) {
            selectedTrait.getSets().add(FuzzySet.createStandardFuzzySet(new double[]{1, 2, 3, 4}, "Nowy set"));
        }
        refreshFuzzySetsList();
    }

    public void removeSet() {
        FuzzySet set = fuzzySetsListView.getSelectionModel().getSelectedItem();
        if (set != null) {
            traitsListView.getSelectionModel().getSelectedItem().getSets().remove(set);
        }
    }

    public void modifySet() {
        FuzzySet set = fuzzySetsListView.getSelectionModel().getSelectedItem();
        if (set != null) {
            selectedSet = set;
            displaySelectedSet();
        }
    }

    private void displaySelectedSet() {
        if (selectedSet != null) {
            nameField.setText(selectedSet.getLabel());
            aField.setText(String.valueOf(selectedSet.getAbcd()[0]));
            bField.setText(String.valueOf(selectedSet.getAbcd()[1]));
            cField.setText(String.valueOf(selectedSet.getAbcd()[2]));
            if (selectedSet.getAbcd().length > 3) {
                dField.setText(String.valueOf(selectedSet.getAbcd()[3]));
            } else {
                dField.setText("");
            }
        }
    }

    public void saveChangesToSet() {
        if (selectedSet != null) {
            selectedSet.setLabel(nameField.getText());
            double[] abcd;
            if (isNumeric(dField.getText())) {
                abcd = new double[4];
                abcd[3] = Double.parseDouble(dField.getText());
            } else {
                abcd = new double[3];
            }
            abcd[0] = Double.parseDouble(aField.getText());
            abcd[1] = Double.parseDouble(bField.getText());
            abcd[2] = Double.parseDouble(cField.getText());
            selectedSet = FuzzySet.createStandardFuzzySet(abcd, selectedSet.getLabel());
            refreshFuzzySetsList();
        }
    }

    public void loadDefaultValues() {
        ArrayList<Trait> traits = new ArrayList<>();
        for (TraitId id : TraitId.values()) {
            String[][] table = UiUtils.readFromFile("./src/main/resources/labels_default_values/", id.dbName + ".csv", ",");
            ArrayList<FuzzySet> fuzzySets = getFuzzySets(table);
            traits.add(new Trait(id, fuzzySets));
        }

        Quantifier relativeQuantifiers = null;
        Quantifier absoluteQuantifiers = null;
        for (int i = 0; i < 2; i++) {
            String[][] table = UiUtils.readFromFile("./src/main/resources/labels_default_values/", (i == 0) ? "absolute.csv" : "relative.csv", ",");
            ArrayList<FuzzySet> fuzzySets = getFuzzySets(table);
            if (i == 0) {
                absoluteQuantifiers = new Quantifier(Quantifier.ABSOLUTE_QUANTIFIERS_NAME, fuzzySets);
            } else {
                relativeQuantifiers = new Quantifier(Quantifier.RELATIVE_QUANTIFIERS_NAME, fuzzySets);
            }
        }

        calculator.setTraits(traits);
        calculator.setRelativeQuantifiers(relativeQuantifiers);
        calculator.setAbsoluteQuantifiers(absoluteQuantifiers);
        refreshTraitsList();
        refreshQuantifierList();
        refreshVariableLists();
    }

    private ArrayList<FuzzySet> getFuzzySets(String[][] table) {
        ArrayList<FuzzySet> fuzzySets = new ArrayList<>();
        for (String[] row : table) {
            double[] abcd;
            if (row.length == 5) {
                abcd = new double[]{Double.parseDouble(row[1]), Double.parseDouble(row[2]), Double.parseDouble(row[3]), Double.parseDouble(row[4])};
            } else {
                abcd = new double[]{Double.parseDouble(row[1]), Double.parseDouble(row[2]), Double.parseDouble(row[3])};
            }
            fuzzySets.add(FuzzySet.createStandardFuzzySet(abcd, row[0]));
        }
        return fuzzySets;
    }

    private static final DecimalFormat df4 = new DecimalFormat("#.####");

    public void oneSubjectSummary() {
        es.submit(() -> {
            FuzzySet selectedQuantifier = quantifierListView.getSelectionModel().getSelectedItem();
            String label = selectedQuantifier.getLabel();
            String summary = String.format(QUANTIFIER_MESSAGE, label);
            Trait selectedQualifier = qualifierListView.getSelectionModel().getSelectedItem();
            FuzzySet selectedQualifierSet = qualifierFuzzySetsListView.getSelectionModel().getSelectedItem();
            if (selectedQualifierSet != null) {
                summary += String.format(QUALIFIER_MESSAGE, selectedQualifier, selectedQualifierSet);
            }
            Trait selectedSummarizer = summarizerListView.getSelectionModel().getSelectedItem();
            FuzzySet selectedSummarizerSet = summarizerFuzzySetsListView.getSelectionModel().getSelectedItem();
            summary += String.format(SUMMARIZER_MESSAGE, selectedSummarizerSet, selectedSummarizer);
            summaryDisplayField.setText(summary);
            double[] result = generateOneSubjectSummary(selectedQuantifier, selectedQualifier, selectedQualifierSet, selectedSummarizer, selectedSummarizerSet);

            t0Display.setText("T = " + df4.format(result[0]));
            t1Display.setText("T1 = " + df4.format(result[1]));
            t2Display.setText("T2 = " + df4.format(result[2]));
            t3Display.setText("T3 = " + df4.format(result[3]));
            t4Display.setText("T4 = " + df4.format(result[4]));
            t5Display.setText("T5 = " + df4.format(result[5]));
            t6Display.setText("T6 = " + df4.format(result[6]));
            t7Display.setText("T7 = " + df4.format(result[7]));
            t8Display.setText("T8 = " + df4.format(result[8]));
            t9Display.setText("T9 = " + df4.format(result[9]));
            t10Display.setText("T10 = " + df4.format(result[10]));
            t11Display.setText("T11 = " + df4.format(result[11]));
        });
    }

    private double[] generateOneSubjectSummary(FuzzySet selectedQuantifier, Trait selectedQualifier, FuzzySet selectedQualifierSet,
                                               Trait selectedSummarizer, FuzzySet selectedSummarizerSet) {
        HashSet<Tank> tanks = tankRepository.getAllTanks();
        boolean quantifierIsRelative = (quantifierTypeToggleGroup.getSelectedToggle() == relativeQuantifierToggle);
        TraitId summarizerId = selectedSummarizer.getId();
        double[] weights = initializeWeights();
        TraitId qualifierId = null;
        if (!(selectedQualifier instanceof EmptyTrait) && !(selectedQualifier == null)) {
            qualifierId = selectedQualifier.getId();
        }
        return OneSubjectSummaryGenerator.getInstance().calculate(tanks, quantifierIsRelative, selectedQuantifier, summarizerId,
                selectedSummarizerSet, qualifierId, selectedQualifierSet, weights);
    }

    private double[] initializeWeights() {
        double[] weights = new double[11];
        weights[0] = Double.parseDouble(t1WeightField.getText());
        weights[1] = Double.parseDouble(t2WeightField.getText());
        weights[2] = Double.parseDouble(t3WeightField.getText());
        weights[3] = Double.parseDouble(t4WeightField.getText());
        weights[4] = Double.parseDouble(t5WeightField.getText());
        weights[5] = Double.parseDouble(t6WeightField.getText());
        weights[6] = Double.parseDouble(t7WeightField.getText());
        weights[7] = Double.parseDouble(t8WeightField.getText());
        weights[8] = Double.parseDouble(t9WeightField.getText());
        weights[9] = Double.parseDouble(t10WeightField.getText());
        weights[10] = Double.parseDouble(t11WeightField.getText());
        return weights;
    }

    public void multiSubjectSummary1() {
        multiSubjectSummary(multiSubjectSummaryType.TYPE1);
    }
    public void multiSubjectSummary2() {
        multiSubjectSummary(multiSubjectSummaryType.TYPE2);
    }
    public void multiSubjectSummary3() {
        multiSubjectSummary(multiSubjectSummaryType.TYPE3);
    }
    public void multiSubjectSummary4() {
        multiSubjectSummary(multiSubjectSummaryType.TYPE4);
    }

    private void multiSubjectSummary(multiSubjectSummaryType type) {
        if (quantifierTypeToggleGroup.getSelectedToggle() == relativeQuantifierToggle) {
            es.submit(() -> {
                FuzzySet selectedQuantifier = quantifierListView.getSelectionModel().getSelectedItem();
                String label;
                if (type == multiSubjectSummaryType.TYPE4) {
                    label = "więcej";
                }
                else{
                    label = selectedQuantifier.getLabel();
                }
                String summary = String.format(QUANTIFIER_MESSAGE, label);
                Trait selectedQualifier = qualifierListView.getSelectionModel().getSelectedItem();
                FuzzySet selectedQualifierSet = qualifierFuzzySetsListView.getSelectionModel().getSelectedItem();
                Trait selectedSummarizer = summarizerListView.getSelectionModel().getSelectedItem();
                FuzzySet selectedSummarizerSet = summarizerFuzzySetsListView.getSelectionModel().getSelectedItem();

                if (alliesOrAxisToggleGroup.getSelectedToggle() == alliesToggle) {
                    summary += " należących do aliantów";
                }
                else {
                    summary += " należących do państw osi";
                }
                if (type == multiSubjectSummaryType.TYPE3) {
                    summary += String.format(QUALIFIER_MESSAGE, selectedQualifier, selectedQualifierSet);
                }
                if (alliesOrAxisToggleGroup.getSelectedToggle() == alliesToggle) {
                    summary += " w porównaniu do czołgów państw osi";
                }
                else {
                    summary += " w porównaniu do czołgów aliantów";
                }
                if (type == multiSubjectSummaryType.TYPE2) {
                    summary += String.format(QUALIFIER_MESSAGE, selectedQualifier, selectedQualifierSet);
                }
                summary += String.format(SUMMARIZER_MESSAGE, selectedSummarizerSet, selectedSummarizer);
                summaryDisplayField.setText(summary);
                double result = generateMultiSummary(type, selectedQuantifier, selectedQualifier, selectedQualifierSet, selectedSummarizer, selectedSummarizerSet);

                multiSubjectSummaryResultDisplay.setText("T = " + df4.format(result));
            });
        }
    }

    private double generateMultiSummary(multiSubjectSummaryType type, FuzzySet selectedQuantifier, Trait selectedQualifier, FuzzySet selectedQualifierSet,
                                        Trait selectedSummarizer, FuzzySet selectedSummarizerSet) {
        HashSet<Tank> allies = tankRepository.getTanksByNation("china");
        allies.addAll(tankRepository.getTanksByNation("france"));
        allies.addAll(tankRepository.getTanksByNation("poland"));
        allies.addAll(tankRepository.getTanksByNation("uk"));
        allies.addAll(tankRepository.getTanksByNation("usa"));
        allies.addAll(tankRepository.getTanksByNation("ussr"));

        HashSet<Tank> axis = tankRepository.getTanksByNation("czech");
        axis.addAll(tankRepository.getTanksByNation("germany"));
        axis.addAll(tankRepository.getTanksByNation("italy"));
        axis.addAll(tankRepository.getTanksByNation("japan"));
        axis.addAll(tankRepository.getTanksByNation("sweden"));


        if (alliesOrAxisToggleGroup.getSelectedToggle() == alliesToggle) {
            return MultiSubjectSummaryGenerator.getInstance().generateMultiSummary(type, selectedQuantifier, selectedQualifier.getId(),
                    selectedQualifierSet, selectedSummarizer.getId(), selectedSummarizerSet, allies, axis);
        }
        return MultiSubjectSummaryGenerator.getInstance().generateMultiSummary(type, selectedQuantifier, selectedQualifier.getId(),
                selectedQualifierSet, selectedSummarizer.getId(), selectedSummarizerSet, axis, allies);

    }



    //TODO: delete or refactor
    public void multipleOneSubjectSummaries() {
        es.submit(() -> {
            summarizationTimerDisplay.setText("trwa podsumowywanie");
            double[] weights = initializeWeights();

            HashSet<Tank> tanks = tankRepository.getAllTanks();
            TraitId summarizerId = summarizerListView.getSelectionModel().getSelectedItem().getId();
            Trait selectedQualifier = qualifierListView.getSelectionModel().getSelectedItem();
            Quantifier selectedQuantifier;
            boolean quantifierIsRelative = false;

            try (FileWriter writer = new FileWriter("summaries.txt");
                 BufferedWriter bw = new BufferedWriter(writer)) {
                bw.write("summary$T$T1$T2$T3$T4$T5$T6$T7$T8$T9$T10$T11$\n");
                if (quantifierTypeToggleGroup.getSelectedToggle() == relativeQuantifierToggle) {
                    selectedQuantifier = calculator.getRelativeQuantifiers();
                    quantifierIsRelative = true;
                } else {
                    selectedQuantifier = calculator.getAbsoluteQuantifiers();
                }
                boolean finalQuantifierIsRelative = quantifierIsRelative;
                selectedQuantifier.getSets().forEach(quantifierSet -> summarizerFuzzySetsListView.getItems().forEach(summarizerConsumer -> {
                    if (!(selectedQualifier instanceof EmptyTrait)) {
                        TraitId qualifierId = selectedQualifier.getId();
                        qualifierFuzzySetsListView.getItems().forEach(qualifierConsumer -> {
                            try {
                                bw.write(multipleOneSubjectSummariesIteration(tanks, finalQuantifierIsRelative, quantifierSet,
                                        summarizerId, summarizerConsumer, qualifierId, qualifierConsumer, weights));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    } else {
                        try {
                            bw.write(multipleOneSubjectSummariesIteration(tanks, finalQuantifierIsRelative, quantifierSet,
                                    summarizerId, summarizerConsumer, null, null, weights));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }));
            } catch (IOException e) {
                e.printStackTrace();
            }
            summarizationTimerDisplay.setText("zakończone");
        });

    }
    //TODO: delete or refactor
    private String multipleOneSubjectSummariesIteration(HashSet<Tank> tanks, boolean quantifierIsRelative, FuzzySet quantifierSet,
                                                        TraitId summarizerId, FuzzySet summarizerSet,
                                                        TraitId qualifierId, FuzzySet qualifierSet, double[] weights) {
        String summary = String.format(QUANTIFIER_MESSAGE, quantifierSet);
        FuzzySet qualifierAbcd = null;
        if (qualifierSet != null) {
            summary += String.format(QUALIFIER_MESSAGE, qualifierId, qualifierSet);
            qualifierAbcd = qualifierSet;
        }
        summary += String.format(SUMMARIZER_MESSAGE, summarizerSet, summarizerId);
        StringBuilder line = new StringBuilder(summary);

        double[] result = OneSubjectSummaryGenerator.getInstance().calculate(tanks, quantifierIsRelative, quantifierSet,
                summarizerId, summarizerSet, qualifierId, qualifierAbcd, weights);

        for (double tx : result) {
            line.append('$').append(df4.format(tx));
        }
        line.append('\n');
        return line.toString();
    }

}
