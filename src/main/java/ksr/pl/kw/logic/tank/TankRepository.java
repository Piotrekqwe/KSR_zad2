package ksr.pl.kw.logic.tank;

import ksr.pl.kw.logic.fuzzy.TraitId;

import java.awt.image.BandedSampleModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;

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


    public HashSet<Tank> getAllTanks(){

        String query = "select short_name, tier, tank_type, nation, price_credit, hp, hull_hp, tank_weight, " +
                "speed_forward, speed_backward, ammo_avg_penetration, ammo_avg_damage, dpm, gun_fire_rate, " +
                "engine_power, radio from wot_data";

        return sendQueryToDataBase(query);
    }

    public HashSet<Tank> getTanksByType(TankType tankType){

        String query = "select short_name, tier, tank_type, nation, price_credit, hp, hull_hp, tank_weight, " +
                "speed_forward, speed_backward, ammo_avg_penetration, ammo_avg_damage, dpm, gun_fire_rate, " +
                "engine_power, radio from wot_data where tank_type = '" + tankType.dbName + "'";

        return sendQueryToDataBase(query);
    }

    public HashSet<Tank> getTanksByNation(String nation){

        String query = "select short_name, tier, tank_type, nation, price_credit, hp, hull_hp, tank_weight, " +
                "speed_forward, speed_backward, ammo_avg_penetration, ammo_avg_damage, dpm, gun_fire_rate, " +
                "engine_power, radio from wot_data where nation = '" + nation + "'";

        return sendQueryToDataBase(query);
    }

    public HashSet<Tank> getTanksByTier(int tier){

        String query = "select short_name, tier, tank_type, nation, price_credit, hp, hull_hp, tank_weight, " +
                "speed_forward, speed_backward, ammo_avg_penetration, ammo_avg_damage, dpm, gun_fire_rate, " +
                "engine_power, radio from wot_data where tier = " + tier;

        return sendQueryToDataBase(query);
    }

    public HashSet<Tank> getTanksByTrait(TraitId id, int ammount, boolean asc){

        String query = "select short_name, tier, tank_type, nation, price_credit, hp, hull_hp, tank_weight, " +
                "speed_forward, speed_backward, ammo_avg_penetration, ammo_avg_damage, dpm, gun_fire_rate, " +
                "engine_power, radio from wot_data order by " + id.dbName + (asc?"":" desc");

        return sendQueryToDataBase(query);
    }

    private HashSet<Tank> sendQueryToDataBase(String query){

        connect(url);
        Statement statement;
        ResultSet resultSet;
        HashSet<Tank> tanksSet = new HashSet<>();

        try{
            statement = connection.createStatement();
            resultSet  = statement.executeQuery(query);

            while(resultSet.next()){

                EnumMap<TraitId, Double> traits = new EnumMap<>(TraitId.class);

                for(int i=0; i<12; i++){
                    traits.put(TraitId.valueOfLabel(resultSet.getMetaData().getColumnLabel(i+5)), (double) resultSet.getFloat(i+5));
                }
                tanksSet.add(new Tank(
                        traits,
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

        //Tank[] tab = tanksSet.toArray(new Tank[tanksSet.size()]);
        return tanksSet;

    }

}
