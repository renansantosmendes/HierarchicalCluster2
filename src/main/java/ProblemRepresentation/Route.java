package ProblemRepresentation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author renansantos - The Route Class represents a vehicle route for the problem
 */
public class Route {
    private long totalRouteDistance;
    private long routeTravelTime;
    private Set<Request> notServedRequests;
    private List<Node> nodesSequence;
    private List<Request> sequenceOfAttendedRequests;
    private List<Integer> integerRouteRepresetation;

    public Route(long totalRouteDistance, long routeTravelTime, Set<Request> notServedRequests, List<Node> nodesSequence, 
            List<Request> sequenceOfServedRequests) {
        this.totalRouteDistance = totalRouteDistance;
        this.routeTravelTime = routeTravelTime;
        this.notServedRequests = notServedRequests;
        this.nodesSequence = nodesSequence;
        this.sequenceOfAttendedRequests = sequenceOfServedRequests;
    }

    public Route() {
       this.notServedRequests = new HashSet<>();
       this.nodesSequence = new ArrayList<>();
       this.sequenceOfAttendedRequests = new ArrayList<>();
       this.integerRouteRepresetation = new ArrayList<>();
    }

    public double getTotalRouteDistance() {
        return totalRouteDistance;
    }
 
    public void setTotalRouteDistance(long totalRouteDistance) {
        this.totalRouteDistance = totalRouteDistance;
    }

    public long getRouteTravelTime() {
        return routeTravelTime;
    }
    
    public void setRouteTravelTime(int routeTravelTime) {
        this.routeTravelTime = routeTravelTime;
    }

    public Set<Request> getNotServedRequests() {
        return notServedRequests;
    }

    public void setNotServedRequests(Set<Request> notServedRequests) {
        this.notServedRequests = notServedRequests;
    }

    public List<Node> getNodesSequence() {
        return nodesSequence;
    }

    public void setNodesSequence(List<Node> nodesSequence) {
        this.nodesSequence = nodesSequence;
    }

    public List<Request> getSequenceOfAttendedRequests() {
        return sequenceOfAttendedRequests;
    }

    public void setSequenceOfAttendedRequests(List<Request> sequenceOfAttendedRequests) {
        this.sequenceOfAttendedRequests = sequenceOfAttendedRequests;
    }

    public List<Integer> getIntegerRouteRepresetation() {
        return integerRouteRepresetation;
    }

    public void setIntegerRouteRepresetation(List<Integer> integerRouteRepresetation) {
        this.integerRouteRepresetation = integerRouteRepresetation;
    }
    
    
    
    public void calculateTotalRouteDistance(){
        
    }
 
    public void calculateTravelTime(){
        
    }
    
    
    
}
