package ksr.pl.kw.gui;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import ksr.pl.kw.logic.Calculator;
import ksr.pl.kw.logic.fuzzy.FuzzySet;
import ksr.pl.kw.logic.fuzzy.Trait;
import ksr.pl.kw.logic.fuzzy.TraitListItem;
import ksr.pl.kw.logic.tank.TankRepository;

import java.net.URL;
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

    private void refreshTraitsList(){
        traitsListView.getItems().removeAll(traitsListView.getItems());
        for (Trait trait : calculator.getTraits()) {
            traitsListView.getItems().add(trait);
        }
        traitsListView.getItems().add(calculator.getRelativeQuantifiers());
        traitsListView.getItems().add(calculator.getAbsoluteQuantifiers());
    }

    public void refreshFuzzySetsList() {
        TraitListItem selectedItem = ((TraitListItem) traitsListView.getSelectionModel().getSelectedItem());
        if(selectedItem != null){
            fuzzySetsListView.getItems().removeAll(fuzzySetsListView.getItems());
            for (FuzzySet set : selectedItem.getSets()) {
                fuzzySetsListView.getItems().add(set);
            }
        }
    }

    public void addSet() {
        TraitListItem selectedTrait = ((TraitListItem) traitsListView.getSelectionModel().getSelectedItem());
        if(selectedTrait != null){
            selectedTrait.getSets().add(new FuzzySet(new int[]{1, 2, 3, 4}, "Nowy set"));
        }
        refreshFuzzySetsList();
    }

    public void removeSet() {
        FuzzySet set = (FuzzySet) fuzzySetsListView.getSelectionModel().getSelectedItem();
        if(set != null){
            TraitListItem selectedItem = ((TraitListItem) traitsListView.getSelectionModel().getSelectedItem());
            selectedItem.getSets().remove(set);
        }
    }

    public void modifySet() {
        FuzzySet set = (FuzzySet) fuzzySetsListView.getSelectionModel().getSelectedItem();
        if(set != null){
            selectedSet = set;
            displaySelectedSet();
        }
    }

    private void displaySelectedSet(){
        if(selectedSet != null) {
            nameField.setText(selectedSet.getLabel());
            aField.setText(String.valueOf(selectedSet.getAbcd()[0]));
            bField.setText(String.valueOf(selectedSet.getAbcd()[1]));
            cField.setText(String.valueOf(selectedSet.getAbcd()[2]));
            if (selectedSet.getAbcd().length > 3) {
                dField.setText(String.valueOf(selectedSet.getAbcd()[3]));
            }
        }
    }

    public void saveChangesToSet() {
        if(selectedSet != null) {
            selectedSet.setLabel(nameField.getText());
            int[] abcd;
            if (isNumeric(dField.getText())) {
                abcd = new int[4];
                abcd[3] = Integer.parseInt(dField.getText());
            }
            else{
                abcd = new int[3];
            }
            abcd[0] = Integer.parseInt(aField.getText());
            abcd[1] = Integer.parseInt(bField.getText());
            abcd[2] = Integer.parseInt(cField.getText());
            selectedSet.setAbcd(abcd);
            refreshFuzzySetsList();
        }
    }

    public void loadDefaultValues() {
        //TODO: wczytać domyślne wartości
        //calculator.setTraits();
        //calculator.setRelativeQuantifiers();
        //calculator.setAbsoluteQuantifiers();
    }
}
