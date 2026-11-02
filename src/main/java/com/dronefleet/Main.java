package com.dronefleet;

import com.dronefleet.config.DBConnection;
import com.dronefleet.ui.DashboardFrame;
import javax.swing.*;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try (Connection ignored = DBConnection.getConnection()) {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                new DashboardFrame().setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                    "Cannot connect to MySQL.\n\n1. Start MySQL.\n2. Run database/schema.sql.\n3. Configure DRONE_DB_USER and DRONE_DB_PASSWORD.\n\nDetails: "+ex.getMessage(),
                    "DroneFleet - Database Connection Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
