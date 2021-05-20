package ksr.pl.kw.logic.fuzzy;

import java.io.Serializable;
import java.util.ArrayList;

public class Quantifier implements Serializable, TraitListItem {
    private String title;
    private ArrayList<FuzzySet> fuzzySets;
    public static String RELATIVE_QUANTIFIERS_NAME = "Kwantyfikatory Względne";
    public static String ABSOLUTE_QUANTIFIERS_NAME = "Kwantyfikatory Absolutne";

    public Quantifier(String title, ArrayList<FuzzySet> sets) {
        this.fuzzySets = sets;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public ArrayList<FuzzySet> getSets() {
        return fuzzySets;
    }

    @Override
    public String toString() {
        return title;
    }


}
