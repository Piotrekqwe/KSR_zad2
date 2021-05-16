package ksr.pl.kw.logic.tank;

import java.util.Arrays;

public class Tank {
    private TraitValue[] traits;
    private TankType tankType;
    private int tier;
    private String nation;
    private String name;

    public Tank(TraitValue[] traits, TankType tankType, int tier, String nation, String name) {
        this.traits = traits;
        this.tankType = tankType;
        this.tier = tier;
        this.nation = nation;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Tank{" +
                "traits=" + Arrays.toString(traits) +
                ", tankType=" + tankType +
                ", tier=" + tier +
                ", nation='" + nation + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
