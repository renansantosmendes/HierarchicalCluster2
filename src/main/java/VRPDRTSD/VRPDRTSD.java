package VRPDRTSD;

import ProblemRepresentation.*;
import Algorithms.*;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author renansantos
 */
public class VRPDRTSD implements Heuristic {

    private ProblemData data;
    private String instanceName;
    private String nodesInstanceName;
    private String adjacenciesInstanceName;
    private int numberOfVehicles;
    private int vehicleCapacity;
    private double maxDistance;
    private double minDistance;
    private int minTimeWindowLower;
    private int maxTimeWindowLower;
    private int minTimeWindowUpper;
    private int maxTimeWindowUpper;
    private Map<Node, List<Request>> requestsThatBoardsInNode;
    private Map<Node, List<Request>> requestsThatLeavesInNode;
    private int maxLoadIndex;
    private int minLoadIndex;
    private Solution solution;
    private List<Request> candidates = new ArrayList<>();
    private List<Request> feasibleRequests = new ArrayList<>();
    private Request candidate;
    private Route currentRoute;

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

    public Solution getSolution() {
        return solution;
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
        }
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
        }
        finalizeSolution();
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
        currentRoute.addValueInIntegerRepresentation(-1 * candidate.getDeliveryTimeInMinutes());
    }

    public void scheduleDeliveryTime(Request request) {
        currentRoute.addValueInIntegerRepresentation(-1 * request.getDeliveryTimeWindowLowerInMinutes());
    }

    public void scheduleDeliveryTime(LocalDateTime ldt) {
        currentRoute.addValueInIntegerRepresentation(-1 * (ldt.getHour() * 60 + ldt.getMinute()));
    }

    private Integer getFirstDeliveryTime() {
        return currentRoute.getIntegerRouteRepresetation().get(1);
    }

    private Integer getFirstDeliveryPassengerId() {
        return currentRoute.getIntegerRouteRepresetation().get(0);
    }

    public void addRouteInSolution() {
        solution.addRoute(currentRoute);
    }

    public void finalizeRoute() {
        addPickupSequence();
        scheduleRoute();
        buildSequenceOfAttendedRequests();
        buildNodesSequence();
        evaluateRoute();
    }

    private void addPickupSequence() {
        List<Integer> deliverySequence = getOnlyIdSequence();
        List<Integer> pickupSequence = getOnlyIdSequence();
        List<Integer> idSequence = new ArrayList<>();

        idSequence.addAll(deliverySequence);
        idSequence.addAll(pickupSequence);
        idSequence.add(0);
        idSequence.add(0, 0);

        currentRoute.clearIntegerRepresentation();
        currentRoute.setIntegerRouteRepresetation(idSequence);
    }

    public void scheduleRoute() {
        currentRoute.scheduleRoute(data);
    }

    private List<Integer> getOnlyIdSequence() {
        List<Integer> idSequence = currentRoute.getIntegerRouteRepresetation()
                .stream().filter(u -> u.intValue() > 0)
                .collect(Collectors.toCollection(ArrayList::new));
        return idSequence;
    }

    public Request getRequestUsingId(Integer id) {
        if (id != 0) {
            return data.getRequests().stream().filter(u -> u.getId().equals(id)).findAny().get();
        } else {
            return null;
        }
    }

    private void buildSequenceOfAttendedRequests() {
        currentRoute.buildSequenceOfAttendedRequests(data);
    }

    private void buildNodesSequence() {
        currentRoute.buildNodesSequence(data);
    }

    private void evaluateRoute() {
        this.currentRoute.evaluateRoute(data);
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
            candidate.setDeliveryTime(data.getCurrentTime());
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

    private void finalizeSolution() {
        solution.calculateEvaluationFunction();
    }

    @Override
    public void buildRandomSolution() {
        initializeSolution();
        initializeRandomCandidatesSet();
        while (stoppingCriterionIsFalse()) {
            startNewRoute();
            requestsFeasibilityAnalysis();
            while (hasFeasibleRequests() && hasEmptySeatInVehicle()) {
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
        }
        finalizeSolution();
    }

    public void buildSelfishSolution() {
        VRPDRTSD problem = new VRPDRTSD(instanceName, nodesInstanceName, adjacenciesInstanceName, numberOfVehicles, 1);
        problem.buildGreedySolution();
        this.solution = problem.getSolution();
    }

    public void initializeRandomCandidatesSet() {
        originalRequestsFeasibilityAnalysis();
        prepareAndSetRequestsData();
        setRequestRandomParameters();
        initializeCandidates();
    }

    public void setRequestRandomParameters() {
        Random rnd = new Random();
        for (Request request : data.getRequests()) {
            request.setRequestRankingFunction(rnd.nextDouble());
        }
    }

    @Override
    public void localSearch(int localSearchType) {
        switch (localSearchType) {
            case 1:
                this.solution = swapIntraRouteFirstImprovement();
                break;
            case 2:
                this.solution = swapIntraBestImprovement();
                break;
            case 3:
                this.solution = addMinutesInSolutionScheduleFirstImprovement();
                break;
            case 4:
                this.solution = addMinutesInSolutionScheduleBestImprovement();
                break;
            case 5:
                this.solution = removeMinutesInSolutionScheduleFirstImprovement();
                break;
            case 6:
                this.solution = removeMinutesInSolutionScheduleBestImprovement();
                break;
            case 7:
                this.solution = swapInterRouteFirstImprovement();
                break;
            case 8:
                this.solution = swapInterRouteBestImprovement();
                break;
            case 9:
                //this.solution = reallocationFirstImprovement();
                break;
            case 10:
                this.solution = requestReallocationBestImprovement();
                break;
        }
    }

    private Solution swapIntraRouteFirstImprovement() {
        Solution solution = new Solution(this.solution);
        for (int i = 0; i < solution.getRoutes().size(); i++) {
            Route route = new Route(solution.getRoute(i));
            long evaluationFunctionBeforeMovement = solution.getEvaluationFunction();
            for (int j = 1; j < route.getIntegerSequenceOfAttendedRequests().size(); j++) {
                for (int k = j + 1; k < route.getIntegerSequenceOfAttendedRequests().size(); k++) {
                    route.swapRequests(j, k, data);
                    solution.setRoute(i, route);
                    solution.calculateEvaluationFunction();
                    long evaluationFunctionAfterMovement = solution.getEvaluationFunction();

                    if (evaluationFunctionAfterMovement < evaluationFunctionBeforeMovement) {
                        return solution;
                    } else {
                        route.swapRequests(j, k, data);
                    }
                }
            }
        }
        return this.solution;
    }

    private Solution swapIntraBestImprovement() {
        Solution solution = new Solution((Solution) this.solution.clone());
        for (int i = 0; i < solution.getRoutes().size(); i++) {
            Route route = new Route(solution.getRoute(i));
            long evaluationFunctionBeforeMovement = solution.getEvaluationFunction();
            for (int j = 1; j < route.getIntegerSequenceOfAttendedRequests().size() - 1; j++) {
                for (int k = j + 1; k < route.getIntegerSequenceOfAttendedRequests().size(); k++) {
                    route.swapRequests(j, k, data);
                    solution.setRoute(i, route);
                    solution.calculateEvaluationFunction();
                    long evaluationFunctionAfterMovement = solution.getEvaluationFunction();
                    if (evaluationFunctionAfterMovement > evaluationFunctionBeforeMovement) {
                        route.swapRequests(j, k, data);
                        solution.setRoute(i, route);
                        solution.calculateEvaluationFunction();
                    }
                }
            }
        }
        if (solution.getEvaluationFunction() < this.solution.getEvaluationFunction()) {
            return solution;
        } else {
            return this.solution;
        }
    }

    private Solution addMinutesInSolutionScheduleFirstImprovement() {
        Solution solution = new Solution(this.solution);

        for (int i = 0; i < solution.getRoutes().size(); i++) {
            for (int j = 1; j <= 5; j++) {
                Route route = new Route(solution.getRoute(i));
                long evaluationFunctionBeforeMovement = solution.getEvaluationFunction();

                route.addMinutesInRoute(j, data);
                solution.setRoute(i, route);
                solution.calculateEvaluationFunction();
                long evaluationFunctionAfterMovement = solution.getEvaluationFunction();

                if (evaluationFunctionAfterMovement < evaluationFunctionBeforeMovement) {
                    return solution;
                } else {
                    route.removeMinutesInRoute(j, data);
                }
            }
        }
        return this.solution;
    }

    private Solution addMinutesInSolutionScheduleBestImprovement() {
        Solution solution = new Solution(this.solution);

        for (int i = 0; i < solution.getRoutes().size(); i++) {
            for (int j = 1; j <= 5; j++) {
                Route route = new Route(solution.getRoute(i));
                long evaluationFunctionBeforeMovement = solution.getEvaluationFunction();

                route.addMinutesInRoute(j, data);
                solution.setRoute(i, route);
                solution.calculateEvaluationFunction();
                long evaluationFunctionAfterMovement = solution.getEvaluationFunction();

                if (evaluationFunctionAfterMovement > evaluationFunctionBeforeMovement) {
                    route.removeMinutesInRoute(j, data);
                    solution.setRoute(i, route);
                    solution.calculateEvaluationFunction();
                }
            }
        }
        if (solution.getEvaluationFunction() < this.solution.getEvaluationFunction()) {
            return solution;
        } else {
            return this.solution;
        }
    }

    private Solution removeMinutesInSolutionScheduleBestImprovement() {
        Solution solution = new Solution(this.solution);

        for (int i = 0; i < solution.getRoutes().size(); i++) {
            for (int j = 1; j <= 5; j++) {
                Route route = new Route(solution.getRoute(i));
                long evaluationFunctionBeforeMovement = solution.getEvaluationFunction();

                route.removeMinutesInRoute(j, data);
                solution.setRoute(i, route);
                solution.calculateEvaluationFunction();
                long evaluationFunctionAfterMovement = solution.getEvaluationFunction();

                if (evaluationFunctionAfterMovement < evaluationFunctionBeforeMovement) {
                    return solution;
                } else {
                    route.addMinutesInRoute(j, data);
                }
            }
        }
        return this.solution;
    }

    private Solution removeMinutesInSolutionScheduleFirstImprovement() {
        Solution solution = new Solution(this.solution);

        for (int i = 0; i < solution.getRoutes().size(); i++) {
            for (int j = 1; j <= 5; j++) {
                Route route = new Route(solution.getRoute(i));
                long evaluationFunctionBeforeMovement = solution.getEvaluationFunction();

                route.removeMinutesInRoute(j, data);
                solution.setRoute(i, route);
                solution.calculateEvaluationFunction();
                long evaluationFunctionAfterMovement = solution.getEvaluationFunction();

                if (evaluationFunctionAfterMovement > evaluationFunctionBeforeMovement) {
                    route.addMinutesInRoute(j, data);
                    solution.setRoute(i, route);
                    solution.calculateEvaluationFunction();
                }
            }
        }
        if (solution.getEvaluationFunction() < this.solution.getEvaluationFunction()) {
            return solution;
        } else {
            return this.solution;
        }
    }

    private Solution swapInterRouteFirstImprovement() {
        Solution solution = new Solution(this.solution);

        for (int i = 0; i < solution.getRoutes().size(); i++) {
            long evaluationFunctionBeforeMovement = solution.getEvaluationFunction();
            LinkedHashSet<Integer> firstRoute = new LinkedHashSet<>();
            firstRoute.addAll(returnUsedIds(solution, i));

            for (int j = i + 1; j < solution.getRoutes().size(); j++) {
                LinkedHashSet<Integer> secondRoute = new LinkedHashSet<>();
                secondRoute.addAll(returnUsedIds(solution, j));

                for (int firstId : firstRoute) {
                    for (int secondId : secondRoute) {
                        solution.getRoute(i).replaceRequest(firstId, secondId, data);
                        solution.getRoute(j).replaceRequest(secondId, firstId, data);
                        solution.calculateEvaluationFunction();
                        long evaluationFunctionAfterMovement = solution.getEvaluationFunction();
                        if (evaluationFunctionAfterMovement < evaluationFunctionBeforeMovement) {
                            return solution;
                        } else {
                            solution.getRoute(i).replaceRequest(secondId, firstId, data);
                            solution.getRoute(j).replaceRequest(firstId, secondId, data);
                            solution.calculateEvaluationFunction();
                        }
                    }
                }
            }
        }

        return this.solution;
    }

    private static List<Integer> returnUsedIds(Solution solution, int routePosition) {
        Set<Integer> setOfIds = solution.getRoute(routePosition).getIntegerRouteRepresetation()
                .stream()
                .filter(u -> u.intValue() > 0)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        List<Integer> idsUsed = new ArrayList<>();
        for (int id : setOfIds) {
            idsUsed.add(id);
        }
        return idsUsed;
    }

    private Solution swapInterRouteBestImprovement() {
        Solution solution = new Solution(this.solution);

        for (int i = 0; i < solution.getRoutes().size(); i++) {
            long evaluationFunctionBeforeMovement = solution.getEvaluationFunction();
            List<Integer> firstRoute = new ArrayList<>();
            firstRoute.addAll(returnUsedIds(solution, i));

            for (int j = i + 1; j < solution.getRoutes().size(); j++) {
                List<Integer> secondRoute = new ArrayList<>();
                secondRoute.addAll(returnUsedIds(solution, j));

                for (int k = 0; k < firstRoute.size(); k++) {
                    for (int l = 0; l < secondRoute.size(); l++) {
                        solution.getRoute(i).replaceRequest(firstRoute.get(k), secondRoute.get(l), data);
                        solution.getRoute(j).replaceRequest(secondRoute.get(l), firstRoute.get(k), data);
                        solution.calculateEvaluationFunction();
                        long evaluationFunctionAfterMovement = solution.getEvaluationFunction();

                        if (evaluationFunctionAfterMovement > evaluationFunctionBeforeMovement) {
                            solution.getRoute(i).replaceRequest(secondRoute.get(l), firstRoute.get(k), data);
                            solution.getRoute(j).replaceRequest(firstRoute.get(k), secondRoute.get(l), data);
                            solution.calculateEvaluationFunction();
                        } else {
                            evaluationFunctionBeforeMovement = evaluationFunctionAfterMovement;
                            int removedIdFromFirstRoute = firstRoute.get(k);
                            int removedIdFromSecondRoute = secondRoute.get(l);
                            firstRoute.set(k, removedIdFromSecondRoute);
                            secondRoute.set(l, removedIdFromFirstRoute);
                        }
                    }
                }
            }
        }

        if (solution.getEvaluationFunction() < this.solution.getEvaluationFunction()) {
            return solution;
        } else {
            return this.solution;
        }
    }

    private Solution requestReallocationBestImprovement() {
        Solution solution = new Solution(this.solution);

        for (int i = 0; i < solution.getRoutes().size(); i++) {
            Route firstRoute = new Route(solution.getRoute(i));
            long evaluationFunctionBeforeMovement = solution.getEvaluationFunction();

            List<Integer> firstRouteIdSequence = new ArrayList<>();
            firstRouteIdSequence.addAll(returnUsedIds(solution, i));

            for (int j = i + 1; j < solution.getRoutes().size(); j++) {
                Route secondRoute = new Route(solution.getRoute(j));
                for (int k = 0; k < firstRouteIdSequence.size(); k++) {
                    int requestId = firstRouteIdSequence.get(k);
                    List<Integer> idSequence = new ArrayList<>();
                    idSequence.addAll(secondRoute.getIntegerSequenceOfAttendedRequests());

                    for (int l = 1; l < idSequence.size() - 1; l++) {
                        for (int m = l + 1; m < idSequence.size(); m++) {
                            List<Integer> newIdSequence = new ArrayList<>();

                            newIdSequence.addAll(idSequence.subList(0, l));
                            newIdSequence.add(requestId);
                            newIdSequence.addAll(idSequence.subList(l, m - 1));
                            newIdSequence.add(requestId);
                            newIdSequence.addAll(idSequence.subList(m - 1, idSequence.size()));

//                            System.out.println("sequence = " + idSequence);
                            System.out.println("newSequence = " + newIdSequence);
                        }

                    }
                }
            }
        }

        if (solution.getEvaluationFunction() < this.solution.getEvaluationFunction()) {
            return solution;
        } else {
            return this.solution;
        }
    }
}
