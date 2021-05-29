package ksr.pl.kw.model.fuzzy;

import java.io.Serializable;

public interface FuzzySet extends Serializable {
    double belong(double value);
    double[] getAbcd();
    String getLabel();
    void setLabel(String Label);

    static FuzzySet createStandardFuzzySet(double[] abcd, String label){
        if(abcd.length == 3) return new TriangleFuzzySet(abcd, label);
        else return new TrapezeFuzzySet(abcd, label);
    }

}
