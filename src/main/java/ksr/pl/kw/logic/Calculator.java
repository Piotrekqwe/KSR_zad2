package ksr.pl.kw.logic;

import ksr.pl.kw.logic.fuzzy.FuzzySet;
import ksr.pl.kw.logic.fuzzy.Quantifier;
import ksr.pl.kw.logic.fuzzy.Trait;
import ksr.pl.kw.logic.fuzzy.TraitId;
import ksr.pl.kw.logic.tank.Tank;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;


public class Calculator {
    private ArrayList<Trait> traits;
    private Quantifier relativeQuantifiers;
    private Quantifier absoluteQuantifiers;
    public static String TRAIT_FILE_PATH = "data\\traits.bin";
    public static String RELATIVE_QUANTIFIERS_PATH = "data\\relative_quantifiers.bin";
    public static String ABSOLUTE_QUANTIFIERS_PATH = "data\\absolute_quantifiers.bin";

    public Calculator() {
        if (new File(TRAIT_FILE_PATH).exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(TRAIT_FILE_PATH))) {
                traits = (ArrayList<Trait>) in.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            traits = (ArrayList<Trait>) Arrays.stream(TraitId.values()).map(id -> new Trait(id, new ArrayList<>())).collect(Collectors.toList());
        }
        if (new File(RELATIVE_QUANTIFIERS_PATH).exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(RELATIVE_QUANTIFIERS_PATH))) {
                relativeQuantifiers = (Quantifier) in.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            relativeQuantifiers = new Quantifier(Quantifier.RELATIVE_QUANTIFIERS_NAME, new ArrayList<>());
        }
        if (new File(ABSOLUTE_QUANTIFIERS_PATH).exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(ABSOLUTE_QUANTIFIERS_PATH))) {
                absoluteQuantifiers = (Quantifier) in.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            absoluteQuantifiers = new Quantifier(Quantifier.ABSOLUTE_QUANTIFIERS_NAME, new ArrayList<>());
        }
    }

    public void save() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(TRAIT_FILE_PATH))) {
            out.writeObject(traits);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(RELATIVE_QUANTIFIERS_PATH))) {
            out.writeObject(relativeQuantifiers);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(ABSOLUTE_QUANTIFIERS_PATH))) {
            out.writeObject(absoluteQuantifiers);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Trait> getTraits() {
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

    public Trait getTraitById(TraitId id){
        for(Trait trait : traits) {
            if (trait.getId() == id) {
                return trait;
            }
        }
        return null;
    }
    public static double belong(double value, double[] abcd){
        if(value < abcd[0] || value > abcd[abcd.length - 1]) return 0;
        if(value < abcd[1]) return (value - abcd[0]) / (abcd[1] - abcd[0]);
        if(value > abcd[abcd.length - 2]) return (abcd[abcd.length - 1] - value) / (abcd[abcd.length - 1] - abcd[abcd.length - 2]);
        return 1;
    }
    private double[] t1form1(HashSet<Tank> tanks, boolean quantifierIsRelative, TraitId summarizerId, double[] summarizerSet){
        double[] result = new double[]{0,0};
        for(Tank tank : tanks){
            result[0] += belong(tank.getTraits().get(summarizerId), summarizerSet);
        }
        if(quantifierIsRelative){
            for(Tank tank : tanks){
                result[1]++;
            }
        }
        else result[1] = 1;
        return result;
    }
    private double[] t1form2(HashSet<Tank> tanks, TraitId summarizerId, double[] summarizerSet, TraitId qualifierId, double[] qualifierSet) {
        double[] result = new double[]{0, 0};
        for (Tank tank : tanks) {
            result[0] += Math.min(belong(tank.getTraits().get(summarizerId), summarizerSet), belong(tank.getTraits().get(qualifierId), qualifierSet));
        }
        for (Tank tank : tanks) {
            result[1] += belong(tank.getTraits().get(qualifierId), qualifierSet);
        }
        return result;
    }
    private boolean supp(double value, double[] set){
        return (value > set[0] && value < set[set.length - 1]);
    }
    private double suppSet(HashSet<Tank> tanks, TraitId id, double[] abcd){
        int resoult = 0;
        for(Tank tank : tanks){
            if(supp(tank.getTraits().get(id), abcd)) resoult++;
        }
        return (double) resoult / tanks.size();
    }
    private double t2_t9(HashSet<Tank> tanks, TraitId[] ids, double[][] sets){
        int[] sup = new int[ids.length];
        for(int i = 0; i < ids.length; i++){
            for(Tank tank : tanks){
                double val = tank.getTraits().get(ids[i]);
                if(supp(val, sets[i])) sup[i]++;
            }
        }
        double result = 1;
        for(int s : sup) result *= s;
        result = Math.pow(result, (double) 1 / sup.length);
        result /= tanks.size();

        return 1 - result;
    }
    //nie jestem pewien czy t3 działa dobrze dla wielu sumatorów
    private double t3(HashSet<Tank> tanks, TraitId[] summarizerIds, double[][] summarizerSets, TraitId qualifierId, double[] qualifierSet){
        HashSet<Tank> remainingTanks = (HashSet<Tank>) tanks.clone();
        if(qualifierId != null){
            ArrayList<Tank> toRemoveList = new ArrayList<>();
            remainingTanks.forEach(tank -> {
                if(supp(tank.getTraits().get(qualifierId), qualifierSet))
                    toRemoveList.add(tank);
            });
            for(Tank tank : toRemoveList){
                remainingTanks.remove(tank);
            }
        }
        int M = remainingTanks.size();
        for (int i = 0; i < summarizerIds.length; i++){
            int finalI = i;
            ArrayList<Tank> toRemoveList = new ArrayList<>();
            remainingTanks.forEach(tank -> {
                if(supp(tank.getTraits().get(summarizerIds[finalI]), summarizerSets[finalI]))
                    toRemoveList.add(tank);
            });
            for(Tank tank : toRemoveList){
                remainingTanks.remove(tank);
            }
        }

        return (double)remainingTanks.size()/M;
    }
    //nie jestem pewien czy t4 działa dobrze dla wielu sumatorów
    private double t4(HashSet<Tank> tanks, TraitId[] summarizerIds, double t3){
        double resoult = 1;
        for(TraitId id : summarizerIds){
            for(FuzzySet set : getTraitById(id).getSets()){
                resoult *= suppSet(tanks, id, set.getAbcd());
            }
        }
        return Math.abs(resoult - t3);
    }
    private double t5_11(TraitId[] summarizerIds){
        double resoult = 2;
        for(TraitId id : summarizerIds)
            for(FuzzySet set : getTraitById(id).getSets())
                resoult /= 2;
        return resoult;
    }
    private double t6(int setSize, boolean quantifierIsRelative, double[] quantifierSet){
        double resoult = quantifierSet[quantifierSet.length - 1] - quantifierSet[0];
        if(quantifierIsRelative){
            return resoult;
        }
        else{
            return resoult / setSize;
        }
    }
    private double t7(int setSize, boolean quantifierIsRelative, double[] quantifierSet){
        double resoult = 0;
        resoult += quantifierSet[1] - quantifierSet[0] / 2;
        resoult += quantifierSet[quantifierSet.length - 1] - quantifierSet[quantifierSet.length - 2] / 2;
        resoult += quantifierSet[quantifierSet.length - 2] - quantifierSet[1];
        if(!quantifierIsRelative) resoult /= setSize;
        return resoult;
    }
    private double t8_t10(HashSet<Tank> tanks, TraitId[] summarizerIds, double[][] summarizerSets){
        double[] sup = new double[summarizerIds.length];
        for(int i = 0; i < summarizerIds.length; i++){
            for(Tank tank : tanks){
                double val = tank.getTraits().get(summarizerIds[i]);
                sup[i] += belong(val, summarizerSets[i]);
            }
        }
        double result = 1;
        for(double s : sup) result *= s;
        result = Math.pow(result, (double) 1 / sup.length);
        result /= tanks.size();

        return 1 - result;
    }
    public double[] oneSubjectSummary(HashSet<Tank> tanks, boolean quantifierIsRelative, double[] quantifierSet,
                                      TraitId summarizerId, double[] summarizerSet,
                                      TraitId qualifierId, double[] qualifierSet) {
        double[] result = new double[11];

        double[] t1Values;
        if (qualifierId == null) {
            t1Values = t1form1(tanks, quantifierIsRelative, summarizerId, summarizerSet);
            result[8] = 0;
            result[9] = 0;
            result[10] = 0;
        }
        else{
            t1Values = t1form2(tanks, summarizerId, summarizerSet, qualifierId, qualifierSet);
            result[8] = t2_t9(tanks, new TraitId[]{qualifierId}, new double[][]{qualifierSet});
            result[9] = t8_t10(tanks, new TraitId[]{qualifierId}, new double[][]{qualifierSet});
            result[10] = t5_11(new TraitId[]{qualifierId});
        }
        result[0] = belong(t1Values[0] / t1Values[1], quantifierSet);
        result[1] = t2_t9(tanks, new TraitId[]{summarizerId}, new double[][]{summarizerSet});
        result[2] = t3(tanks, new TraitId[]{summarizerId}, new double[][]{summarizerSet}, qualifierId, qualifierSet);
        result[3] = t4(tanks, new TraitId[]{summarizerId}, result[2]);
        result[4] = t5_11(new TraitId[]{summarizerId});
        result[5] = t6(tanks.size(), quantifierIsRelative, quantifierSet);
        result[6] = t7(tanks.size(), quantifierIsRelative, quantifierSet);
        result[7] = t8_t10(tanks, new TraitId[]{summarizerId}, new double[][]{summarizerSet});

        return result;
    }

}
