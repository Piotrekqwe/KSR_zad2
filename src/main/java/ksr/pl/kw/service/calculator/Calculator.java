package ksr.pl.kw.service.calculator;

import ksr.pl.kw.model.Quantifier;
import ksr.pl.kw.model.fuzzy.FuzzySet;
import ksr.pl.kw.model.tanks.Tank;
import ksr.pl.kw.model.traits.Trait;
import ksr.pl.kw.model.traits.TraitId;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class Calculator {
    private List<Trait> traits;
    private Quantifier relativeQuantifiers;
    private Quantifier absoluteQuantifiers;

    private static final Calculator INSTANCE = new CalculatorStateSerializer().loadCalculator();

    public static Calculator getInstance() {
        return INSTANCE;
    }

    Calculator(List<Trait> traits, Quantifier relativeQuantifiers, Quantifier absoluteQuantifiers) {
        this.traits = traits;
        this.relativeQuantifiers = relativeQuantifiers;
        this.absoluteQuantifiers = absoluteQuantifiers;
    }

    public List<Trait> getTraits() {
        return traits;
    }

    public Quantifier getRelativeQuantifiers() {
        return relativeQuantifiers;
    }

    public Quantifier getAbsoluteQuantifiers() {
        return absoluteQuantifiers;
    }

    public void setTraits(ArrayList<Trait> traits) {
        this.traits = traits;
    }

    public void setRelativeQuantifiers(Quantifier relativeQuantifiers) {
        this.relativeQuantifiers = relativeQuantifiers;
    }

    public void setAbsoluteQuantifiers(Quantifier absoluteQuantifiers) {
        this.absoluteQuantifiers = absoluteQuantifiers;
    }

    public Trait getTraitById(TraitId id) {
        for (Trait trait : traits) {
            if (trait.getId() == id) {
                return trait;
            }
        }
        return null;
    }

    public double cardinalNumber(HashSet<Tank> tanks, TraitId summarizerId, FuzzySet summarizerSet, boolean quantifierIsRelative) {
        double result = 0;
        for (Tank tank : tanks) {
            result += summarizerSet.belong(tank.getTraits().get(summarizerId));
        }
        if (quantifierIsRelative) {
            result /= tanks.size();
        }
        return result;
    }

    public double cardinalNumberWithSummarizer(HashSet<Tank> tanks, TraitId summarizerId, FuzzySet summarizerSet, TraitId qualifierId, FuzzySet qualifierSet) {
        double result = 0;
        double divider = 0;
        for (Tank tank : tanks) {
            double summarizerBelong = summarizerSet.belong(tank.getTraits().get(summarizerId));
            double qualifierBelong = qualifierSet.belong(tank.getTraits().get(qualifierId));
            result += Math.min(summarizerBelong, qualifierBelong);
            divider += qualifierSet.belong(tank.getTraits().get(qualifierId));
        }
        return result / divider;
    }

    private boolean supp(double value, double[] set) {
        return (value > set[0] && value < set[set.length - 1]);
    }

    private double suppSet(HashSet<Tank> tanks, TraitId id, double[] abcd) {
        int resoult = 0;
        for (Tank tank : tanks) {
            if (supp(tank.getTraits().get(id), abcd)) resoult++;
        }
        return (double) resoult / tanks.size();
    }

    public double t2_t9(HashSet<Tank> tanks, TraitId[] ids, double[][] sets) {
        int[] sup = new int[ids.length];
        for (int i = 0; i < ids.length; i++) {
            for (Tank tank : tanks) {
                double val = tank.getTraits().get(ids[i]);
                if (supp(val, sets[i])) sup[i]++;
            }
        }
        double result = 1;
        for (int s : sup) result *= s;
        result = Math.pow(result, (double) 1 / sup.length);
        result /= tanks.size();

        return 1 - result;
    }

    //nie jestem pewien czy t3 działa dobrze dla wielu sumatorów
    public double t3(HashSet<Tank> tanks, TraitId[] summarizerIds, double[][] summarizerSets, TraitId qualifierId, FuzzySet qualifierSet) {
        HashSet<Tank> remainingTanks = (HashSet<Tank>) tanks.clone();
        if (qualifierId != null) {
            ArrayList<Tank> toRemoveList = new ArrayList<>();
            remainingTanks.forEach(tank -> {
                if (supp(tank.getTraits().get(qualifierId), qualifierSet.getAbcd()))
                    toRemoveList.add(tank);
            });
            for (Tank tank : toRemoveList) {
                remainingTanks.remove(tank);
            }
        }
        int M = remainingTanks.size();
        for (int i = 0; i < summarizerIds.length; i++) {
            int finalI = i;
            ArrayList<Tank> toRemoveList = new ArrayList<>();
            remainingTanks.forEach(tank -> {
                if (supp(tank.getTraits().get(summarizerIds[finalI]), summarizerSets[finalI]))
                    toRemoveList.add(tank);
            });
            for (Tank tank : toRemoveList) {
                remainingTanks.remove(tank);
            }
        }

        return (double) remainingTanks.size() / M;
    }

    //nie jestem pewien czy t4 działa dobrze dla wielu sumatorów
    public double t4(HashSet<Tank> tanks, TraitId[] summarizerIds, double t3) {
        double resoult = 1;
        for (TraitId id : summarizerIds) {
            for (FuzzySet set : getTraitById(id).getSets()) {
                resoult *= suppSet(tanks, id, set.getAbcd());
            }
        }
        return Math.abs(resoult - t3);
    }

    public double t5_11(TraitId[] summarizerIds) {
        double resoult = 2;
        for (TraitId id : summarizerIds)
            for (FuzzySet set : getTraitById(id).getSets())
                resoult /= 2;
        return resoult;
    }

    public double t6(int setSize, boolean quantifierIsRelative, double[] quantifierSet) {
        double resoult = quantifierSet[quantifierSet.length - 1] - quantifierSet[0];
        if (quantifierIsRelative) {
            return resoult;
        } else {
            return resoult / setSize;
        }
    }

    public double t7(int setSize, boolean quantifierIsRelative, double[] quantifierSet) {
        double resoult = 0;
        resoult += quantifierSet[1] - quantifierSet[0] / 2;
        resoult += quantifierSet[quantifierSet.length - 1] - quantifierSet[quantifierSet.length - 2] / 2;
        resoult += quantifierSet[quantifierSet.length - 2] - quantifierSet[1];
        if (!quantifierIsRelative) resoult /= setSize;
        return resoult;
    }

    public double t8_t10(HashSet<Tank> tanks, TraitId[] summarizerIds, FuzzySet[] summarizerSets) {
        double[] sup = new double[summarizerIds.length];
        for (int i = 0; i < summarizerIds.length; i++) {
            for (Tank tank : tanks) {
                double val = tank.getTraits().get(summarizerIds[i]);
                sup[i] += summarizerSets[i].belong(val);
            }
        }
        double result = 1;
        for (double s : sup) result *= s;
        result = Math.pow(result, (double) 1 / sup.length);
        result /= tanks.size();

        return 1 - result;
    }

}
