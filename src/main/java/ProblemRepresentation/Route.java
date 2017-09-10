package ProblemRepresentation;

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

    public void addValueInIntegerRepresentation(int value) {
        this.integerRouteRepresetation.add(value);
    }

    public void calculateTotalRouteDistance() {

    }

    public void calculateTravelTime() {

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
            Request request = data.getRequests().stream().filter(u -> u.getId() == id).findAny().get();
            this.sequenceOfAttendedRequests.add(request);
        }
    }

    public void buildNodesSequence(ProblemData data) {
        List<Integer> idSequence = new ArrayList<>();
        idSequence = this.integerRouteRepresetation.stream().filter(u -> u.longValue() > 0)
                .collect(Collectors.toCollection(ArrayList::new));

        if (this.nodesSequence == null) {
            this.nodesSequence = new ArrayList<>();
        }
        this.nodesSequence.clear();

        Map<Integer, Integer> occurrenceInSequence = new HashMap<>();

        for (int i = 0; i < idSequence.size(); i++) {
            occurrenceInSequence.put(idSequence.get(i), 0);
        }
        
        for (int i = 0; i < idSequence.size(); i++) {
            Integer id = idSequence.get(i);
            Request request = data.getRequests().stream().filter(u -> u.getId() == id).findAny().get();
            if(idSequence.get(id) == 0){
                this.nodesSequence.add(data.getNodes().stream().filter(u -> u.getId() == 0).findAny().get());
            } else if(occurrenceInSequence.get(id) == 0 ){
                this.nodesSequence.add(request.getOrigin());
                occurrenceInSequence.replace(id, 1);
            } else if(occurrenceInSequence.get(id) == 1 ){
                this.nodesSequence.add(request.getDestination());
            }
        }
        
        System.out.println("idSequence"+idSequence);
        //this.nodesSequence.add(data.getNodes().stream().filter(u -> u.getId() == 0).findAny().get());
        //int positionCounter = 0;
//        for (Integer id : idSequence) {
//            Request request = data.getRequests().stream().filter(u -> u.getId() == id).findAny().get();
//            if (occurrenceInSequence.get(id) == 0) {
//                if (idSequence.get(positionCounter) != request.getPassengerOrigin().getId()) {
//                    this.nodesSequence.add(request.getPassengerOrigin());
//                }
//                occurrenceInSequence.replace(id, 1);
//                positionCounter++;
//            } else if (occurrenceInSequence.get(id) == 1) {
//                if (idSequence.get(positionCounter) != request.getPassengerDestination().getId()) {
//                    this.nodesSequence.add(request.getPassengerDestination());
//                }
//                positionCounter++;
//            }
//        }

//        for (int i = 0; i < idSequence.size(); i++) {
//            int id = idSequence.get(i);
//            Request request = data.getRequests().stream().filter(u -> u.getId() == id).findAny().get();
//            if (occurrenceInSequence.get(id) == 0) {
//                if (idSequence.get(i) != request.getPassengerOrigin().getId()) {
//                    this.nodesSequence.add(request.getPassengerOrigin());
//                }
//                occurrenceInSequence.replace(id, 1);
//                positionCounter++;
//            } else if (occurrenceInSequence.get(id) == 1) {
//                if (idSequence.get(positionCounter) != request.getPassengerDestination().getId()) {
//                    this.nodesSequence.add(request.getPassengerDestination());
//                }
//                positionCounter++;
//            }
//        }
//        this.nodesSequence.add(data.getNodes().stream().filter(u -> u.getId() == 0).findAny().get());

    }

}
