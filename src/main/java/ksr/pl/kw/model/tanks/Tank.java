package ksr.pl.kw.model.tanks;

import ksr.pl.kw.model.traits.TraitId;

import java.util.EnumMap;

public class Tank {
    private EnumMap<TraitId, Double> traits;
    private TankType tankType;
    private int tier;
    private String nation;
    private String name;

    public Tank(EnumMap<TraitId, Double> traits, TankType tankType, int tier, String nation, String name) {
        this.traits = traits;
        this.tankType = tankType;
        this.tier = tier;
        this.nation = nation;
        this.name = name;
    }

    public EnumMap<TraitId, Double> getTraits() {
        return traits;
    }

    public TankType getTankType() {
        return tankType;
    }

    public int getTier() {
        return tier;
    }

    public String getNation() {
        return nation;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Tank{" +
                "traits=" + traits +
                ", tankType=" + tankType +
                ", tier=" + tier +
                ", nation='" + nation + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
