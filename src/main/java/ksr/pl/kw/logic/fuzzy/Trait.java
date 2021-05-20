package ksr.pl.kw.logic.fuzzy;

import java.io.Serializable;
import java.util.ArrayList;

public class Trait implements Serializable, TraitListItem {
    private final TraitId id;
    private final ArrayList<FuzzySet> fuzzySets;

    public Trait(TraitId id, ArrayList<FuzzySet> sets) {
        this.id = id;
        this.fuzzySets = sets;
    }

    public TraitId getId() {
        return id;
    }

    @Override
    public ArrayList<FuzzySet> getSets() {
        return fuzzySets;
    }

    @Override
    public String toString() {
        return id.uiName;
    }

}
