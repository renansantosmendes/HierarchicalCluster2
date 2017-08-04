package VRPDRTSD;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author renansantos
 */
class Solution {
    private long totalDistance;
    private long totalTravelTime;
    private List<Route> routes;
    private Set<Request> nonAttendedRequests;
    
    public Solution(){
        this.routes = new ArrayList<>();
        this.nonAttendedRequests = new HashSet<>();
    }

    public Solution(long totalDistance, long totalTravelTime, List<Route> routes, Set<Request> nonAttendedRequests) {
        this.totalDistance = totalDistance;
        this.totalTravelTime = totalTravelTime;
        this.routes = routes;
        this.nonAttendedRequests = nonAttendedRequests;
    }
    
    
}
