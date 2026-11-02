package com.dronefleet.model;

import java.time.LocalDateTime;

public class Mission {
    private int id;
    private String name, type, destination, priority, status;
    private Integer droneId;
    private LocalDateTime scheduledAt;

    public Mission(int id, String name, String type, String destination, Integer droneId,
                   String priority, String status, LocalDateTime scheduledAt) {
        this.id=id; this.name=name; this.type=type; this.destination=destination;
        this.droneId=droneId; this.priority=priority; this.status=status; this.scheduledAt=scheduledAt;
    }
    public int getId(){ return id; }
    public String getName(){ return name; }
    public String getType(){ return type; }
    public String getDestination(){ return destination; }
    public Integer getDroneId(){ return droneId; }
    public String getPriority(){ return priority; }
    public String getStatus(){ return status; }
    public LocalDateTime getScheduledAt(){ return scheduledAt; }
}
