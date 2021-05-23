package ksr.pl.kw.gui;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import ksr.pl.kw.logic.Calculator;
import ksr.pl.kw.logic.MyUtils;
import ksr.pl.kw.logic.fuzzy.*;
import ksr.pl.kw.logic.tank.Tank;
import ksr.pl.kw.logic.tank.TankRepository;

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

    public static final ExecutorService es = Executors.newFixedThreadPool(1);
    public static Calculator calculator;
    TankRepository tankRepository;
    FuzzySet selectedSet;
    //private static final String TANK_TYPE_QUALIFIER = "typ czołgu";
    //private static final String TIER_QUALIFIER = "tier";
    //private static final String NATION_QUALIFIER = "kraj";


    public ListView quantifierListView;
    public ListView summarizerListView;
    public ListView summarizerFuzzySetsListView;
    public ListView qualifierListView;
    public ListView qualifierFuzzySetsListView;
    public ToggleGroup quantifierTypeToggleGroup;
    public RadioButton relativeQuantifierToggle;
    public RadioButton absoluteQuantifierToggle;
    public TextArea summaryDisplayField;
    public Text summarizationTimerDisplay;
    public Text t0Dusplay;
    public Text t1Dusplay;
    public Text t2Dusplay;
    public Text t3Dusplay;
    public Text t4Dusplay;
    public Text t5Dusplay;
    public Text t6Dusplay;
    public Text t7Dusplay;
    public Text t8Dusplay;
    public Text t9Dusplay;
    public Text t10Dusplay;
    public Text t11Dusplay;
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

    public ListView traitsListView;
    public ListView fuzzySetsListView;
    public TextField nameField;
    public TextField aField;
    public TextField bField;
    public TextField cField;
    public TextField dField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tankRepository = new TankRepository();
        calculator = new Calculator();
        refreshTraitsList();
        refreshQuantifierList();
        refreshVariableLists();
    }

    private static final String ALL_TANKS_QUALIFIER = "brak kwalifikatora";

    private void refreshVariableLists() {
        summarizerListView.getItems().removeAll(summarizerListView.getItems());
        qualifierListView.getItems().removeAll(qualifierListView.getItems());
        fillVariableList(summarizerListView);
        qualifierListView.getItems().add(ALL_TANKS_QUALIFIER);
        fillVariableList(qualifierListView);
    }

    private void fillVariableList(ListView variableListView) {
        for (Trait trait : calculator.getTraits()) {
            variableListView.getItems().add(trait);
        }
    }

    private void refreshTraitsList() {
        traitsListView.getItems().removeAll(traitsListView.getItems());
        for (Trait trait : calculator.getTraits()) {
            traitsListView.getItems().add(trait);
        }
        traitsListView.getItems().add(calculator.getRelativeQuantifiers());
        traitsListView.getItems().add(calculator.getAbsoluteQuantifiers());
    }

    public void refreshFuzzySetsList() {
        TraitListItem selectedItem = ((TraitListItem) traitsListView.getSelectionModel().getSelectedItem());
        if (selectedItem != null) {
            fuzzySetsListView.getItems().removeAll(fuzzySetsListView.getItems());
            for (FuzzySet set : selectedItem.getSets()) {
                fuzzySetsListView.getItems().add(set);
            }
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
            quantifierListView.getItems().removeAll(quantifierListView.getItems());
            for (FuzzySet set : selectedQuantifier.getSets()) {
                quantifierListView.getItems().add(set);
            }
        }
    }

    public void refreshSummarizerSetsListView() {
        Trait selectedSumarizer = (Trait) summarizerListView.getSelectionModel().getSelectedItem();
        if (selectedSumarizer != null) {
            summarizerFuzzySetsListView.getItems().removeAll(summarizerFuzzySetsListView.getItems());
            for (FuzzySet set : selectedSumarizer.getSets()) {
                summarizerFuzzySetsListView.getItems().add(set);
            }
        }
    }

    public void refreshQualifierSetsListView() {
        Object selectedQualifier = qualifierListView.getSelectionModel().getSelectedItem();
        if (selectedQualifier != null) {
            if (selectedQualifier instanceof Trait) {
                qualifierFuzzySetsListView.getItems().removeAll(qualifierFuzzySetsListView.getItems());
                for (FuzzySet set : ((Trait)selectedQualifier).getSets()) {
                    qualifierFuzzySetsListView.getItems().add(set);
                }
            }
            else{
                qualifierFuzzySetsListView.getItems().removeAll(qualifierFuzzySetsListView.getItems());
            }
        }
    }

    public void addSet() {
        TraitListItem selectedTrait = ((TraitListItem) traitsListView.getSelectionModel().getSelectedItem());
        if (selectedTrait != null) {
            selectedTrait.getSets().add(new FuzzySet(new double[]{1, 2, 3, 4}, "Nowy set"));
        }
        refreshFuzzySetsList();
    }

    public void removeSet() {
        FuzzySet set = (FuzzySet) fuzzySetsListView.getSelectionModel().getSelectedItem();
        if (set != null) {
            TraitListItem selectedItem = ((TraitListItem) traitsListView.getSelectionModel().getSelectedItem());
            selectedItem.getSets().remove(set);
        }
    }

    public void modifySet() {
        FuzzySet set = (FuzzySet) fuzzySetsListView.getSelectionModel().getSelectedItem();
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
            selectedSet.setAbcd(abcd);
            refreshFuzzySetsList();
        }
    }

    public void loadDefaultValues() {
        ArrayList<Trait> traits = new ArrayList<>();
        for (TraitId id : TraitId.values()) {
            String[][] table = MyUtils.readFromFile("./src/main/resources/labels_default_values/", id.dbName + ".csv", ",");
            ArrayList<FuzzySet> fuzzySets = getFuzzySets(table);
            traits.add(new Trait(id, fuzzySets));
        }

        Quantifier relativeQuantifiers = null;
        Quantifier absoluteQuantifiers = null;
        for (int i = 0; i < 2; i++) {
            String[][] table = MyUtils.readFromFile("./src/main/resources/labels_default_values/", (i == 0) ? "absolute.csv" : "relative.csv", ",");
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
            fuzzySets.add(new FuzzySet(abcd, row[0]));
        }
        return fuzzySets;
    }

    private static final DecimalFormat df4 = new DecimalFormat("#.####");
    public void oneSubjectSummary() {
        es.submit(() -> {
            String summary = ((FuzzySet) quantifierListView.getSelectionModel().getSelectedItem()).getLabel() + " czołgów";
            FuzzySet selectedQualifierSet = (FuzzySet) qualifierFuzzySetsListView.getSelectionModel().getSelectedItem();
            if (selectedQualifierSet != null) {
                summary += " których " + qualifierListView.getSelectionModel().getSelectedItem() + " jest " + selectedQualifierSet + ',';
            }
            summary += " ma " + summarizerFuzzySetsListView.getSelectionModel().getSelectedItem() + ' ' + summarizerListView.getSelectionModel().getSelectedItem();
            summaryDisplayField.setText(summary);

            HashSet<Tank> tanks = tankRepository.getAllTanks();
            boolean quantifierIsRelative = (quantifierTypeToggleGroup.getSelectedToggle() == relativeQuantifierToggle);
            double[] quantifierSet = ((FuzzySet) quantifierListView.getSelectionModel().getSelectedItem()).getAbcd();
            TraitId summarizerId = ((Trait) summarizerListView.getSelectionModel().getSelectedItem()).getId();
            double[] summarizerSet = ((FuzzySet) summarizerFuzzySetsListView.getSelectionModel().getSelectedItem()).getAbcd();
            TraitId qualifierId = null;
            double[] qualifierSet = null;
            Object selectedQualifier = qualifierListView.getSelectionModel().getSelectedItem();
            if (selectedQualifier instanceof Trait) {
                qualifierId = ((Trait) selectedQualifier).getId();
                qualifierSet = ((FuzzySet) qualifierFuzzySetsListView.getSelectionModel().getSelectedItem()).getAbcd();
            }

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

            double[] result = calculator.oneSubjectSummary(tanks, quantifierIsRelative, quantifierSet, summarizerId, summarizerSet, qualifierId, qualifierSet, weights);

            t0Dusplay.setText("T1 = " + df4.format(result[0]));
            t1Dusplay.setText("T1 = " + df4.format(result[1]));
            t2Dusplay.setText("T2 = " + df4.format(result[2]));
            t3Dusplay.setText("T3 = " + df4.format(result[3]));
            t4Dusplay.setText("T4 = " + df4.format(result[4]));
            t5Dusplay.setText("T5 = " + df4.format(result[5]));
            t6Dusplay.setText("T6 = " + df4.format(result[6]));
            t7Dusplay.setText("T7 = " + df4.format(result[7]));
            t8Dusplay.setText("T8 = " + df4.format(result[8]));
            t9Dusplay.setText("T9 = " + df4.format(result[9]));
            t10Dusplay.setText("T10 = " + df4.format(result[10]));
            t11Dusplay.setText("T11 = " + df4.format(result[11]));
        });
    }

    public void multipleOneSubjectSummaries() {
        es.submit(() -> {
            summarizationTimerDisplay.setText("trwa podsumowywanie");
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

            HashSet<Tank> tanks = tankRepository.getAllTanks();
            TraitId summarizerId = ((Trait) summarizerListView.getSelectionModel().getSelectedItem()).getId();
            Object selectedQualifier = qualifierListView.getSelectionModel().getSelectedItem();
            Quantifier selectedQuantifier;
            boolean quantifierIsRelative = false;
            TraitId summarizerTraitId = ((Trait) summarizerListView.getSelectionModel().getSelectedItem()).getId();
            //TraitId qualifierTraitId = ((Trait) qualifierListView.getSelectionModel().getSelectedItem()).getId();

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
                selectedQuantifier.getSets().forEach(quantifierSet -> {
                    summarizerFuzzySetsListView.getItems().forEach(summarizerConsumer -> {
                        FuzzySet summarizerSet = (FuzzySet) summarizerConsumer;
                        if (selectedQualifier instanceof Trait) {
                            TraitId qualifierId = ((Trait) selectedQualifier).getId();
                            qualifierFuzzySetsListView.getItems().forEach(qualifierConsumer -> {
                                FuzzySet qualifierSet = (FuzzySet) qualifierConsumer;
                                try {
                                    bw.write(multipleOneSubjectSummariesIteration(tanks, finalQuantifierIsRelative, quantifierSet,
                                            summarizerId, summarizerSet, qualifierId, qualifierSet, weights));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                        } else {
                            try {
                                bw.write(multipleOneSubjectSummariesIteration(tanks, finalQuantifierIsRelative, quantifierSet,
                                        summarizerId, summarizerSet, null, null, weights));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            summarizationTimerDisplay.setText("zakończone");
        });

    }
    private String multipleOneSubjectSummariesIteration(HashSet<Tank> tanks, boolean quantifierIsRelative, FuzzySet quantifierSet,
                                                        TraitId summarizerId, FuzzySet summarizerSet,
                                                        TraitId qualifierId, FuzzySet qualifierSet, double[] weights){
        String summary = quantifierSet + " czołgów";
        double[] qualifierAbcd = null;
        if (qualifierSet != null) {
            summary += " których " + qualifierId + " jest " + qualifierSet + ',';
            qualifierAbcd = qualifierSet.getAbcd();
        }
        summary += " ma " + summarizerSet + ' ' + summarizerId;
        StringBuilder line = new StringBuilder(summary);

        double[] result = calculator.oneSubjectSummary(tanks, quantifierIsRelative, quantifierSet.getAbcd(),
                summarizerId, summarizerSet.getAbcd(), qualifierId, qualifierAbcd, weights);

        for(double tx : result){
            line.append('$').append(df4.format(tx));
        }
        line.append('\n');
        return line.toString();
    }

}
