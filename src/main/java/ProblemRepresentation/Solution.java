package ProblemRepresentation;

import GoogleMapsApi.GoogleStaticMap;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author renansantos
 */
public class Solution implements Cloneable {

    private long totalDistanceTraveled;
    private long totalTravelTime;
    private long totalTimeWindowAnticipation;
    private long totalTimeWindowDelay;
    private long evaluationFunction;
    private long numberOfVehicles;
    private List<Route> routes;
    private Set<Request> nonAttendedRequests;
    private List<Integer> integerRepresentation = new ArrayList<>();

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
        this.numberOfVehicles = solution.getNumberOfVehicles();
        this.routes.clear();
        this.routes.addAll(solution.getRoutes());
        this.nonAttendedRequests = solution.getNonAttendedRequests();
        this.integerRepresentation.addAll(solution.getIntegerRepresentation());
    }

    private void initializeAttributesWithEmptyLists() {
        this.routes = new ArrayList<>();
        this.nonAttendedRequests = new HashSet<>();
        this.integerRepresentation = new ArrayList<>();
    }

    public Solution(long totalDistance, long totalTravelTime, long totalTimeWindowAnticipation, long totalTimeWindowDelay,
            long numberOfVehicles, long evaluationFunction, List<Route> routes, Set<Request> nonAttendedRequests,
            List<Integer> integerRepresentation) {
        this.totalDistanceTraveled = totalDistance;
        this.totalTravelTime = totalTravelTime;
        this.totalTimeWindowAnticipation = totalTimeWindowAnticipation;
        this.totalTimeWindowDelay = totalTimeWindowDelay;
        this.numberOfVehicles = numberOfVehicles;
        this.evaluationFunction = evaluationFunction;
        this.routes = routes;
        this.nonAttendedRequests = nonAttendedRequests;
        this.integerRepresentation = integerRepresentation;
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

    public Route getRoute(int position) {
        return routes.get(position);
    }

    public Route getRandomRoute() {
        int position = new Random().nextInt(this.routes.size());
        return routes.get(position);
    }

    public int getRandomRoutePosition() {
        int position = new Random().nextInt(this.routes.size());
        return position;
    }

    public void removeSequenceFromAllSolution(List<Integer> idSequence, int indexOfAlteratedRoute, ProblemData data) {
        int currentRouteIndex = 0;
        for (Integer id : idSequence) {
            if (currentRouteIndex != indexOfAlteratedRoute) {
                for (Route route : routes) {
                    route.removeRequest(id, data);
                }
                calculateEvaluationFunction(data);
            }
            currentRouteIndex++;
        }
        this.removeEmptyRoutes();
    }

    public long getEvaluationFunction() {
        return this.evaluationFunction;
    }

    public long getNumberOfVehicles() {
        return numberOfVehicles;
    }

    public void setRoute(int position, Route route) {
        routes.get(position).setRoute(route);
    }

    public void setSolution(Solution solution1) {
        Solution solution = new Solution((Solution) solution1.clone());
        initializeAttributesWithEmptyLists();
        clearAttributeValues();
        this.totalDistanceTraveled = solution.getTotalDistanceTraveled();
        this.totalTravelTime = solution.getTotalTravelTime();
        this.totalTimeWindowAnticipation = solution.getTotalTimeWindowAnticipation();
        this.totalTimeWindowDelay = solution.getTotalTimeWindowDelay();
        this.evaluationFunction = solution.getEvaluationFunction();
        this.numberOfVehicles = solution.getNumberOfVehicles();
        this.routes.clear();
        this.routes.addAll(solution.getRoutes());
        this.nonAttendedRequests = solution.getNonAttendedRequests();
        this.integerRepresentation.addAll(solution.getIntegerRepresentation());
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

    public void calculateEvaluationFunction(ProblemData data) {
        clearAttributeValues();
        sumAttibutesForEveryRoute();
        buildIntegerRepresentation();
        analyseCapacityConstraint(data);
        evaluateSolution();
        penalizeSolution();
    }

    private void evaluateSolution() {
        if (this.totalTimeWindowDelay > 0) {
            this.evaluationFunction = this.totalDistanceTraveled + this.totalTravelTime * this.totalTimeWindowDelay
                    + this.totalTimeWindowAnticipation * this.numberOfVehicles;//+ 50* this.numberOfVehicles
        } else {
            this.evaluationFunction = this.totalDistanceTraveled + this.totalTravelTime + this.totalTimeWindowAnticipation * this.numberOfVehicles;//+ 50* this.numberOfVehicles
        }
    }

    private void penalizeSolution() {
        if (hasRouteWithViolatedConstraint()) {
            this.evaluationFunction = this.totalDistanceTraveled * this.totalTravelTime * this.totalTravelTime;
        }
    }

    private void analyseCapacityConstraint(ProblemData data) {
        for (Route route : routes) {
            route.capacityAnalysis(data);
        }
    }

    private boolean hasRouteWithViolatedConstraint() {
        return routes.stream().filter(Route::isPenalized).collect(Collectors.toCollection(ArrayList::new)).size() > 0;
    }

    public void buildIntegerRepresentation() {
        this.integerRepresentation.clear();
        for (Route route : this.routes) {
            this.integerRepresentation.addAll(route.getIntegerRouteRepresetation());
        }
    }

    private void clearAttributeValues() {
        this.totalDistanceTraveled = 0;
        this.totalTravelTime = 0;
        this.totalTimeWindowAnticipation = 0;
        this.totalTimeWindowDelay = 0;
        this.evaluationFunction = 0;
        this.numberOfVehicles = 0;
    }

    private void sumAttibutesForEveryRoute() {
        this.totalDistanceTraveled = this.routes.stream().mapToLong(Route::getTotalRouteDistance).sum();
        this.totalTravelTime = this.routes.stream().mapToLong(Route::getRouteTravelTime).sum();
        this.totalTimeWindowAnticipation = this.routes.stream().mapToLong(Route::getTotalTimeWindowAnticipation).sum();
        this.totalTimeWindowDelay = this.routes.stream().mapToLong(Route::getTotalTimeWindowDelay).sum();

        List<Route> test = new ArrayList<>();
        for (Route route : routes) {
            if (route.getIntegerRouteRepresetation().size() != 0) {
                test.add(route);
            }
        }
        this.numberOfVehicles = test.size();
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

    public int getNumberOfRoutes() {
        return this.routes.size();
    }

    public void getStaticMapWithAllRoutes(List<Node> nodesList, String adjacenciesTable, String nodesTable) throws IOException {
        new GoogleStaticMap(nodesList, this.getRoutesForMap(), adjacenciesTable, nodesTable);
    }

    public void getStaticMapForEveryRoute(List<Node> nodesList, String adjacenciesTable, String nodesTable) throws IOException {
        for (List<Integer> route : this.getRoutesListForMap()) {
            new GoogleStaticMap(nodesList, route, adjacenciesTable, nodesTable);
        }
    }

    public void printRoutes() {
        this.routes.forEach(r -> System.out.println(r.getEvaluationFunction() + "\t" + r.getIntegerSequenceOfAttendedRequests()));
    }

    public void printIntegerRepresentationOfRoutes() {
        this.routes.forEach(r -> System.out.println(r.getEvaluationFunction() + "\t" + r.getTotalTimeWindowAnticipation() + "\t" + r.getIntegerRouteRepresetation()));
    }

    public void printAllInformations() {
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
        System.out.println("Solution - " + this.evaluationFunction + "\t" + this.totalDistanceTraveled + "\t"
                + this.totalTravelTime + "\t" + this.totalTimeWindowAnticipation + "\t" + this.totalTimeWindowDelay + "\t"
                + this.routes.size());
        System.out.println(nodesSequence);
        System.out.println(integerRepresentation);
        System.out.println(idSequence);
        System.out.println(this.integerRepresentation);
        System.out.println(this.integerRepresentation.stream().filter(u -> u.intValue() >= 0)
                .collect(Collectors.toCollection(ArrayList::new)));
    }

    public void removeEmptyRoutes() {
        List<Route> newRoutes = this.routes.stream()
                .filter(r -> r.isEmpty() != true)
                .collect(Collectors.toCollection(ArrayList::new));

        if (newRoutes.size() == 0) {
            System.out.println("has no routes problem!!!");
        }
        this.routes.clear();
        this.routes = newRoutes;
    }

    @Override
    public String toString() {
        return "Solution - " + this.evaluationFunction + "\t" + this.totalDistanceTraveled + "\t" + this.totalTravelTime + "\t"
                + this.totalTimeWindowAnticipation + "\t" + this.totalTimeWindowDelay + "\t" + this.numberOfVehicles;
    }

    public Object clone() {
        List<Route> routesClone = new ArrayList<>();
        for (Route route : routes) {
            routesClone.add((Route) route.clone());
        }
        return new Solution(totalDistanceTraveled, totalTravelTime, totalTimeWindowAnticipation,
                totalTimeWindowDelay, numberOfVehicles, evaluationFunction, routesClone,
                nonAttendedRequests, integerRepresentation);
    }
}
