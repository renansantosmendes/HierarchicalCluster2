package ProblemRepresentation;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author renansantos - The Route Class represents a vehicle route for the
 * problem
 */
public class Route {

    private long totalDistanceTraveled;
    private long routeTravelTime;
    private long totalTimeWindowViolation;
    private Set<Request> notServedRequests;
    private List<Node> nodesSequence;
    private List<Request> sequenceOfAttendedRequests;
    private List<Integer> integerRouteRepresetation;

    public Route(long totalRouteDistance, long routeTravelTime, long totalTimeWindowViolation, Set<Request> notServedRequests,
            List<Node> nodesSequence, List<Request> sequenceOfServedRequests) {
        this.totalDistanceTraveled = totalRouteDistance;
        this.routeTravelTime = routeTravelTime;
        this.totalTimeWindowViolation = totalTimeWindowViolation;
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
        return totalDistanceTraveled;
    }

    public void setTotalDistanceTraveled(long totalDistanceTraveled) {
        this.totalDistanceTraveled = totalDistanceTraveled;
    }

    public long getRouteTravelTime() {
        return routeTravelTime;
    }

    public void setRouteTravelTime(int routeTravelTime) {
        this.routeTravelTime = routeTravelTime;
    }

    public long getTotalTimeWindowViolation() {
        return totalTimeWindowViolation;
    }

    public void setTotalTimeWindowViolation(long totalTimeWindowViolation) {
        this.totalTimeWindowViolation = totalTimeWindowViolation;
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

    public void addValueInIntegerRepresentation(int value) {
        this.integerRouteRepresetation.add(value);
    }

    public void buildSequenceOfAttendedRequests(ProblemData data) {
        List<Integer> idSequence = new ArrayList<>();
        idSequence = this.integerRouteRepresetation.stream().filter(u -> u.longValue() > 0)
                .collect(Collectors.toCollection(ArrayList::new));

        if (this.sequenceOfAttendedRequests == null) {
            this.sequenceOfAttendedRequests = new ArrayList<>();
        }
        this.sequenceOfAttendedRequests.clear();

        for (Integer id : idSequence) {
            Request request = data.getRequests().stream().filter(u -> u.getId().equals(id)).findAny().get();
            this.sequenceOfAttendedRequests.add(request);
        }

        
    }

    public void buildNodesSequence(ProblemData data) {
        List<Integer> idSequence = new ArrayList<>();
        idSequence = this.integerRouteRepresetation.stream().filter(u -> u.longValue() >= 0)
                .collect(Collectors.toCollection(ArrayList::new));

        if (this.nodesSequence == null) {
            this.nodesSequence = new ArrayList<>();
        }
        this.nodesSequence.clear();

        Set<Integer> idCrossed = new HashSet<>();
        int currentPosition = 0;
        for (int i = 0; i < idSequence.size(); i++) {
            Integer id = idSequence.get(i);
            if (!id.equals(0)) {
                Request passenger = null;
                passenger = findRequestWithIdentification(data, id, passenger);

                if (idCrossed.contains(id)) {
                    if (!this.nodesSequence.get(currentPosition - 1).getId().equals(passenger.getDestination().getId())) {
                        this.nodesSequence.add(passenger.getDestination());
                        currentPosition++;
                    }

                } else if (!this.nodesSequence.get(currentPosition - 1).getId().equals(passenger.getOrigin().getId())) {
                    this.nodesSequence.add(passenger.getOrigin());
                    currentPosition++;
                }
                idCrossed.add(id);
            } else {
                this.nodesSequence.add(data.getNodes().get(0));
                currentPosition++;
            }
        }

        this.nodesSequence.forEach(n -> System.out.print(n.getId() + " "));
        System.out.println();
    }

    private Request findRequestWithIdentification(ProblemData data, Integer id, Request passenger) {
        for (Request request : data.getRequests()) {
            if (request.getId().equals(id)) {
                passenger = request;
            }
        }
        return passenger;
    }

    public void calculateTravelTime(ProblemData data) {
        long totalTravelTime = 0;
        for (int i = 0; i < this.nodesSequence.size() - 2; i++) {
            totalTravelTime += data.getDuration()[this.nodesSequence.get(i).getId()][this.nodesSequence.get(i + 1).getId()].getSeconds();
        }

        this.routeTravelTime = totalTravelTime;
    }

    public void calculateDistanceTraveled(ProblemData data) {
        long totalDistance = 0;
        for (int i = 0; i < this.nodesSequence.size() - 2; i++) {
            totalDistance += data.getDistance()[this.nodesSequence.get(i).getId()][this.nodesSequence.get(i + 1).getId()];
        }

        this.totalDistanceTraveled = totalDistance;
    }

    public void calculateTotalViolationOfTheDeliveryTimeWindow() {
        Duration violations = Duration.ofMinutes(0);
        Set<Request> attendedRequests = new HashSet<>();
        for (Request request : this.sequenceOfAttendedRequests) {
            attendedRequests.add(request);
        }

        for (Request request : attendedRequests) {
            if (request.getDeliveryTimeWindowLower().isAfter(request.getDeliveryTime())) {
                Duration time = Duration.between(request.getDeliveryTime(), request.getDeliveryTimeWindowLower());
                //System.out.println("violation for request = " + time);
                violations = violations.plus(time);
            }
        }
        
        this.totalTimeWindowViolation = violations.getSeconds()/60;
        //System.out.println(this.totalTimeWindowViolation/60);
    }

    
    public List<Integer> getNodesVisitationInIntegerRepresentation(){
        List<Integer> nodesSequence = new ArrayList<>();
        
        for(Node node: this.nodesSequence){
            nodesSequence.add(node.getId());
        }
        return nodesSequence;
    }
    
    
    public void clearIntegerRepresentation(){
        this.integerRouteRepresetation.clear();
    }
    
    @Override
    public String toString() {
        return "Route - Total Distance = " + this.totalDistanceTraveled + "m - Travel Time = " + this.routeTravelTime +
                "s - Total DTW Violated  = " + this.totalTimeWindowViolation + " min";
    }

}
