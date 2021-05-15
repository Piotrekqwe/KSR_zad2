package ksr.pl.kw.logic.fuzzy;

public class Quantifier {
    private FuzzySet set;
    private String label;

    public Quantifier(FuzzySet set, String label) {
        this.set = set;
        this.label = label;
    }

    public FuzzySet getSet() {
        return set;
    }

    public String getLabel() {
        return label;
    }
}
