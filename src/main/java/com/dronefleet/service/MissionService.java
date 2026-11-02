package com.dronefleet.service;

import com.dronefleet.dao.MissionDAO;
import com.dronefleet.model.Mission;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class MissionService {
    private final MissionDAO dao=new MissionDAO();
    public List<Mission> allMissions() throws SQLException { return dao.findAll(); }
    public void create(String name,String type,String destination,String priority,LocalDateTime scheduled) throws SQLException {
        if(name==null||name.isBlank()||destination==null||destination.isBlank()) throw new IllegalArgumentException("Mission name and destination are required.");
        dao.insert(name.trim(),type,destination.trim(),priority,Timestamp.valueOf(scheduled));
    }
    public void assign(int missionId,int droneId) throws SQLException { dao.assign(missionId,droneId); }
    public void updateStatus(int missionId,String status) throws SQLException { dao.updateStatus(missionId,status); }
}
