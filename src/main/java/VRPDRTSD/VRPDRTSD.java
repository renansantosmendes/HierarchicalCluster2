package VRPDRTSD;

import ProblemRepresentation.Solution;
import ProblemRepresentation.Request;
import ProblemRepresentation.ProblemData;
import ProblemRepresentation.Node;
import Algorithms.Algorithm;
import ProblemRepresentation.Route;
import ProblemRepresentation.Vehicle;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author renansantos
 */
public class VRPDRTSD implements Algorithm {

    private ProblemData data;
    private String instanceName;
    private String nodesInstanceName;
    private String adjacenciesInstanceName;
    private int numberOfVehicles;
    private int vehicleCapacity;
    double maxDistance;
    double minDistance;
    int minTimeWindowLower;
    int maxTimeWindowLower;
    int minTimeWindowUpper;
    int maxTimeWindowUpper;
    Map<Node, List<Request>> requestsThatBoardsInNode;
    Map<Node, List<Request>> requestsThatLeavesInNode;
    int maxLoadIndex;
    int minLoadIndex;
    Solution solution;
    List<Request> candidates = new ArrayList<>();
    List<Request> feasibleRequests = new ArrayList<>();
    Request candidate;
    Route currentRoute;

    public VRPDRTSD(String instanceName, String nodesInstanceName, String adjacenciesInstanceName,
            int numberOfVehicles, int vehicleCapacity) {
        this.instanceName = instanceName;
        this.nodesInstanceName = nodesInstanceName;
        this.adjacenciesInstanceName = adjacenciesInstanceName;
        this.numberOfVehicles = numberOfVehicles;
        this.vehicleCapacity = vehicleCapacity;
        this.readInstance();

    }

    public ProblemData getData() {
        return data;
    }

    public void setData(ProblemData data) {
        this.data = data;
    }

    public void originalRequestsFeasibilityAnalysis() {
        for (Request request : data.getRequests()) {
            request.determineInicialFeasibility(data.getCurrentTime(), data.getCurrentNode(), data.getDuration());
        }
    }

    public void requestsFeasibilityAnalysis() {
        data.setCurrentNode(data.getNodes().get(0));
        for (Request request : candidates) {
            request.determineInicialFeasibility(data.getCurrentTime(), data.getCurrentNode(), data.getDuration());
        }
    }

    public void requestsFeasibilityAnalysisInConstructionFase() {
        for (Request request : candidates) {
            request.determineFeasibilityInConstructionFase(data.getCurrentTime(), data.getLastPassengerAddedToRoute(),
                    data.getCurrentNode(), data.getDuration());
//            request.determineFeasibilityInConstructionFase(data.getCurrentTime(), data.getCurrentNode(), data.getDuration());
        }
        //System.out.println(candidates.stream().filter(Request::isFeasible).collect(Collectors.toCollection(ArrayList::new)));
    }

    public void setDistanceToAttendEveryRequest() {
        data.getRequests().forEach(r -> r.setDistanceToAttendThisRequest(data.getCurrentNode(), data.getDistance()));
    }

    public void findMaxAndMinDistance() {
        maxDistance = data.getRequests().stream()
                .mapToDouble(Request::getDistanceToAttendThisRequest)
                .max().getAsDouble();
        minDistance = data.getRequests().stream()
                .mapToDouble(Request::getDistanceToAttendThisRequest)
                .min().getAsDouble();
    }

    public void findMaxAndMinTimeWindowLower() {
        minTimeWindowLower = data.getRequests().stream()
                .mapToInt(Request::getDeliveryTimeWindowLowerInMinutes)
                .min().getAsInt();
        maxTimeWindowLower = data.getRequests().stream()
                .mapToInt(Request::getDeliveryTimeWindowLowerInMinutes)
                .max().getAsInt();
    }

    public void findMaxAndMinTimeWindowUpper() {
        minTimeWindowUpper = data.getRequests().stream()
                .mapToInt(Request::getDeliveryTimeWindowUpperInMinutes)
                .min().getAsInt();
        maxTimeWindowUpper = data.getRequests().stream()
                .mapToInt(Request::getDeliveryTimeWindowUpperInMinutes)
                .max().getAsInt();
    }

