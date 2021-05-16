package ksr.pl.kw.logic.tank;

import java.util.Arrays;

public class Tank {
    private TraitValue[] traits;
    private TankType tankType;
    private int tier;

    public Tank(TraitValue[] traits, TankType tankType, int tier, String nation, String name) {
        this.traits = traits;
        this.tankType = tankType;
        this.tier = tier;
        this.nation = nation;
        this.name = name;
    }

    public Tank(TankType tankType, int tier, String nation, String name) {
        this.tankType = tankType;
        this.tier = tier;
        this.nation = nation;
        this.name = name;
    }

    private String nation;
    private String name;
}
