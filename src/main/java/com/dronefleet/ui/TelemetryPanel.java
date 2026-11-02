package com.dronefleet.ui;

import com.dronefleet.dao.DroneDAO;
import com.dronefleet.dao.TelemetryDAO;
import com.dronefleet.model.Drone;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class TelemetryPanel extends JPanel {
    private final JComboBox<Drone> drones=new JComboBox<>();
    private final JLabel battery=new JLabel("Battery: —"),altitude=new JLabel("Altitude: —"),speed=new JLabel("Speed: —"),location=new JLabel("Location: —");
    private final TelemetryDAO telemetry=new TelemetryDAO();
    public TelemetryPanel(){
        setLayout(new BorderLayout(12,12));setBorder(BorderFactory.createEmptyBorder(25,25,25,25));
        JPanel top=new JPanel(new FlowLayout(FlowLayout.LEFT));top.add(new JLabel("Select Drone:"));top.add(drones);
        JButton refresh=new JButton("Refresh Drones"),simulate=new JButton("Generate Telemetry");
        top.add(refresh);top.add(simulate);add(top,BorderLayout.NORTH);
        JPanel cards=new JPanel(new GridLayout(2,2,12,12));
        for(JLabel l:new JLabel[]{battery,altitude,speed,location}){l.setFont(new Font("SansSerif",Font.BOLD,20));l.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(210,215,225)),BorderFactory.createEmptyBorder(25,15,25,15)));cards.add(l);}
        add(cards,BorderLayout.CENTER);
        refresh.addActionListener(e->loadDrones());simulate.addActionListener(e->generate());loadDrones();
    }
    private void loadDrones(){
        drones.removeAllItems();
        try{List<Drone> all=new DroneDAO().findAll();for(Drone d:all)drones.addItem(d);
            drones.setRenderer((list,value,index,selected,focus)->new JLabel(value==null?"":value.getCode()+" — "+value.getModel()));
        }catch(Exception ex){error(ex);}
    }
    private void generate(){
        Drone d=(Drone)drones.getSelectedItem();if(d==null){JOptionPane.showMessageDialog(this,"Add a drone first.");return;}
        int b=Math.max(0,d.getBattery()-ThreadLocalRandom.current().nextInt(0,4));
        double a=ThreadLocalRandom.current().nextDouble(20,121),s=ThreadLocalRandom.current().nextDouble(5,45);
        double lat=18.5204+ThreadLocalRandom.current().nextDouble(-0.01,0.01),lon=73.8567+ThreadLocalRandom.current().nextDouble(-0.01,0.01);
        try{
            telemetry.insert(d.getId(),b,a,s,lat,lon);new DroneDAO().updateBattery(d.getId(),b);
            battery.setText("Battery: "+b+"%"+(b<25?"  LOW":""));altitude.setText(String.format("Altitude: %.1f m",a));
            speed.setText(String.format("Speed: %.1f km/h",s));location.setText(String.format("Location: %.5f, %.5f",lat,lon));
            if(b<25)JOptionPane.showMessageDialog(this,"Low battery warning. Return drone for charging.");
            loadDrones();
        }catch(Exception ex){error(ex);}
    }
    private void error(Exception ex){JOptionPane.showMessageDialog(this,ex.getMessage(),"Telemetry",JOptionPane.ERROR_MESSAGE);}
}
