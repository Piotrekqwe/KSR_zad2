package ksr.pl.kw.model.traits;

public enum TraitId {
    PRICE_CREDIT("price_credit", "cena"),
    HP("hp", "wytrzymałość"),
    HULL_HP("hull_hp", "wytrzymałość korpusu"),
    WEIGHT("tank_weight", "waga"),
    SPEED_FORWARD("speed_forward", "prędkość maksymalna w przód"),
    SPEED_BACKWARD("speed_backward", "prędkość maksymalna do tyłu"),
    AVG_PENETRATION("ammo_avg_penetration", "średnia penetracja pocisku"),
    AVG_DAMAGE("ammo_avg_damage", "średnie obrażenia zadane przy trafieniu"),
    DPM("dpm", "średnie obrażenia na minutę"),
    FIRE_RATE("gun_fire_rate", "szybkostrzelność"),
    ENGINE_POWER("engine_power", "moc silnika"),
    RADIO("radio", "zasięg sygnału radiowego");


    public final String dbName;
    public final String uiName;

    TraitId(String dbName, String uiName) {
        this.dbName = dbName;
        this.uiName = uiName;
    }

    public static TraitId valueOfLabel(String label) {
        for (TraitId e : values()) {
            if (e.dbName.equals(label)) {
                return e;
            }
        }
        return null;
    }
}
