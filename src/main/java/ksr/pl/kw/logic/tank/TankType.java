package ksr.pl.kw.logic.tank;

public enum TankType {
    LIGHT("lightTank", "lekki"),
    MEDIUM("mediumTank", "średni"),
    HEAVY("heavyTank", "ciężki"),
    TANK_DESTROYER("AT-SPG", "niszczyciel czołgów"),
    ARTILLERY("SPG", "artyleria");

    public final String dbName;
    public final String uiName;

    TankType(String dbName, String uiName) {
        this.dbName = dbName;
        this.uiName = uiName;
    }

}
