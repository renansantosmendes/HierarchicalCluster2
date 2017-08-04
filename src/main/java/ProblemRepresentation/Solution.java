package ProblemRepresentation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author renansantos
 */
public class Solution {
    private long totalDistance;
    private long totalTravelTime;
    private List<Route> routes;
    private Set<Request> nonAttendedRequests;
    private List<Integer> integerRepresentation;
    
    public Solution(){
        this.routes = new ArrayList<>();
        this.nonAttendedRequests = new HashSet<>();
        this.integerRepresentation = new ArrayList<>();
    }

    public Solution(long totalDistance, long totalTravelTime, List<Route> routes, Set<Request> nonAttendedRequests) {
        this.totalDistance = totalDistance;
        this.totalTravelTime = totalTravelTime;
        this.routes = routes;
        this.nonAttendedRequests = nonAttendedRequests;
    }
    
    
}
