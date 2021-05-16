package ksr.pl.kw.logic.tank;

import ksr.pl.kw.logic.fuzzy.TraitId;

import java.awt.image.BandedSampleModel;
import java.sql.*;
import java.util.ArrayList;

public class TankRepository {

    private Connection connection;
    private String url;

    public TankRepository(){
        connection = null;
        url =  "jdbc:sqlserver://localhost\\SQLEXPRESS:1433;databaseName=WOT;integratedSecurity=true;";
    }

    public void connect(String url){
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connection = DriverManager.getConnection(url);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void disconnect(){
        if(connection != null){
            try{
                connection.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    }


    public void displayAllRecords(){
        connect(url);
        String query = "select short_name, tier, is_wheeled, dpm from wot_data";
        Statement statement;
        ResultSet resultSet;
        try{
            statement = connection.createStatement();
            resultSet  = statement.executeQuery(query);
            while(resultSet.next()){
                System.out.println(
                        "short_name: " + resultSet.getString(1) + " " +
                        "tier: " + resultSet.getInt(2) + " " +
                        "is_wheeled: " + resultSet.getBoolean(3) + " " +
                        "dpm: " + resultSet.getFloat(4)
                );
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        disconnect();
    }


    public ArrayList<Tank> getAllTanks(){

        String query = "select short_name, tier, tank_type, nation, price_credit, hp, hull_hp, tank_weight, " +
                "speed_forward, speed_backward, ammo_avg_penetration, ammo_avg_damage, dpm, gun_fire_rate, " +
                "engine_power, radio from wot_data";

        return sendQueryToDataBase(query);
    }

    public ArrayList<Tank> getTanksByType(TankType tankType){

        String query = "select short_name, tier, tank_type, nation, price_credit, hp, hull_hp, tank_weight, " +
                "speed_forward, speed_backward, ammo_avg_penetration, ammo_avg_damage, dpm, gun_fire_rate, " +
                "engine_power, radio from wot_data where tank_type = '" + tankType.dbName + "'";

        return sendQueryToDataBase(query);
    }

    public ArrayList<Tank> getTanksByNation(String nation){

        String query = "select short_name, tier, tank_type, nation, price_credit, hp, hull_hp, tank_weight, " +
                "speed_forward, speed_backward, ammo_avg_penetration, ammo_avg_damage, dpm, gun_fire_rate, " +
                "engine_power, radio from wot_data where nation = '" + nation + "'";

        return sendQueryToDataBase(query);
    }

    public ArrayList<Tank> getTanksByTier(int tier){

        String query = "select short_name, tier, tank_type, nation, price_credit, hp, hull_hp, tank_weight, " +
                "speed_forward, speed_backward, ammo_avg_penetration, ammo_avg_damage, dpm, gun_fire_rate, " +
                "engine_power, radio from wot_data where tier = " + tier;

        return sendQueryToDataBase(query);
    }

    public ArrayList<Tank> getTanksByTrait(TraitId id, int ammount, boolean asc){

        String query = "select short_name, tier, tank_type, nation, price_credit, hp, hull_hp, tank_weight, " +
                "speed_forward, speed_backward, ammo_avg_penetration, ammo_avg_damage, dpm, gun_fire_rate, " +
                "engine_power, radio from wot_data order by " + id.dbName + (asc?"":" desc");

        return sendQueryToDataBase(query);
    }

    private ArrayList<Tank> sendQueryToDataBase(String query){

        connect(url);
        Statement statement;
        ResultSet resultSet;
        ArrayList<Tank> tanksList = new ArrayList<>();

        try{
            statement = connection.createStatement();
            resultSet  = statement.executeQuery(query);

            while(resultSet.next()){

                TraitValue[] traitValues = new TraitValue[12];
                for(int i=0; i<12; i++){
                    traitValues[i] = new TraitValue(
                            TraitId.valueOfLabel(resultSet.getMetaData().getColumnLabel(i+5)),
                            resultSet.getFloat(i+5)
                    );
                }

                tanksList.add(new Tank(
                        traitValues,
                        TankType.valueOfLabel(resultSet.getString(3)),
                        resultSet.getInt(2),
                        resultSet.getString(4),
                        resultSet.getString(1)
                ));

            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        disconnect();

        //Tank[] tab = tanksList.toArray(new Tank[tanksList.size()]);
        return tanksList;

    }

}
