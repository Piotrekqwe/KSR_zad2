package ksr.pl.kw.model.fuzzy;

public class TriangleFuzzySet implements FuzzySet {
    private double[] abcd;
    private String label;

    public TriangleFuzzySet(double[] abcd, String label) {
        this.abcd = abcd;
        this.label = label;
    }

    @Override
    public double belong(double value) {
        if(value < abcd[0] || value > abcd[abcd.length - 1]) return 0;
        if(value < abcd[1]) return (value - abcd[0]) / (abcd[1] - abcd[0]);
        if(value > abcd[abcd.length - 2]) return (abcd[abcd.length - 1] - value) / (abcd[abcd.length - 1] - abcd[abcd.length - 2]);
        return 1;
    }

    @Override
    public double[] getAbcd() {
        return abcd;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
