package ksr.pl.kw.model.traits;

import ksr.pl.kw.model.fuzzy.FuzzySet;
import ksr.pl.kw.model.TraitListItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class Trait implements Serializable, TraitListItem {
    private final TraitId id;
    private final List<FuzzySet> fuzzySets;

    public Trait(TraitId id) {
        this.id = id;
        this.fuzzySets = Collections.emptyList();
    }

    public Trait(TraitId id, List<FuzzySet> sets) {
        this.id = id;
        this.fuzzySets = sets;
    }

    public TraitId getId() {
        return id;
    }

    @Override
    public List<FuzzySet> getSets() {
        return fuzzySets;
    }

    @Override
    public String toString() {
        return id.uiName;
    }

}
