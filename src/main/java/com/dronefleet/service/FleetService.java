package com.dronefleet.service;

import com.dronefleet.dao.DroneDAO;
import com.dronefleet.model.Drone;
import java.sql.SQLException;
import java.util.List;

public class FleetService {
    private final DroneDAO dao=new DroneDAO();
    public List<Drone> allDrones() throws SQLException { return dao.findAll(); }
    public void addDrone(String code,String model,int battery,String status,double payload) throws SQLException {
        validate(code,model,battery,payload); dao.insert(code.trim(),model.trim(),battery,status,payload);
    }
    public void editDrone(int id,String code,String model,int battery,String status,double payload) throws SQLException {
        validate(code,model,battery,payload); dao.update(id,code.trim(),model.trim(),battery,status,payload);
    }
    public void removeDrone(int id) throws SQLException { dao.delete(id); }
    private void validate(String code,String model,int battery,double payload) {
        if(code==null||code.isBlank()||model==null||model.isBlank()) throw new IllegalArgumentException("Drone code and model are required.");
        if(battery<0||battery>100) throw new IllegalArgumentException("Battery must be between 0 and 100.");
        if(payload<=0) throw new IllegalArgumentException("Payload must be greater than zero.");
    }
}
