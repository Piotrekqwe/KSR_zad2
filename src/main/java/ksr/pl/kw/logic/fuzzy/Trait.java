package ksr.pl.kw.logic.fuzzy;

public class Trait {
    private final TraitId id;
    private final FuzzySet set;

    public Trait(TraitId id, FuzzySet set) {
        this.id = id;
        this.set = set;
    }

    public TraitId getId() {
        return id;
    }

    public FuzzySet getSet() {
        return set;
    }
}
