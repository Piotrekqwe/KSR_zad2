package ksr.pl.kw.logic.tank;

import ksr.pl.kw.logic.fuzzy.TraitId;

public class TraitValue {
    private TraitId id;
    private double value;

    public TraitValue(TraitId id, double value) {
        this.id = id;
        this.value = value;
    }

    public TraitId getId() {
        return id;
    }

    public double getValue() {
        return value;
    }
}
