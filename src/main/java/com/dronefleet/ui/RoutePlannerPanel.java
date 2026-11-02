package com.dronefleet.ui;

import com.dronefleet.service.GridPathfinder;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/** Interactive educational grid-based route planner using Java A*. */
public class RoutePlannerPanel extends JPanel {
    private static final int ROWS = 16, COLS = 24;
    private final int[][] grid = new int[ROWS][COLS];
    private final GridPathfinder pathfinder = new GridPathfinder();
    private final GridPathfinder.Cell start = new GridPathfinder.Cell(1, 1);
    private final GridPathfinder.Cell goal = new GridPathfinder.Cell(14, 22);
    private List<GridPathfinder.Cell> path = List.of();
    private final GridCanvas canvas = new GridCanvas();
    private final JLabel status = new JLabel("Click cells to add/remove obstacles, then calculate a route.");

    public RoutePlannerPanel() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton calculate = new JButton("Calculate A* Route");
        JButton clear = new JButton("Clear Obstacles");
        JButton demo = new JButton("Load Demo Obstacles");
        toolbar.add(calculate); toolbar.add(demo); toolbar.add(clear);
        add(toolbar, BorderLayout.NORTH);
        add(canvas, BorderLayout.CENTER);
        status.setBorder(BorderFactory.createEmptyBorder(6, 4, 4, 4));
        add(status, BorderLayout.SOUTH);
        calculate.addActionListener(e -> calculate());
        clear.addActionListener(e -> { clearGrid(); path = List.of(); canvas.repaint(); status.setText("Grid cleared."); });
        demo.addActionListener(e -> { loadDemo(); calculate(); });
        loadDemo();
        calculate();
    }

    private void calculate() {
        path = pathfinder.findPath(grid, start, goal);
        status.setText(path.isEmpty() ? "No safe route found. Change obstacles and retry."
            : "Route found: " + path.size() + " grid cells. A* simulation only.");
        canvas.repaint();
    }

    private void clearGrid() {
        for (int[] row : grid) java.util.Arrays.fill(row, 0);
    }

    private void loadDemo() {
        clearGrid();
        for (int r = 2; r < 13; r++) if (r != 8) grid[r][8] = 1;
        for (int c = 10; c < 22; c++) if (c != 17) grid[6][c] = 1;
        for (int r = 8; r < 15; r++) if (r != 11) grid[r][15] = 1;
        path = List.of();
        canvas.repaint();
    }

    private class GridCanvas extends JPanel {
        private static final int CELL = 30;
        GridCanvas() {
            setPreferredSize(new Dimension(COLS * CELL, ROWS * CELL));
            setBackground(Color.WHITE);
            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                    int col = e.getX() / CELL, row = e.getY() / CELL;
                    if (row < 0 || row >= ROWS || col < 0 || col >= COLS) return;
                    GridPathfinder.Cell cell = new GridPathfinder.Cell(row, col);
                    if (cell.equals(start) || cell.equals(goal)) return;
                    grid[row][col] = 1 - grid[row][col];
                    path = List.of();
                    status.setText("Obstacle grid edited. Recalculate route.");
                    repaint();
                }
            });
        }
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (int r = 0; r < ROWS; r++) for (int c = 0; c < COLS; c++) {
                int x = c * CELL, y = r * CELL;
                if (grid[r][c] == 1) g.setColor(new Color(55, 65, 81));
                else g.setColor(new Color(239, 243, 248));
                g.fillRect(x, y, CELL, CELL);
                g.setColor(new Color(210, 218, 228));
                g.drawRect(x, y, CELL, CELL);
            }
            g.setColor(new Color(35, 130, 220));
            for (GridPathfinder.Cell cell : path) {
                g.fillRoundRect(cell.col()*CELL+7, cell.row()*CELL+7, CELL-14, CELL-14, 8, 8);
            }
            drawMarker(g, start, new Color(22, 163, 74), "S");
            drawMarker(g, goal, new Color(220, 38, 38), "G");
        }
        private void drawMarker(Graphics g, GridPathfinder.Cell cell, Color color, String label) {
            g.setColor(color);
            g.fillOval(cell.col()*CELL+3, cell.row()*CELL+3, CELL-6, CELL-6);
            g.setColor(Color.WHITE);
            g.setFont(new Font("SansSerif", Font.BOLD, 13));
            g.drawString(label, cell.col()*CELL+11, cell.row()*CELL+19);
        }
    }
}
