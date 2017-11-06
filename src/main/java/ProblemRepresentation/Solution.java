package ProblemRepresentation;

import GoogleMapsApi.GoogleStaticMap;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author renansantos
 */
public class Solution implements Cloneable{

    private long totalDistanceTraveled;
    private long totalTravelTime;
    private long totalTimeWindowAnticipation;
    private long totalTimeWindowDelay;
    private long evaluationFunction;
    private List<Route> routes;
    private Set<Request> nonAttendedRequests;
    private List<Integer> integerRepresentation;

    public Solution() {
        initializeAttributesWithEmptyLists();
        clearAttributeValues();
    }
    
    public Solution(Solution solution) {
        initializeAttributesWithEmptyLists();
        clearAttributeValues();
        this.totalDistanceTraveled = solution.getTotalDistanceTraveled();
        this.totalTravelTime = solution.getTotalTravelTime();
        this.totalTimeWindowAnticipation = solution.getTotalTimeWindowAnticipation();
        this.totalTimeWindowDelay = solution.getTotalTimeWindowDelay();
        this.evaluationFunction = solution.getEvaluationFunction();
        this.routes.clear();
        this.routes.addAll(solution.getRoutes());
        this.nonAttendedRequests = solution.getNonAttendedRequests();
        
    }

    private void initializeAttributesWithEmptyLists() {
        this.routes = new ArrayList<>();
        this.nonAttendedRequests = new HashSet<>();
        this.integerRepresentation = new ArrayList<>();
    }

    public Solution(long totalDistance, long totalTravelTime, long totalTimeWindowAnticipation, long totalTimeWindowDelay,
            long evaluationFunction, List<Route> routes, Set<Request> nonAttendedRequests) {
        this.totalDistanceTraveled = totalDistance;
        this.totalTravelTime = totalTravelTime;
        this.totalTimeWindowAnticipation = totalTimeWindowAnticipation;
        this.totalTimeWindowDelay = totalTimeWindowDelay;
        this.evaluationFunction = evaluationFunction;
        this.routes = routes;
        this.nonAttendedRequests = nonAttendedRequests;
    }

    public long getTotalDistanceTraveled() {
        return totalDistanceTraveled;
    }

    public long getTotalTravelTime() {
        return totalTravelTime;
    }

    public long getTotalTimeWindowAnticipation() {
        return totalTimeWindowAnticipation;
    }

    public long getTotalTimeWindowDelay() {
        return totalTimeWindowDelay;
    }

    public List<Route> getRoutes() {
        return routes;
    }
    
    public Route getRoute(int position){
        return routes.get(position);
    }
    
    public long getEvaluationFunction(){
        return this.evaluationFunction;
    }
    
    public void setRoute(int position, Route route){
        routes.get(position).setRoute(route);
    }
    
    public Set<Request> getNonAttendedRequests() {
        return nonAttendedRequests;
    }

    public List<Integer> getIntegerRepresentation() {
        return integerRepresentation;
    }

    public void addRoute(Route route) {
        this.routes.add(route);
    }

    public void calculateEvaluationFunction() {
        clearAttributeValues();
        sumAttibutesForEveryRoute();
        
        if (this.totalTimeWindowDelay > 0) {
            this.evaluationFunction = this.totalDistanceTraveled + this.totalTravelTime * this.totalTimeWindowDelay 
                    + this.totalTimeWindowAnticipation;
        }else{
            this.evaluationFunction = this.totalDistanceTraveled + this.totalTravelTime + this.totalTimeWindowAnticipation;
        }

    }

    private void sumAttibutesForEveryRoute() {
        this.totalDistanceTraveled = this.routes.stream().mapToLong(Route::getTotalRouteDistance).sum();
        this.totalTravelTime = this.routes.stream().mapToLong(Route::getRouteTravelTime).sum();
        this.totalTimeWindowAnticipation = this.routes.stream().mapToLong(Route::getTotalTimeWindowAnticipation).sum();
        this.totalTimeWindowDelay = this.routes.stream().mapToLong(Route::getTotalTimeWindowDelay).sum();
    }

    private void clearAttributeValues() {
        this.totalDistanceTraveled = 0;
        this.totalTravelTime = 0;
        this.totalTimeWindowAnticipation = 0;
        this.totalTimeWindowDelay = 0;
        this.evaluationFunction = 0;    
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
    
    public void printRoutes(){
        this.routes.forEach(r -> System.out.println(r.getEvaluationFunction() + "\t" + r.getIntegerSequenceOfAttendedRequests()));
    }
    
    public void printIntegerRepresentationOfRoutes(){
        this.routes.forEach(r -> System.out.println(r.getEvaluationFunction() + "\t" + r.getTotalTimeWindowAnticipation() + "\t" + r.getIntegerRouteRepresetation()));
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

        return "Solution - " + this.evaluationFunction + "\t"+ this.totalDistanceTraveled + "\t" + this.totalTravelTime + "\t"
                + this.totalTimeWindowAnticipation + "\t" + this.totalTimeWindowDelay;
    }

    
    public Object clone(){
        return new Solution(totalDistanceTraveled, totalTravelTime, totalTimeWindowAnticipation, 
                totalTimeWindowDelay, evaluationFunction,  routes, nonAttendedRequests);
    }
}
