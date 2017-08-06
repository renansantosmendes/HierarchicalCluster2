package VRPDRTSD;

import ProblemRepresentation.Solution;
import ProblemRepresentation.Request;
import ProblemRepresentation.ProblemData;
import ProblemRepresentation.Node;
import Algorithms.Algorithm;
import ProblemRepresentation.Route;
import java.time.Duration;
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
    Map<Node, List<Request>> requestsWichBoardsInNode;
    Map<Node, List<Request>> requestsWichLeavesInNode;
    int maxLoadIndex;
    int minLoadIndex;
    Solution solution;
    List<Request> candidates = new ArrayList<>();
    List<Request> feasibleRequests = new ArrayList<>();
    Request candidate;
    Route actualRoute;

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

    private void requestsFeasibilityAnalysis() {
        for (Request request : data.getRequests()) {
            request.determineFeasibility(data.getCurrentTime(), data.getCurrentNode(), data.getDuration());
        }
    }

    private void setDistanceToAttendEveryRequest() {
        data.getRequests().forEach(r -> r.setDistanceToAttendThisRequest(data.getCurrentNode(), data.getDistance()));
    }

    private void findMaxAndMinDistance() {
        maxDistance = data.getRequests().stream()
                .mapToDouble(Request::getDistanceToAttendThisRequest)
                .max().getAsDouble();
        minDistance = data.getRequests().stream()
                .mapToDouble(Request::getDistanceToAttendThisRequest)
                .min().getAsDouble();
    }

    private void findMaxAndMinTimeWindowLower() {
        minTimeWindowLower = data.getRequests().stream()
                .mapToInt(Request::getDeliveryTimeWindowLowerInMinutes)
                .min().getAsInt();
        maxTimeWindowLower = data.getRequests().stream()
                .mapToInt(Request::getDeliveryTimeWindowLowerInMinutes)
                .max().getAsInt();
    }

    private void findMaxAndMinTimeWindowUpper() {
        minTimeWindowUpper = data.getRequests().stream()
                .mapToInt(Request::getDeliveryTimeWindowUpperInMinutes)
                .min().getAsInt();
        maxTimeWindowUpper = data.getRequests().stream()
                .mapToInt(Request::getDeliveryTimeWindowUpperInMinutes)
                .max().getAsInt();
    }

    private void separateRequestsWhichBoardsAndLeavesInNodes() {
        requestsWichBoardsInNode = data.getRequests().stream()
                .collect(Collectors.groupingBy(Request::getPassengerOrigin));
        requestsWichLeavesInNode = data.getRequests().stream()
                .collect(Collectors.groupingBy(Request::getPassengerDestination));
    }

    private void setLoadIndexForEveryNode() {
        data.getNodes().forEach(n -> n.setLoadIndex(requestsWichBoardsInNode, requestsWichLeavesInNode));
    }

    private void findMaxAndMinLoadIndex() {
        maxLoadIndex = data.getNodes().stream()
                .mapToInt(Node::getLoadIndex)
                .max().getAsInt();
        minLoadIndex = data.getNodes().stream()
                .mapToInt(Node::getLoadIndex)
                .min().getAsInt();
    }

    private ArrayList<Request> getListOfFeasibleRequests() {
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
            while (hasFeasibleRequests()) {
                findBestCandidate();
                addCandidateIntoRoute();//pesquisar se há outro passageiro que possa desembarcar no mesmo nó, se sim, inserí-lo
                actualizeCandidatesSet();//não usar RRF, deve-se priorizar a janela de tempo
                //evaluateRequestsFeasibility???? ou achar outras solicitações que possam ser atendidas
            }
            addRouteInSolution();
        }
    }

    public void initializeSolution() {
        solution = new Solution();
    }

    public void initializeCandidatesSet() {
        requestsFeasibilityAnalysis();
        prepareAndSetRequestsData();
        setRequestFeasibilityParameters();
        initializeCandidates();
    }

    private void prepareAndSetRequestsData() {
        setDistanceToAttendEveryRequest();
        findMaxAndMinDistance();
        findMaxAndMinTimeWindowLower();
        findMaxAndMinTimeWindowUpper();
        separateRequestsWhichBoardsAndLeavesInNodes();
        setLoadIndexForEveryNode();
        findMaxAndMinLoadIndex();
    }

    private void setRequestFeasibilityParameters() {
        for (Request request : data.getRequests()) {
            request.setDistanceRankingFunction(maxDistance, minDistance);
            request.setDeliveryTimeWindowLowerRankingFunction(maxTimeWindowLower, minTimeWindowLower);
            request.setDeliveryTimeWindowUpperRankingFunction(maxTimeWindowUpper, minTimeWindowUpper);
            request.setOriginNodeRankingFunction(maxLoadIndex, minLoadIndex);
            request.setDestinationNodeRankingFunction(maxLoadIndex, minLoadIndex);
            request.setRequestRankingFunction(0.1, 0.5, 0.1, 0.1, 0.1);
        }
    }

    private void initializeCandidates() {
        data.getRequests().sort(Comparator.comparing(Request::getRequestRankingFunction).reversed());
        this.candidates.addAll(data.getRequests());
    }

    public void findBestCandidate() {
        candidate = candidates.get(0);
        //data.getRequests().forEach(System.out::println);
    }

    public void addCandidateIntoRoute() {
        this.actualRoute.addValueInIntegerRepresentation(candidate.getId());
        scheduleDeliveryTime();
    }

    public void actualizeCandidatesSet() {
        data.setLastPassengerAddedToRoute(data.getRequests().get(0));
        candidates.remove(0);
        data.setCurrentNode(data.getLastPassengerAddedToRoute().getPassengerDestination());
    }

    private boolean stoppingCriterionIsFalse() {
        return !candidates.isEmpty() && !data.getAvaibleVehicles().isEmpty();
    }

    private void startNewRoute() {
        this.actualRoute = new Route();
    }
    
    private boolean hasFeasibleRequests(){
        return true;
    }

    private void scheduleDeliveryTime() {
        this.actualRoute.addValueInIntegerRepresentation(-1*candidate.getDeliveryTimeWindowLowerInMinutes());
        this.data.setCurrentTime(candidate.getDeliveryTimeWindowLower());
    }

    private void addRouteInSolution() {

    }

}
