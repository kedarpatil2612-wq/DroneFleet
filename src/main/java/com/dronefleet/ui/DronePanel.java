package com.dronefleet.ui;

import com.dronefleet.model.Drone;
import com.dronefleet.service.FleetService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DronePanel extends JPanel {
    private final FleetService service=new FleetService();
    private final DefaultTableModel model=new DefaultTableModel(new String[]{"ID","Code","Model","Battery %","Status","Payload kg"},0) {
        public boolean isCellEditable(int r,int c){return false;}
    };
    private final JTable table=new JTable(model);
    public DronePanel() {
        setLayout(new BorderLayout(10,10)); setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
        table.setRowHeight(26); add(new JScrollPane(table),BorderLayout.CENTER);
        JPanel actions=new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton add=new JButton("Add Drone"), edit=new JButton("Edit Selected"), delete=new JButton("Delete Selected"), refresh=new JButton("Refresh");
        actions.add(add);actions.add(edit);actions.add(delete);actions.add(refresh);add(actions,BorderLayout.NORTH);
        add.addActionListener(e->editDrone(false)); edit.addActionListener(e->editDrone(true));
        delete.addActionListener(e->deleteDrone()); refresh.addActionListener(e->load());
        load();
    }
    private void load() {
        model.setRowCount(0);
        try { for(Drone d:service.allDrones()) model.addRow(new Object[]{d.getId(),d.getCode(),d.getModel(),d.getBattery(),d.getStatus(),d.getPayload()}); }
        catch(Exception ex){showError(ex);}
    }
    private void editDrone(boolean editing) {
        int row=table.getSelectedRow();
        if(editing&&row<0){JOptionPane.showMessageDialog(this,"Select a drone first.");return;}
        JTextField code=new JTextField(editing?model.getValueAt(row,1).toString():"");
        JTextField droneModel=new JTextField(editing?model.getValueAt(row,2).toString():"");
        JTextField battery=new JTextField(editing?model.getValueAt(row,3).toString():"100");
        JComboBox<String> status=new JComboBox<>(new String[]{"AVAILABLE","CHARGING","MAINTENANCE","IN_MISSION"});
        if(editing) status.setSelectedItem(model.getValueAt(row,4).toString());
        JTextField payload=new JTextField(editing?model.getValueAt(row,5).toString():"1.0");
        JPanel form=new JPanel(new GridLayout(0,2,8,8));
        form.add(new JLabel("Drone code"));form.add(code);form.add(new JLabel("Model"));form.add(droneModel);
        form.add(new JLabel("Battery (0-100)"));form.add(battery);form.add(new JLabel("Status"));form.add(status);
        form.add(new JLabel("Max payload (kg)"));form.add(payload);
        if(JOptionPane.showConfirmDialog(this,form,editing?"Edit Drone":"Add Drone",JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION) {
            try {
                if(editing) service.editDrone((int)model.getValueAt(row,0),code.getText(),droneModel.getText(),Integer.parseInt(battery.getText()),(String)status.getSelectedItem(),Double.parseDouble(payload.getText()));
                else service.addDrone(code.getText(),droneModel.getText(),Integer.parseInt(battery.getText()),(String)status.getSelectedItem(),Double.parseDouble(payload.getText()));
                load();
            } catch(Exception ex){showError(ex);}
        }
    }
    private void deleteDrone() {
        int row=table.getSelectedRow(); if(row<0){JOptionPane.showMessageDialog(this,"Select a drone first.");return;}
        if(JOptionPane.showConfirmDialog(this,"Delete selected drone?","Confirm",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
            try{service.removeDrone((int)model.getValueAt(row,0));load();}catch(Exception ex){showError(ex);}
        }
    }
    private void showError(Exception ex){JOptionPane.showMessageDialog(this,ex.getMessage(),"DroneFleet",JOptionPane.ERROR_MESSAGE);}
}
