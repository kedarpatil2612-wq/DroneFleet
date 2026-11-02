package com.dronefleet.ui;

import com.dronefleet.model.Mission;
import com.dronefleet.model.Drone;
import com.dronefleet.service.MissionService;
import com.dronefleet.service.FleetService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MissionPanel extends JPanel {
    private final MissionService service=new MissionService();
    private final FleetService fleet=new FleetService();
    private final DefaultTableModel model=new DefaultTableModel(new String[]{"ID","Mission","Type","Destination","Drone ID","Priority","Status","Scheduled"},0) {
        public boolean isCellEditable(int r,int c){return false;}
    };
    private final JTable table=new JTable(model);
    public MissionPanel() {
        setLayout(new BorderLayout(10,10));setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
        table.setRowHeight(26);add(new JScrollPane(table),BorderLayout.CENTER);
        JPanel actions=new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton create=new JButton("Create Mission"),assign=new JButton("Assign Drone"),start=new JButton("Start"),complete=new JButton("Complete"),cancel=new JButton("Cancel"),refresh=new JButton("Refresh");
        for(JButton b:new JButton[]{create,assign,start,complete,cancel,refresh})actions.add(b);
        add(actions,BorderLayout.NORTH);
        create.addActionListener(e->createMission());assign.addActionListener(e->assignDrone());
        start.addActionListener(e->change("IN_PROGRESS"));complete.addActionListener(e->change("COMPLETED"));
        cancel.addActionListener(e->change("CANCELLED"));refresh.addActionListener(e->load());load();
    }
    private void load(){
        model.setRowCount(0);
        try{DateTimeFormatter f=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            for(Mission m:service.allMissions())model.addRow(new Object[]{m.getId(),m.getName(),m.getType(),m.getDestination(),m.getDroneId()==null?"—":m.getDroneId(),m.getPriority(),m.getStatus(),m.getScheduledAt().format(f)});
        }catch(Exception ex){error(ex);}
    }
    private void createMission(){
        JTextField name=new JTextField(),destination=new JTextField();
        JComboBox<String> type=new JComboBox<>(new String[]{"SURVEILLANCE","DELIVERY","MAPPING","SEARCH_AND_RESCUE"});
        JComboBox<String> priority=new JComboBox<>(new String[]{"LOW","MEDIUM","HIGH","CRITICAL"});
        JPanel p=new JPanel(new GridLayout(0,2,8,8));
        p.add(new JLabel("Mission name"));p.add(name);p.add(new JLabel("Type"));p.add(type);
        p.add(new JLabel("Destination"));p.add(destination);p.add(new JLabel("Priority"));p.add(priority);
        p.add(new JLabel("Schedule"));p.add(new JLabel("Immediately (current time)"));
        if(JOptionPane.showConfirmDialog(this,p,"New Mission",JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION){
            try{service.create(name.getText(),(String)type.getSelectedItem(),destination.getText(),(String)priority.getSelectedItem(),LocalDateTime.now());load();}
            catch(Exception ex){error(ex);}
        }
    }
    private void assignDrone(){
        int row=table.getSelectedRow();if(row<0){JOptionPane.showMessageDialog(this,"Select a mission.");return;}
        try{
            var drones=fleet.allDrones().stream().filter(d->"AVAILABLE".equals(d.getStatus())&&d.getBattery()>=30).toList();
            if(drones.isEmpty()){JOptionPane.showMessageDialog(this,"No available drone with battery >= 30%.");return;}
            JComboBox<Drone> choices=new JComboBox<>(drones.toArray(new Drone[0]));
            choices.setRenderer((list,value,index,selected,focus)->new JLabel(value==null?"":value.getCode()+" — "+value.getModel()+" ("+value.getBattery()+"%)"));
            if(JOptionPane.showConfirmDialog(this,choices,"Choose Drone",JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION){
                service.assign((int)model.getValueAt(row,0),((Drone)choices.getSelectedItem()).getId());load();
            }
        }catch(Exception ex){error(ex);}
    }
    private void change(String status){
        int row=table.getSelectedRow();if(row<0){JOptionPane.showMessageDialog(this,"Select a mission.");return;}
        try{service.updateStatus((int)model.getValueAt(row,0),status);load();}catch(Exception ex){error(ex);}
    }
    private void error(Exception ex){JOptionPane.showMessageDialog(this,ex.getMessage(),"DroneFleet",JOptionPane.ERROR_MESSAGE);}
}
