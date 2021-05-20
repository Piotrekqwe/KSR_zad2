package ksr.pl.kw.logic;

import ksr.pl.kw.logic.fuzzy.Quantifier;
import ksr.pl.kw.logic.fuzzy.Trait;
import ksr.pl.kw.logic.fuzzy.TraitId;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class Calculator {
    private List<Trait> traits;
    private Quantifier relativeQuantifiers;
    private Quantifier absoluteQuantifiers;
    public static String TRAIT_FILE_PATH = "data\\traits.bin";
    public static String RELATIVE_QUANTIFIERS_PATH = "data\\relative_quantifiers.bin";
    public static String ABSOLUTE_QUANTIFIERS_PATH = "data\\absolute_quantifiers.bin";

    public Calculator() {
        if (new File(TRAIT_FILE_PATH).exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(TRAIT_FILE_PATH))) {
                traits = (List<Trait>) in.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            traits = Arrays.stream(TraitId.values()).map(id -> new Trait(id, new ArrayList<>())).collect(Collectors.toList());
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

    public List<Trait> getTraits() {
        return traits;
    }

    public Quantifier getRelativeQuantifiers() {
        return relativeQuantifiers;
    }

    public Quantifier getAbsoluteQuantifiers() {
        return absoluteQuantifiers;
    }

    public void setTraits(List<Trait> traits) {
        this.traits = traits;
    }

    public void setRelativeQuantifiers(Quantifier relativeQuantifiers) {
        this.relativeQuantifiers = relativeQuantifiers;
    }

    public void setAbsoluteQuantifiers(Quantifier absoluteQuantifiers) {
        this.absoluteQuantifiers = absoluteQuantifiers;
    }
}
