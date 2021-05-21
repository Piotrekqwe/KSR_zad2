package ksr.pl.kw.gui;

import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import ksr.pl.kw.logic.Calculator;
import ksr.pl.kw.logic.fuzzy.*;
import ksr.pl.kw.logic.tank.TankRepository;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static org.apache.commons.lang3.StringUtils.isNumeric;


public class FxUserInterfaceController implements Initializable {

    public static Calculator calculator;
    TankRepository tankRepository;
    FuzzySet selectedSet;

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
            }
            else{
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
            String[][] table = Calculator.readFromFile("./src/main/resources/labels_default_values/", id.dbName + ".csv", ",");
            ArrayList<FuzzySet> fuzzySets = getFuzzySets(table);
            traits.add(new Trait(id, fuzzySets));
        }

        Quantifier relativeQuantifiers = null;
        Quantifier absoluteQuantifiers = null;

        for (int i = 0; i < 2; i++) {
            String[][] table = Calculator.readFromFile("./src/main/resources/labels_default_values/", (i == 0) ? "absolute.csv" : "relative.csv", ",");
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
}
