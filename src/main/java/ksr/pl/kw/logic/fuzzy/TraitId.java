package ksr.pl.kw.logic.fuzzy;

public enum TraitId {
    PRICE_CREDIT("price_credit", "cena"),
    HP("hp", "wytrzymałość");
    //HULL_HP,
    //WEIGHT,
    //SPEED_FORWARD,
    //SPEED_BACKWARD,
    //AVG_PENETRATION,
    //AVG_DAMAGE,
    //DPM,
    //FIRE_RATE,
    //ENGINE_POWER,
    //RADIO;



    public final String dbName;
    public final String uiName;

    TraitId(String dbName, String uiName) {
        this.dbName = dbName;
        this.uiName = uiName;
    }

}
