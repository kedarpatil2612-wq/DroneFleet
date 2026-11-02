package com.dronefleet.service;

public class GridPathfinderSelfTest {
    public static void main(String[] args) {
        GridPathfinder planner = new GridPathfinder();
        int[][] open = new int[5][5];
        var path = planner.findPath(open, new GridPathfinder.Cell(0,0), new GridPathfinder.Cell(4,4));
        if (path.isEmpty() || !path.get(0).equals(new GridPathfinder.Cell(0,0)) ||
            !path.get(path.size()-1).equals(new GridPathfinder.Cell(4,4)))
            throw new AssertionError("A* should find a path on an open grid.");

        int[][] blocked = new int[3][3];
        blocked[0][1] = blocked[1][0] = blocked[1][1] = blocked[1][2] = blocked[2][1] = 1;
        var noPath = planner.findPath(blocked, new GridPathfinder.Cell(0,0), new GridPathfinder.Cell(2,2));
        if (!noPath.isEmpty()) throw new AssertionError("A* should report no path when the goal is enclosed.");

        System.out.println("GridPathfinder self-test: 2 checks passed.");
    }
}
