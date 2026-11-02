package com.dronefleet.service;

import java.util.*;

/**
 * A* pathfinder for a 2D grid. 0 = free cell, 1 = blocked cell.
 * Uses 8-direction movement with diagonal corner-cut prevention.
 */
public class GridPathfinder {
    public record Cell(int row, int col) {}
    private record Node(Cell cell, double g, double f) {}

    private static final int[][] DIRECTIONS = {
        {-1,0},{1,0},{0,-1},{0,1},{-1,-1},{-1,1},{1,-1},{1,1}
    };

    public List<Cell> findPath(int[][] grid, Cell start, Cell goal) {
        validateGrid(grid);
        if (!inside(grid, start) || !inside(grid, goal))
            throw new IllegalArgumentException("Start and goal must be inside the grid.");
        if (grid[start.row()][start.col()] != 0 || grid[goal.row()][goal.col()] != 0)
            return List.of();

        PriorityQueue<Node> open = new PriorityQueue<>(Comparator.comparingDouble(Node::f));
        Map<Cell, Double> scores = new HashMap<>();
        Map<Cell, Cell> previous = new HashMap<>();
        Set<Cell> closed = new HashSet<>();
        scores.put(start, 0.0);
        open.add(new Node(start, 0.0, heuristic(start, goal)));

        while (!open.isEmpty()) {
            Node current = open.poll();
            Cell cell = current.cell();
            if (!closed.add(cell)) continue;
            if (cell.equals(goal)) return reconstruct(previous, goal);

            for (int[] d : DIRECTIONS) {
                int nr = cell.row() + d[0], nc = cell.col() + d[1];
                Cell next = new Cell(nr, nc);
                if (!inside(grid, next) || grid[nr][nc] != 0 || closed.contains(next)) continue;
                // Do not pass diagonally between two blocked orthogonal cells.
                if (d[0] != 0 && d[1] != 0 &&
                    (grid[cell.row() + d[0]][cell.col()] != 0 ||
                     grid[cell.row()][cell.col() + d[1]] != 0)) continue;

                double step = (d[0] == 0 || d[1] == 0) ? 1.0 : Math.sqrt(2.0);
                double candidate = scores.get(cell) + step;
                if (candidate < scores.getOrDefault(next, Double.POSITIVE_INFINITY)) {
                    scores.put(next, candidate);
                    previous.put(next, cell);
                    open.add(new Node(next, candidate, candidate + heuristic(next, goal)));
                }
            }
        }
        return List.of();
    }

    private boolean inside(int[][] grid, Cell c) {
        return c != null && c.row() >= 0 && c.row() < grid.length &&
            c.col() >= 0 && c.col() < grid[0].length;
    }

    private double heuristic(Cell a, Cell b) {
        int dx = Math.abs(a.row() - b.row()), dy = Math.abs(a.col() - b.col());
        return Math.max(dx, dy) + (Math.sqrt(2.0) - 1.0) * Math.min(dx, dy);
    }

    private List<Cell> reconstruct(Map<Cell, Cell> previous, Cell goal) {
        LinkedList<Cell> path = new LinkedList<>();
        Cell current = goal;
        path.addFirst(current);
        while (previous.containsKey(current)) {
            current = previous.get(current);
            path.addFirst(current);
        }
        return List.copyOf(path);
    }

    private void validateGrid(int[][] grid) {
        if (grid == null || grid.length == 0 || grid[0] == null || grid[0].length == 0)
            throw new IllegalArgumentException("Grid cannot be empty.");
        int width = grid[0].length;
        for (int[] row : grid) {
            if (row == null || row.length != width)
                throw new IllegalArgumentException("Grid must be rectangular.");
            for (int value : row)
                if (value != 0 && value != 1)
                    throw new IllegalArgumentException("Grid cells must be 0 (free) or 1 (blocked).");
        }
    }
}
