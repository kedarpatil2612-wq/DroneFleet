package com.dronefleet.dao;

import com.dronefleet.config.DBConnection;
import com.dronefleet.model.Mission;
import java.sql.*;
import java.util.*;

public class MissionDAO {
    public List<Mission> findAll() throws SQLException {
        List<Mission> rows=new ArrayList<>();
        String sql="SELECT * FROM missions ORDER BY mission_id DESC";
        try(Connection c=DBConnection.getConnection(); PreparedStatement p=c.prepareStatement(sql);
            ResultSet r=p.executeQuery()) {
            while(r.next()) {
                int did=r.getInt("assigned_drone_id");
                Integer droneId=r.wasNull()?null:did;
                rows.add(new Mission(r.getInt("mission_id"),r.getString("mission_name"),
                    r.getString("mission_type"),r.getString("destination"),droneId,
                    r.getString("priority"),r.getString("status"),r.getTimestamp("scheduled_at").toLocalDateTime()));
            }
        }
        return rows;
    }
    public void insert(String name,String type,String destination,String priority,Timestamp scheduled) throws SQLException {
        String sql="INSERT INTO missions(mission_name,mission_type,destination,priority,scheduled_at) VALUES(?,?,?,?,?)";
        try(Connection c=DBConnection.getConnection(); PreparedStatement p=c.prepareStatement(sql)) {
            p.setString(1,name);p.setString(2,type);p.setString(3,destination);p.setString(4,priority);p.setTimestamp(5,scheduled);p.executeUpdate();
        }
    }
    public void assign(int missionId,int droneId) throws SQLException {
        try(Connection c=DBConnection.getConnection()) {
            c.setAutoCommit(false);
            try {
                try(PreparedStatement check=c.prepareStatement(
                    "SELECT battery_level,status FROM drones WHERE drone_id=? FOR UPDATE")) {
                    check.setInt(1,droneId);
                    try(ResultSet r=check.executeQuery()) {
                        if(!r.next()) throw new SQLException("Selected drone does not exist.");
                        if(r.getInt("battery_level")<30) throw new SQLException("Drone battery must be at least 30%.");
                        if(!"AVAILABLE".equals(r.getString("status"))) throw new SQLException("Drone is not available.");
                    }
                }
                try(PreparedStatement p=c.prepareStatement(
                    "UPDATE missions SET assigned_drone_id=?,status='ASSIGNED' WHERE mission_id=? AND status IN ('PLANNED','ASSIGNED')")) {
                    p.setInt(1,droneId);p.setInt(2,missionId);
                    if(p.executeUpdate()==0) throw new SQLException("Mission cannot be assigned in its current status.");
                }
                try(PreparedStatement p=c.prepareStatement("UPDATE drones SET status='IN_MISSION' WHERE drone_id=?")) {
                    p.setInt(1,droneId);p.executeUpdate();
                }
                c.commit();
            } catch(SQLException ex) { c.rollback(); throw ex; }
            finally { c.setAutoCommit(true); }
        }
    }
    public void updateStatus(int missionId,String status) throws SQLException {
        try(Connection c=DBConnection.getConnection()) {
            c.setAutoCommit(false);
            try {
                Integer droneId=null;
                try(PreparedStatement p=c.prepareStatement("SELECT assigned_drone_id FROM missions WHERE mission_id=? FOR UPDATE")) {
                    p.setInt(1,missionId);try(ResultSet r=p.executeQuery()){if(r.next()){int x=r.getInt(1);if(!r.wasNull())droneId=x;}}
                }
                try(PreparedStatement p=c.prepareStatement("UPDATE missions SET status=? WHERE mission_id=?")) {
                    p.setString(1,status);p.setInt(2,missionId);p.executeUpdate();
                }
                if(droneId!=null && (status.equals("COMPLETED")||status.equals("CANCELLED"))) {
                    try(PreparedStatement p=c.prepareStatement("UPDATE drones SET status='AVAILABLE' WHERE drone_id=?")) {
                        p.setInt(1,droneId);p.executeUpdate();
                    }
                }
                c.commit();
            } catch(SQLException ex){c.rollback();throw ex;}
            finally{c.setAutoCommit(true);}
        }
    }
}
