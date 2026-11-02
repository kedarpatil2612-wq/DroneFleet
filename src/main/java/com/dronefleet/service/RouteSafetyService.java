package com.dronefleet.service;

public class RouteSafetyService {
    // Haversine distance in metres. This is a simple 2D demo check, not certified aviation separation.
    public double distanceMetres(double lat1,double lon1,double lat2,double lon2) {
        double earth=6_371_000.0;
        double p1=Math.toRadians(lat1), p2=Math.toRadians(lat2);
        double dp=Math.toRadians(lat2-lat1), dl=Math.toRadians(lon2-lon1);
        double a=Math.sin(dp/2)*Math.sin(dp/2)+Math.cos(p1)*Math.cos(p2)*Math.sin(dl/2)*Math.sin(dl/2);
        return 2*earth*Math.atan2(Math.sqrt(a),Math.sqrt(1-a));
    }
    public boolean isPotentialConflict(double lat1,double lon1,double lat2,double lon2,double thresholdMetres) {
        return distanceMetres(lat1,lon1,lat2,lon2)<thresholdMetres;
    }
}
