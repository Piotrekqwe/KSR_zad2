package ksr.pl.kw.logic;

import ksr.pl.kw.logic.fuzzy.Quantifier;
import ksr.pl.kw.logic.fuzzy.Trait;

public class Calculator {
    private Trait[] traits;
    private Quantifier[] quantifiers;

    public Calculator(Trait[] traits, Quantifier[] quantifiers) {
        this.traits = traits;
        this.quantifiers = quantifiers;
    }
}
