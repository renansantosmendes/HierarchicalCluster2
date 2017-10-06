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
    private long totalDistanceTraveled;
    private long totalTravelTime;
    private long totalTimeWindowViolation;
    private List<Route> routes;
    private Set<Request> nonAttendedRequests;
    private List<Integer> integerRepresentation;
    
    public Solution(){
        this.routes = new ArrayList<>();
        this.nonAttendedRequests = new HashSet<>();
        this.integerRepresentation = new ArrayList<>();
        this.totalDistanceTraveled = 0;
        this.totalTravelTime = 0;
        this.totalTimeWindowViolation = 0;
    }

    public Solution(long totalDistance, long totalTravelTime, long totalTimeWindowViolation, List<Route> routes, 
            Set<Request> nonAttendedRequests) {
        this.totalDistanceTraveled = totalDistance;
        this.totalTravelTime = totalTravelTime;
        this.totalTimeWindowViolation = totalTimeWindowViolation;
        this.routes = routes;
        this.nonAttendedRequests = nonAttendedRequests;
    }
    
    public void addRoute(Route route){
        this.routes.add(route);
        this.totalDistanceTraveled += route.getTotalRouteDistance();
        this.totalTravelTime += route.getRouteTravelTime();
        this.totalTimeWindowViolation += route.getTotalTimeWindowViolation();
    }
    
    @Override
    public String toString(){
        return "Solution - " + this.totalDistanceTraveled + "\t" + this.totalTravelTime + "\t" + this.totalTimeWindowViolation;
    }
    
}
