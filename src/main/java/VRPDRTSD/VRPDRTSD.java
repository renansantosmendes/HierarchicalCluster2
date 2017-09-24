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
import java.util.Collections;
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
            while (hasFeasibleRequests() && hasEmptySeatInVehicle()) {
                //System.out.println(data.getCurrentVehicle().getBusySeats());
                findBestCandidateUsingRRF();
                addCandidateIntoRoute();
                actualizeRequestsData();
                if (hasEmptySeatInVehicle()) {
                    findOtherRequestsThatCanBeAttended();
                }
                requestsFeasibilityAnalysisInConstructionFase();
            }
            finalizeRoute();
            addRouteInSolution();
            List<Integer> list = currentRoute.getIntegerRouteRepresetation();
            System.out.println(list);
        }

        System.out.println(candidates);
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
        candidates.sort(Comparator.comparing(Request::getRequestRankingFunction).reversed());
        List<Request> feasibleCandidates = new ArrayList<>();
        feasibleCandidates.addAll(candidates.stream().filter(Request::isFeasible).collect(Collectors.toCollection(ArrayList::new)));
        if (feasibleCandidates.size() != 0) {
            candidate = feasibleCandidates.get(0);
        }
    }

    public void addCandidateIntoRoute() {

        if (currentRoute.getIntegerRouteRepresetation().size() == 0) {
            data.setCurrentTime(candidate.getDeliveryTimeWindowLower());
        } else {
            Duration displacementTime = data.getDuration()[data.getLastPassengerAddedToRoute().getDestination().getId()][candidate.getDestination().getId()];
            data.setCurrentTime(data.getCurrentTime().plus(displacementTime));
        }

        int indexOfCandidate = candidates.indexOf(candidate);
        data.setLastPassengerAddedToRoute(candidates.get(indexOfCandidate));
        data.setCurrentNode(data.getLastPassengerAddedToRoute().getDestination());

        candidate.setDeliveryTime(data.getCurrentTime());
        currentRoute.addValueInIntegerRepresentation(candidate.getId());

        data.getCurrentVehicle().boardPassenger();
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
        data.getAvaibleVehicles().remove(0);
        data.setCurrentNode(data.getNodes().get(0));
        data.setCurrentTime(LocalDateTime.of(2017, 1, 1, 0, 0, 0));
    }

    public boolean hasFeasibleRequests() {
        return candidates.stream()
                .filter(r -> r.isFeasible())
                .collect(Collectors.toCollection(ArrayList::new)).size() != 0;
    }

    public boolean hasEmptySeatInVehicle() {
        return data.getCurrentVehicle().getBusySeats() < data.getCurrentVehicle().getCapacity();
    }

    public void scheduleDeliveryTimeInRouteRepresentation() {
        //currentRoute.addValueInIntegerRepresentation(-1 * candidate.getDeliveryTimeWindowLowerInMinutes());
        currentRoute.addValueInIntegerRepresentation(-1 * candidate.getDeliveryTimeInMinutes());
    }

    public void scheduleDeliveryTime(Request request) {
        currentRoute.addValueInIntegerRepresentation(-1 * request.getDeliveryTimeWindowLowerInMinutes());
    }

    public void scheduleDeliveryTime(LocalDateTime ldt) {
        currentRoute.addValueInIntegerRepresentation(-1 * (ldt.getHour() * 60 + ldt.getMinute()));
    }

    public void schedulePickUpTime() {
        List<Integer> pickupSequence = currentRoute.getIntegerRouteRepresetation()
                .stream().filter(u -> u.intValue() > 0)
                .collect(Collectors.toCollection(ArrayList::new));

        List<Integer> pickupTimes = new ArrayList<>();
        List<Integer> timesBetween = new ArrayList<>();
        int startDeliveryTimeInteger = getFirstDeliveryTime();
        int startDeliveryPassenger = getFirstDeliveryPassengerId();
        int currentTime = startDeliveryTimeInteger;
        pickupSequence.add(startDeliveryPassenger);
        Request passengerOrigin = null, passengerDestination = null;

        for (int i = 0; i < pickupSequence.size() - 1; i++) {

            for (Request request : data.getRequests()) {
                if (request.getId().equals(pickupSequence.get(i))) {
                    passengerOrigin = request;
                }
                if (request.getId().equals(pickupSequence.get(i + 1))) {
                    passengerDestination = request;
                }
            }

            Duration timeBetween = data
                    .getDuration()[passengerOrigin.getOrigin().getId()][passengerDestination.getOrigin().getId()];

            int integerTimeBetween = (int) timeBetween.toHours() * 60 + (int) timeBetween.toMinutes();
            timesBetween.add(integerTimeBetween);

        }

        for (int i = timesBetween.size() - 1; i >= 0; i--) {
            currentTime = currentTime + timesBetween.get(i);
            pickupTimes.add(currentTime);

        }

        Collections.reverse(pickupTimes);

        List<Integer> pickupScheduledSequence = new ArrayList<>();

        for (int i = 0; i < pickupSequence.size() - 1; i++) {
            pickupScheduledSequence.add(pickupSequence.get(i));
            pickupScheduledSequence.add(pickupTimes.get(i));
        }
        pickupScheduledSequence.addAll(currentRoute.getIntegerRouteRepresetation());
        currentRoute.getIntegerRouteRepresetation().clear();
        currentRoute.getIntegerRouteRepresetation().addAll(pickupScheduledSequence);

        //Parei nessa parte aqui -> setar a hora de embarque dos passageiros no proprio objeto com base na 
        //representação da rota com numeros inteiros
        for (int i = 0; i < pickupScheduledSequence.size() / 2 - 1; i = i + 2) {
            for (Request request : data.getRequests()) {
                if (request.getId().equals(pickupScheduledSequence.get(i))) {
                    request.setPickUpTime(pickupScheduledSequence.get(i + 1));
                }
            }
        }
    }

    private Integer getFirstDeliveryTime() {
        return currentRoute.getIntegerRouteRepresetation().get(1);
    }

    private Integer getFirstDeliveryPassengerId() {
        return currentRoute.getIntegerRouteRepresetation().get(0);
    }

    public void addRouteInSolution() {

    }

    public void finalizeRoute() {
        schedulePickUpTime();
        //System.out.println("Current Route = " + currentRoute.getIntegerRouteRepresetation());
        //currentRoute.buildSequenceOfAttendedRequests(data);
        //System.out.println(currentRoute.getSequenceOfAttendedRequests());

        //currentRoute.buildNodesSequence(data);
        //depois de fazer todo planejamento de embarque e desembarque, fazer um 
        //Log com as atividades feitas pelo veículo
    }

    public void findOtherRequestsThatCanBeAttended() {
        List<Request> otherRequestsToAdd = new ArrayList<>();
        for (Request request : requestsThatLeavesInNode.get(data.getCurrentNode())) {
            if (currentTimeIsWithInDeliveryTimeWindow(request)) {
                otherRequestsToAdd.add(request);
            }
        }

        if (otherRequestsToAdd.size() != 0) {
            otherRequestsToAdd.sort(Comparator.comparing(Request::getDeliveryTimeWindowLower));
        }

        while (otherRequestsToAdd.size() != 0) {
            candidate = otherRequestsToAdd.get(0);
            currentRoute.addValueInIntegerRepresentation(otherRequestsToAdd.get(0).getId());
            data.setLastPassengerAddedToRoute(otherRequestsToAdd.get(0));
            data.getCurrentVehicle().boardPassenger();
            candidates.remove(otherRequestsToAdd.get(0));
            requestsThatLeavesInNode.get(data.getCurrentNode()).remove(otherRequestsToAdd.get(0));
            scheduleDeliveryTime(data.getCurrentTime());
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
