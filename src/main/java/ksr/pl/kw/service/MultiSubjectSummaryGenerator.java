package ksr.pl.kw.service;

import ksr.pl.kw.gui.multiSubjectSummaryType;
import ksr.pl.kw.model.fuzzy.FuzzySet;
import ksr.pl.kw.model.tanks.Tank;
import ksr.pl.kw.model.traits.Trait;
import ksr.pl.kw.model.traits.TraitId;
import ksr.pl.kw.service.calculator.Calculator;

import java.util.HashSet;

public class MultiSubjectSummaryGenerator {
    private static final Calculator calculator = Calculator.getInstance();
    private static final MultiSubjectSummaryGenerator INSTANCE = new MultiSubjectSummaryGenerator();

    public static MultiSubjectSummaryGenerator getInstance() {
        return INSTANCE;
    }

    private MultiSubjectSummaryGenerator() {
    }

    public double generateMultiSummary(multiSubjectSummaryType type, FuzzySet selectedQuantifier, TraitId qualifierId, FuzzySet qualifierSet,
                                       TraitId summarizerId, FuzzySet summarizerSet, HashSet<Tank> Set1, HashSet<Tank> Set2){
        double m1value;
        double m2value;
        if (type == multiSubjectSummaryType.TYPE3) {
            m1value = calculator.cardinalNumberWithSummarizer(Set1, summarizerId, summarizerSet, qualifierId, qualifierSet);
        }
        else{
            m1value = calculator.cardinalNumber(Set1, summarizerId, summarizerSet, true);
        }
        if (type == multiSubjectSummaryType.TYPE2) {
            m2value = calculator.cardinalNumberWithSummarizer(Set2, summarizerId, summarizerSet, qualifierId, qualifierSet);
        }
        else{
            m2value = calculator.cardinalNumber(Set2, summarizerId, summarizerSet, true);
        }
        if (type == multiSubjectSummaryType.TYPE4) {
            //uproszczony wzór Reichenbacha dla kwantyfikatora więcej (trujkątny a = 0, b = 1, c = 1)
            return m1value - m1value * m2value;
        }
        return selectedQuantifier.belong(m1value / (m1value + m2value));
    }
}
