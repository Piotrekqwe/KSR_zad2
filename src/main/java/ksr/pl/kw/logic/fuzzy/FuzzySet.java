package ksr.pl.kw.logic.fuzzy;

import java.io.Serializable;

public class FuzzySet implements Serializable {
    private int[] abcd;
    private String label;

    public FuzzySet(int[] abcd, String label) {
        this.abcd = abcd;
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }

    public int[] getAbcd() {
        return abcd;
    }

    public String getLabel() {
        return label;
    }

    public void setAbcd(int[] abcd) {
        this.abcd = abcd;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