    public void separateRequestsWhichBoardsAndLeavesInNodes() {
        requestsThatBoardsInNode = data.getRequests().stream()
                .collect(Collectors.groupingBy(Request::getOrigin));
        requestsThatLeavesInNode = data.getRequests().stream()
                .collect(Collectors.groupingBy(Request::getDestination));
    }

    public void setLoadIndexForEveryNode() {
        data.getNodes().forEach(n -> n.setLoadIndex(requestsThatBoardsInNode, requestsThatLeavesInNode));
    }

    public void findMaxAndMinLoadIndex() {
        maxLoadIndex = data.getNodes().stream()
                .mapToInt(Node::getLoadIndex)
                .max().getAsInt();
        minLoadIndex = data.getNodes().stream()
                .mapToInt(Node::getLoadIndex)
                .min().getAsInt();
    }

    public ArrayList<Request> getListOfFeasibleRequests() {
        return data.getRequests().stream().filter(Request::isFeasible).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void readInstance() {
        data = new ProblemData(instanceName, nodesInstanceName, adjacenciesInstanceName, numberOfVehicles, vehicleCapacity);
    }

    @Override
    public void buildGreedySolution() {
        initializeSolution();
        initializeCandidatesSet();
        while (stoppingCriterionIsFalse()) {
            startNewRoute();
            requestsFeasibilityAnalysis();
            while (hasFeasibleRequests()) {
                findBestCandidateUsingRRF();
                addCandidateIntoRoute();
                actualizeRequestsData();
                findOtherRequestsThatCanBeAttended();
                requestsFeasibilityAnalysisInConstructionFase();
            }
            finalizeRoute();
            addRouteInSolution();
            List<Integer> list = currentRoute.getIntegerRouteRepresetation();
            //list.sort(Comparator.naturalOrder());
            System.out.println(list);
        }
        //finalizeSolution();
    }

    public void initializeSolution() {
        solution = new Solution();
    }

    public void initializeCandidatesSet() {
        originalRequestsFeasibilityAnalysis();
        prepareAndSetRequestsData();
        setRequestFeasibilityParameters();
        initializeCandidates();
    }

    public void prepareAndSetRequestsData() {
        setDistanceToAttendEveryRequest();
        findMaxAndMinDistance();
        findMaxAndMinTimeWindowLower();
        findMaxAndMinTimeWindowUpper();
        separateRequestsWhichBoardsAndLeavesInNodes();
        setLoadIndexForEveryNode();
        findMaxAndMinLoadIndex();
    }

    public void setRequestFeasibilityParameters() {
        for (Request request : data.getRequests()) {
            request.setDistanceRankingFunction(maxDistance, minDistance);
            request.setDeliveryTimeWindowLowerRankingFunction(maxTimeWindowLower, minTimeWindowLower);
            request.setDeliveryTimeWindowUpperRankingFunction(maxTimeWindowUpper, minTimeWindowUpper);
            request.setOriginNodeRankingFunction(maxLoadIndex, minLoadIndex);
            request.setDestinationNodeRankingFunction(maxLoadIndex, minLoadIndex);
            request.setRequestRankingFunction(0.1, 0.5, 0.1, 0.1, 0.1);
        }
    }

    public void initializeCandidates() {
        data.getRequests().sort(Comparator.comparing(Request::getRequestRankingFunction).reversed());
        candidates.addAll(data.getRequests());
    }

    public void findBestCandidateUsingRRF() {
//        candidates.sort(Comparator.comparing(Request::getRequestRankingFunction).reversed());
//        candidate = candidates.get(0);
        candidates.sort(Comparator.comparing(Request::getRequestRankingFunction).reversed());
        List<Request> feasibleCandidates = new ArrayList<>();
        feasibleCandidates.addAll(candidates.stream().filter(Request::isFeasible).collect(Collectors.toCollection(ArrayList::new)));
        candidate = feasibleCandidates.get(0);
    }

    public void addCandidateIntoRoute() {
                
        if(currentRoute.getIntegerRouteRepresetation().size() == 0){
            data.setCurrentTime(candidate.getDeliveryTimeWindowLower());
        } else {
            Duration displacementTime = data.getDuration()[data.getLastPassengerAddedToRoute().getDestination().getId()]
                    [candidate.getDestination().getId()];
            data.setCurrentTime(data.getCurrentTime().plus(displacementTime));
        }
        
        int indexOfCandidate = candidates.indexOf(candidate);
        data.setLastPassengerAddedToRoute(candidates.get(indexOfCandidate));
        data.setCurrentNode(data.getLastPassengerAddedToRoute().getDestination());
        
        candidate.setDeliveryTime(data.getCurrentTime());
        currentRoute.addValueInIntegerRepresentation(candidate.getId());
        
        scheduleDeliveryTimeInRouteRepresentation();
    }

    public void actualizeRequestsData() {
        candidates.remove(candidate);
        requestsThatLeavesInNode.get(data.getCurrentNode()).remove(candidate);
    }

    public boolean stoppingCriterionIsFalse() {
        return !candidates.isEmpty() && !data.getAvaibleVehicles().isEmpty();
    }

    public void startNewRoute() {
        currentRoute = new Route();
        data.setCurrentVehicle(new Vehicle(data.getAvaibleVehicles().get(0)));
        data.setCurrentNode(data.getNodes().get(0));
        data.setCurrentTime(LocalDateTime.of(2017, 1, 1, 0, 0, 0));
    }

    public boolean hasFeasibleRequests() {
        return candidates.stream()
                .filter(r -> r.isFeasible())
                .collect(Collectors.toCollection(ArrayList::new)).size() != 0;
    }

    public void scheduleDeliveryTimeInRouteRepresentation() {
        //currentRoute.addValueInIntegerRepresentation(-1 * candidate.getDeliveryTimeWindowLowerInMinutes());
        currentRoute.addValueInIntegerRepresentation(-1 * candidate.getDeliveryTimeInMinutes());
    }

    public void scheduleDeliveryTime(Request request) {
        currentRoute.addValueInIntegerRepresentation(-1 * request.getDeliveryTimeWindowLowerInMinutes());
    }

    public void schedulePickUpTime() {

    }

    public void addRouteInSolution() {

    }

    public void finalizeRoute() {
        schedulePickUpTime();
        //currentRoute.buildNodesSequence(data);
        //currentRoute.buildSequenceOfAttendedRequests(data);
    }

    public void findOtherRequestsThatCanBeAttended() {
        List<Request> otherRequestsToAdd = new ArrayList<>();
        for (Request request : requestsThatLeavesInNode.get(data.getCurrentNode())) {
            if (currentTimeIsWithInDeliveryTimeWindow(request)) {
                otherRequestsToAdd.add(request);
            }
        }

        otherRequestsToAdd.sort(Comparator.comparing(Request::getDeliveryTimeWindowLower));

        while (otherRequestsToAdd.size() != 0) {
            currentRoute.addValueInIntegerRepresentation(otherRequestsToAdd.get(0).getId());
            data.setCurrentTime(otherRequestsToAdd.get(0).getDeliveryTimeWindowLower());
            data.setLastPassengerAddedToRoute(otherRequestsToAdd.get(0));
            candidates.remove(otherRequestsToAdd.get(0));
            scheduleDeliveryTime(otherRequestsToAdd.get(0));
            otherRequestsToAdd.remove(0);
        }

    }

    private boolean currentTimeIsWithInDeliveryTimeWindow(Request request) {
        return (data.getCurrentTime().isAfter(request.getDeliveryTimeWindowLower())
                || data.getCurrentTime().isEqual(request.getDeliveryTimeWindowLower()))
                && (data.getCurrentTime().isBefore(request.getDeliveryTimeWindowUpper())
                || data.getCurrentTime().isEqual(request.getDeliveryTimeWindowUpper()));
    }

}
