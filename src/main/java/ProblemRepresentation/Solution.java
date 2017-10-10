package ProblemRepresentation;

import GoogleMapsApi.GoogleStaticMap;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    public Solution() {
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

    public long getTotalDistanceTraveled() {
        return totalDistanceTraveled;
    }

    public long getTotalTravelTime() {
        return totalTravelTime;
    }

    public long getTotalTimeWindowViolation() {
        return totalTimeWindowViolation;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public Set<Request> getNonAttendedRequests() {
        return nonAttendedRequests;
    }

    public List<Integer> getIntegerRepresentation() {
        return integerRepresentation;
    }

    public void addRoute(Route route) {
        this.routes.add(route);
        this.totalDistanceTraveled += route.getTotalRouteDistance();
        this.totalTravelTime += route.getRouteTravelTime();
        this.totalTimeWindowViolation += route.getTotalTimeWindowViolation();
    }

    public Set<List<Integer>> getRoutesForMap() {
        Set<List<Integer>> routes = new HashSet<>();
        for (Route route : this.routes) {
            routes.add(route.getNodesVisitationInIntegerRepresentation());
        }
        return routes;
    }

    public List<List<Integer>> getRoutesListForMap() {
        List<List<Integer>> routes = new ArrayList<>();
        for (Route route : this.routes) {
            routes.add(new ArrayList<>(route.getNodesVisitationInIntegerRepresentation()));
        }
        return routes;
    }

    public void getStaticMapWithAllRoutes(List<Node> nodesList, String adjacenciesTable, String nodesTable) throws IOException {
        new GoogleStaticMap(nodesList, this.getRoutesForMap(), adjacenciesTable, nodesTable);
    }

    public void getStaticMapForEveryRoute(List<Node> nodesList, String adjacenciesTable, String nodesTable) throws IOException {
        for (List<Integer> route : this.getRoutesListForMap()) {
            new GoogleStaticMap(nodesList, route, adjacenciesTable, nodesTable);
        }
    }

    @Override
    public String toString() {
        StringBuilder nodesSequence = new StringBuilder();
        StringBuilder integerRepresentation = new StringBuilder();
        StringBuilder idSequence = new StringBuilder();

        for (Route route : this.routes) {
            idSequence.append("Id = ")
                    .append(route.getIntegerRouteRepresetation().stream().filter(r -> r >= 0)
                            .collect(Collectors.toCollection(ArrayList::new)))
                    .append("\n");
            integerRepresentation.append("InR = ")
                    .append(route.getIntegerRouteRepresetation())
                    .append("\n");
            nodesSequence.append("Nodes = ")
                    .append(route.getNodesVisitationInIntegerRepresentation())
                    .append("\n");

        }

        return "Solution - " + this.totalDistanceTraveled + "\t" + this.totalTravelTime + "\t" + this.totalTimeWindowViolation + "\t"
                + this.routes.size() + "\n" + idSequence + integerRepresentation + nodesSequence;
    }

}
