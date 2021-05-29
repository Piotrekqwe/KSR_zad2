package ksr.pl.kw.model.traits;

import ksr.pl.kw.model.fuzzy.FuzzySet;

import java.util.ArrayList;

public class EmptyTrait extends Trait {
    private static final EmptyTrait INSTANCE = new EmptyTrait();

    public static EmptyTrait getInstance() {
        return INSTANCE;
    }

    public EmptyTrait() {
        super(null, null);
    }

    public EmptyTrait(TraitId id) {
        super(id);
        throw new IllegalStateException();
    }

    public EmptyTrait(TraitId id, ArrayList<FuzzySet> sets) {
        super(id, sets);
        throw new IllegalStateException();
    }

    @Override
    public String toString() {
        return "brak kwalifikatora";
    }
}
