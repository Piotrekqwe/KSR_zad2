package ksr.pl.kw.model;

import ksr.pl.kw.model.fuzzy.FuzzySet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Quantifier implements Serializable, TraitListItem {
    public static final String ABSOLUTE_QUANTIFIERS_NAME = "Kwantyfikatory Absolutne";
    public static final String RELATIVE_QUANTIFIERS_NAME = "Kwantyfikatory Względne";
    private final String title;
    private final List<FuzzySet> fuzzySets;

    public Quantifier(String title) {
        this.title = title;
        this.fuzzySets = Collections.emptyList();
    }

    public Quantifier(String title, ArrayList<FuzzySet> sets) {
        this.fuzzySets = sets;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public List<FuzzySet> getSets() {
        return fuzzySets;
    }

    @Override
    public String toString() {
        return title;
    }


}
