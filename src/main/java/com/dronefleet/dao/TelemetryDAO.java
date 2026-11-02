package com.dronefleet.dao;

import com.dronefleet.config.DBConnection;
import java.sql.*;

public class TelemetryDAO {
    public void insert(int droneId,int battery,double altitude,double speed,double lat,double lon) throws SQLException {
        String sql="INSERT INTO telemetry(drone_id,battery_level,altitude_m,speed_kmh,latitude,longitude) VALUES(?,?,?,?,?,?)";
        try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement(sql)) {
            p.setInt(1,droneId);p.setInt(2,battery);p.setDouble(3,altitude);
            p.setDouble(4,speed);p.setDouble(5,lat);p.setDouble(6,lon);p.executeUpdate();
        }
    }
}
