package com.dronefleet.dao;

import com.dronefleet.config.DBConnection;
import com.dronefleet.model.Drone;
import java.sql.*;
import java.util.*;

public class DroneDAO {
    public List<Drone> findAll() throws SQLException {
        String sql = "SELECT * FROM drones ORDER BY drone_id DESC";
        List<Drone> rows = new ArrayList<>();
        try (Connection c=DBConnection.getConnection(); PreparedStatement p=c.prepareStatement(sql);
             ResultSet r=p.executeQuery()) {
            while(r.next()) rows.add(map(r));
        }
        return rows;
    }
    public void insert(String code, String model, int battery, String status, double payload) throws SQLException {
        String sql="INSERT INTO drones(drone_code,model,battery_level,status,max_payload_kg) VALUES(?,?,?,?,?)";
        try(Connection c=DBConnection.getConnection(); PreparedStatement p=c.prepareStatement(sql)) {
            p.setString(1,code); p.setString(2,model); p.setInt(3,battery);
            p.setString(4,status); p.setDouble(5,payload); p.executeUpdate();
        }
    }
    public void update(int id, String code, String model, int battery, String status, double payload) throws SQLException {
        String sql="UPDATE drones SET drone_code=?,model=?,battery_level=?,status=?,max_payload_kg=? WHERE drone_id=?";
        try(Connection c=DBConnection.getConnection(); PreparedStatement p=c.prepareStatement(sql)) {
            p.setString(1,code); p.setString(2,model); p.setInt(3,battery);
            p.setString(4,status); p.setDouble(5,payload); p.setInt(6,id); p.executeUpdate();
        }
    }
    public void delete(int id) throws SQLException {
        try(Connection c=DBConnection.getConnection(); PreparedStatement p=c.prepareStatement("DELETE FROM drones WHERE drone_id=?")) {
            p.setInt(1,id); p.executeUpdate();
        }
    }
    public void updateBattery(int id, int battery) throws SQLException {
        try(Connection c=DBConnection.getConnection(); PreparedStatement p=c.prepareStatement("UPDATE drones SET battery_level=? WHERE drone_id=?")) {
            p.setInt(1,battery); p.setInt(2,id); p.executeUpdate();
        }
    }
    private Drone map(ResultSet r) throws SQLException {
        return new Drone(r.getInt("drone_id"),r.getString("drone_code"),r.getString("model"),
                r.getInt("battery_level"),r.getString("status"),r.getDouble("max_payload_kg"));
    }
}
