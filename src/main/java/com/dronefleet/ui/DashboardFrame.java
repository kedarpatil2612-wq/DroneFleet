package com.dronefleet.ui;

import com.dronefleet.dao.DroneDAO;
import com.dronefleet.dao.MissionDAO;
import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {
    private final JLabel summary=new JLabel("Loading fleet statistics...");
    public DashboardFrame(){
        super("DroneFleet | Intelligent Drone Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);setSize(1100,700);setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(245,247,251));
        JPanel header=new JPanel(new BorderLayout());header.setBackground(new Color(18,29,48));header.setBorder(BorderFactory.createEmptyBorder(18,24,18,24));
        JLabel title=new JLabel("DRONEFLEET  /  COMMAND CENTER");title.setForeground(Color.WHITE);title.setFont(new Font("SansSerif",Font.BOLD,22));
        summary.setForeground(new Color(205,218,235));summary.setFont(new Font("SansSerif",Font.PLAIN,13));
        header.add(title,BorderLayout.WEST);header.add(summary,BorderLayout.SOUTH);add(header,BorderLayout.NORTH);
        JTabbedPane tabs=new JTabbedPane();tabs.addTab("Overview",overview());tabs.addTab("Drone Fleet",new DronePanel());
        tabs.addTab("Missions",new MissionPanel());tabs.addTab("Telemetry Simulator",new TelemetryPanel());tabs.addTab("A* Route Planner",new RoutePlannerPanel());add(tabs,BorderLayout.CENTER);
        refreshSummary();
    }
    private JPanel overview(){
        JPanel p=new JPanel(new BorderLayout(15,15));p.setBorder(BorderFactory.createEmptyBorder(25,25,25,25));
        JLabel welcome=new JLabel("<html><h1>Fleet Operations Overview</h1><p>Manage drones, schedule missions and monitor simulated flight telemetry.</p></html>");
        p.add(welcome,BorderLayout.NORTH);
        JTextArea notes=new JTextArea("QUICK START\n\n1. Open Drone Fleet and register your drones.\n2. Create a mission in the Missions tab.\n3. Assign an available drone with at least 30% battery.\n4. Start, complete or cancel the mission.\n5. Generate simulated telemetry to monitor battery, altitude, speed and coordinates.\n\nSAFETY NOTE\nThis is an educational desktop simulation. It does not connect to or control real drones. Route conflict checks are illustrative only.");
        notes.setEditable(false);notes.setFont(new Font("SansSerif",Font.PLAIN,15));notes.setBackground(new Color(250,251,253));notes.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));p.add(notes,BorderLayout.CENTER);
        JButton refresh=new JButton("Refresh Statistics");refresh.addActionListener(e->refreshSummary());p.add(refresh,BorderLayout.SOUTH);return p;
    }
    private void refreshSummary(){
        try{
            int drones=new DroneDAO().findAll().size();int missions=new MissionDAO().findAll().size();
            summary.setText("Registered drones: "+drones+"     |     Missions: "+missions+"     |     Mode: SIMULATION");
        }catch(Exception ex){summary.setText("Database not connected — check setup instructions.");}
    }
}
