package VRPDRTSD;

import Algorithms.Algorithm;
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
    Request candidate;

    public VRPDRTSD(String instanceName, String nodesInstanceName, String adjacenciesInstanceName) {
        this.instanceName = instanceName;
        this.nodesInstanceName = nodesInstanceName;
        this.adjacenciesInstanceName = adjacenciesInstanceName;
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
        data = new ProblemData(instanceName, nodesInstanceName, adjacenciesInstanceName);
    }

    @Override
    public void buildGreedySolution() {
        initializeSolution();
        initializeCandidatesElementsSet();
        while (stoppingCriterionIsFalse()) {
            findBestCandidate();
            addCandidateIntoSolution();
            actualizeCandidatesElementsSet();
        }
    }

    public void initializeSolution() {
        solution = new Solution();
    }

    public void initializeCandidatesElementsSet() {
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
        this.candidates.addAll(data.getRequests());
    }

    public void findBestCandidate() {
        data.getRequests().sort(Comparator.comparing(Request::getRequestRankingFunction).reversed());
        data.getRequests().forEach(System.out::println);
    }

    public void addCandidateIntoSolution() {

    }

    public void actualizeCandidatesElementsSet() {
        data.setLastPassengerAddedToRoute(data.getRequests().get(0));
        candidates.remove(0);
        data.setCurrentNode(data.getLastPassengerAddedToRoute().getPassengerOrigin());
    }

    private boolean stoppingCriterionIsFalse() {
        return !candidates.isEmpty();
    }

}
