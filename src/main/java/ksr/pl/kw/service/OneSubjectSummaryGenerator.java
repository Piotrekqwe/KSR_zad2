package ksr.pl.kw.service;

import ksr.pl.kw.model.fuzzy.FuzzySet;
import ksr.pl.kw.model.tanks.Tank;
import ksr.pl.kw.model.traits.TraitId;
import ksr.pl.kw.service.calculator.Calculator;

import java.util.HashSet;

public class OneSubjectSummaryGenerator {
    private static final Calculator calculator = Calculator.getInstance();
    private static final OneSubjectSummaryGenerator INSTANCE = new OneSubjectSummaryGenerator();

    public static OneSubjectSummaryGenerator getInstance() {
        return INSTANCE;
    }

    public OneSubjectSummaryGenerator() {
    }

    public double[] calculate(HashSet<Tank> tanks, boolean quantifierIsRelative, FuzzySet quantifierSet,
                              TraitId summarizerId, FuzzySet summarizerSet,
                              TraitId qualifierId, FuzzySet qualifierSet, double[] weights) {
        double[] result = new double[12];

        double[] t1Values;
        if (qualifierId == null) {
            t1Values = calculator.t1form1(tanks, quantifierIsRelative, summarizerId, summarizerSet);
            result[9] = 0;
            result[10] = 0;
            result[11] = 0;
        } else {
            t1Values = calculator.t1form2(tanks, summarizerId, summarizerSet, qualifierId, qualifierSet);
            result[9] = calculator.t2_t9(tanks, new TraitId[]{qualifierId}, new double[][]{qualifierSet.getAbcd()});
            result[10] = calculator.t8_t10(tanks, new TraitId[]{qualifierId}, new FuzzySet[]{qualifierSet});
            result[11] = calculator.t5_11(new TraitId[]{qualifierId});
        }
        result[1] = quantifierSet.belong(t1Values[0] / t1Values[1]);
        result[2] = calculator.t2_t9(tanks, new TraitId[]{summarizerId}, new double[][]{summarizerSet.getAbcd()});
        result[3] = calculator.t3(tanks, new TraitId[]{summarizerId}, new double[][]{summarizerSet.getAbcd()}, qualifierId, qualifierSet.getAbcd());
        result[4] = calculator.t4(tanks, new TraitId[]{summarizerId}, result[2]);
        result[5] = calculator.t5_11(new TraitId[]{summarizerId});
        result[6] = calculator.t6(tanks.size(), quantifierIsRelative, quantifierSet.getAbcd());
        result[7] = calculator.t7(tanks.size(), quantifierIsRelative, quantifierSet.getAbcd());
        result[8] = calculator.t8_t10(tanks, new TraitId[]{summarizerId}, new FuzzySet[]{summarizerSet});

        double sum = 0;
        double divider = 0;
        for (int i = 0; i < 11; i++) {
            sum += weights[i] * result[i];
            divider += weights[i];
        }
        result[0] = sum / divider;
        return result;
    }
}
