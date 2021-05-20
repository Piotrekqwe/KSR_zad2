package ksr.pl.kw.logic.fuzzy;

import java.io.Serializable;

public class FuzzySet implements Serializable {
    private double[] abcd;
    private String label;

    public FuzzySet(double[] abcd, String label) {
        this.abcd = abcd;
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }

    public double[] getAbcd() {
        return abcd;
    }

    public String getLabel() {
        return label;
    }

    public void setAbcd(double[] abcd) {
        this.abcd = abcd;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
