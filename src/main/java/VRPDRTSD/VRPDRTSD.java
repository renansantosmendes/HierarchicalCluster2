package VRPDRTSD;

import ProblemRepresentation.*;
import Algorithms.*;
import InstanceReader.DataOutput;
import InstanceReader.Instance;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import jxl.read.biff.BiffException;

/**
 *
 * @author renansantos
 */
public class VRPDRTSD implements Metaheuristic {

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
    private int localSearchType = 1;
    private String excelDataFilesPath;
    private Instance instance;

    public VRPDRTSD(Instance instance) {
        this.instance = instance;
        this.instanceName = instance.getInstanceName();
        this.nodesInstanceName = instance.getNodesData();
        this.adjacenciesInstanceName = instance.getAdjacenciesData();
        this.numberOfVehicles = instance.getNumberOfVehicles();
        this.vehicleCapacity = instance.getVehicleCapacity();
        this.readInstance();
    }

    public VRPDRTSD(Instance instance, String excelDataFilesPath) {
        this.instance = instance;
        this.instanceName = instance.getInstanceName();
        this.nodesInstanceName = instance.getNodesData();
        this.adjacenciesInstanceName = instance.getAdjacenciesData();
        this.numberOfVehicles = instance.getNumberOfVehicles();
        this.vehicleCapacity = instance.getVehicleCapacity();
        this.excelDataFilesPath = excelDataFilesPath;
        this.readExcelInstance();
    }

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
    
    public void setSolution(Solution solution){
        this.solution.setSolution(solution);
    }
    
    public void printSolutionInformations() {
        this.solution.printAllInformations();
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
        //System.out.println(getRequestUsingId(230).isFeasible());
    }

    public void requestsFeasibilityAnalysisInConstructionFase() {
//        for (Request request : candidates) {
//            request.determineInicialFeasibility(data.getCurrentTime(), data.getCurrentNode(), data.getDuration());
//        }
        for (Request request : candidates) {
            request.determineFeasibilityInConstructionFase(data.getCurrentTime(), data.getLastPassengerAddedToRoute(),
                    data.getCurrentNode(), data.getDuration());
        }
        //System.out.println(getRequestUsingId(230).isFeasible());
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

    public void readExcelInstance() {
        try {
            data = new ProblemData(instance, this.excelDataFilesPath);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (BiffException ex) {
            ex.printStackTrace();
        }
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

    public void recalculateRRF() {
        setDistanceToAttendEveryRequest();
        findMaxAndMinDistance();
        findMaxAndMinTimeWindowLower();
        findMaxAndMinTimeWindowUpper();
        findMaxAndMinLoadIndex();
        setRequestFeasibilityParameters();
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
        candidates.clear();
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
            candidate.setDeliveryTime(data.getCurrentTime());
        } else {
            Duration displacementTime = data.getDuration()[data.getLastPassengerAddedToRoute().getDestination().getId()][candidate.getDestination().getId()];
            data.setCurrentTime(data.getCurrentTime().plus(displacementTime));
            candidate.setDeliveryTime(data.getCurrentTime());
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
        recalculateRRF();
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
        buildSequenceOfAttendedRequests();
        scheduleRoute();
        buildNodesSequence();
        evaluateRoute();
        //improveSchedule();
    }

    private void improveSchedule() {
        currentRoute.improveSchedule(data);
    }

    private void addPickupSequence() {
        List<Integer> deliverySequence = getOnlyIdSequence();
        List<Integer> pickupSequence = getOnlyIdSequence();
        List<Integer> idSequence = new ArrayList<>();
        //buildPickupSequence(pickupSequence,deliverySequence);
        idSequence.addAll(pickupSequence);
        idSequence.addAll(deliverySequence);

        idSequence.add(0);
        idSequence.add(0, 0);

        currentRoute.clearIntegerRepresentation();
        currentRoute.setIntegerRouteRepresetation(idSequence);
    }

    private void buildPickupSequence(List<Integer> pickupSequence, List<Integer> deliverySequence) {
        Request firstDeliveryRequest = getRequestUsingId(deliverySequence.get(0));
        List<Request> deliveryPassengers = new ArrayList<>();

        for (int i = 0; i < deliverySequence.size(); i++) {
            Request requestToAdd = getRequestUsingId(deliverySequence.get(i));
            deliveryPassengers.add(requestToAdd);
        }

        deliveryPassengers.sort(Comparator.comparing(Request::getDeliveryTimeWindowLowerInMinutes));
        pickupSequence.clear();
        pickupSequence.addAll(deliveryPassengers.stream().map(Request::getId).collect(Collectors.toCollection(ArrayList::new)));
    }

    public void scheduleRoute() {
        currentRoute.scheduleRouteUsingBestScheduling(data);
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

        while (otherRequestsToAdd.size() != 0 && hasEmptySeatInVehicle()) {
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
        solution.calculateEvaluationFunction(data);
        solution.buildIntegerRepresentation();
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

    public void buildSelfishSolution() throws BiffException, IOException {
        if (this.excelDataFilesPath != null) {
            instance.setVehicleCapacity(1);
            VRPDRTSD problem = new VRPDRTSD(instance, excelDataFilesPath);
            problem.buildGreedySolution();
            this.solution = problem.getSolution();
        } else {
            VRPDRTSD problem = new VRPDRTSD(instanceName, nodesInstanceName, adjacenciesInstanceName, numberOfVehicles, 1);
            problem.buildGreedySolution();
            this.solution = problem.getSolution();
        }
    }

    public void initializeRandomCandidatesSet() {
        originalRequestsFeasibilityAnalysis();
        prepareAndSetRequestsData();
        setRequestRandomParameters();
        initializeCandidates();
        data.startVehiclesData();
    }

    public void setRequestRandomParameters() {
        Random rnd = new Random();
        for (Request request : data.getRequests()) {
            Double RRF = rnd.nextDouble();
            request.setRequestRankingFunction(RRF);
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
                this.solution = swapInterRouteFirstImprovement();
                break;
            case 4:
                this.solution = swapInterRouteBestImprovement();
                break;
            case 5:
                this.solution = requestReallocationFirstImprovement();
                break;
            case 6:
                this.solution = requestReallocationBestImprovement();
                break;
            case 7://
                this.solution = addMinutesInSolutionScheduleFirstImprovement();
                break;
            case 8:
                this.solution = addMinutesInSolutionScheduleBestImprovement();
                break;
            case 9:
                this.solution = removeMinutesInSolutionScheduleFirstImprovement();
                break;
            case 10:
                this.solution = removeMinutesInSolutionScheduleBestImprovement();
                break;

        }
        this.solution.buildIntegerRepresentation();
    }

    private Solution swapIntraRouteFirstImprovement() {
        Solution solution = new Solution(this.solution);
        for (int i = 0; i < solution.getNumberOfRoutes(); i++) {
            Route route = new Route(solution.getRoute(i));
            long evaluationFunctionBeforeMovement = solution.getEvaluationFunction();
            for (int j = 1; j < route.getIntegerSequenceOfAttendedRequests().size(); j++) {
                for (int k = j + 1; k < route.getIntegerSequenceOfAttendedRequests().size(); k++) {
                    route.swapRequests(j, k, data);
                    actualizeSolution(solution, i, route);
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

    private void actualizeSolution(Solution solution, int i, Route route) {
        solution.getRoute(i).setRoute(route);
        solution.calculateEvaluationFunction(data);
    }

    private Solution swapIntraBestImprovement() {
        boolean canContinue = true;
        while (canContinue) {
            Solution solution = new Solution((Solution) this.solution.clone());
            for (int i = 0; i < solution.getNumberOfRoutes(); i++) {
                Route route = new Route(solution.getRoute(i));
                long evaluationFunctionBeforeMovement = solution.getEvaluationFunction();
                for (int j = 1; j < route.getIntegerSequenceOfAttendedRequests().size() - 1; j++) {
                    for (int k = j + 1; k < route.getIntegerSequenceOfAttendedRequests().size(); k++) {
                        route.swapRequests(j, k, data);
                        actualizeSolution(solution, i, route);
                        long evaluationFunctionAfterMovement = solution.getEvaluationFunction();
                        if (evaluationFunctionAfterMovement > evaluationFunctionBeforeMovement) {
                            route.swapRequests(j, k, data);
                            actualizeSolution(solution, i, route);
                        } else {
                            evaluationFunctionBeforeMovement = evaluationFunctionAfterMovement;
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
        return (Solution) this.solution.clone();
    }

    private Solution addMinutesInSolutionScheduleFirstImprovement() {
        Solution solution = new Solution(this.solution);

        for (int i = 0; i < solution.getNumberOfRoutes(); i++) {
            for (int j = 1; j <= 5; j++) {
                Route route = new Route(solution.getRoute(i));
                long evaluationFunctionBeforeMovement = solution.getEvaluationFunction();

                route.addMinutesInRoute(j, data);
                actualizeSolution(solution, i, route);
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

        for (int i = 0; i < solution.getNumberOfRoutes(); i++) {
            for (int j = 1; j <= 5; j++) {
                Route route = new Route(solution.getRoute(i));
                long evaluationFunctionBeforeMovement = solution.getEvaluationFunction();

                route.addMinutesInRoute(j, data);
                actualizeSolution(solution, i, route);
                long evaluationFunctionAfterMovement = solution.getEvaluationFunction();

                if (evaluationFunctionAfterMovement > evaluationFunctionBeforeMovement) {
                    route.removeMinutesInRoute(j, data);
                    actualizeSolution(solution, i, route);
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

        for (int i = 0; i < solution.getNumberOfRoutes(); i++) {
            for (int j = 1; j <= 5; j++) {
                Route route = new Route(solution.getRoute(i));
                long evaluationFunctionBeforeMovement = solution.getEvaluationFunction();

                route.removeMinutesInRoute(j, data);
                actualizeSolution(solution, i, route);
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

        for (int i = 0; i < solution.getNumberOfRoutes(); i++) {
            for (int j = 1; j <= 5; j++) {
                Route route = new Route(solution.getRoute(i));
                long evaluationFunctionBeforeMovement = solution.getEvaluationFunction();

                route.removeMinutesInRoute(j, data);
                actualizeSolution(solution, i, route);
                long evaluationFunctionAfterMovement = solution.getEvaluationFunction();

                if (evaluationFunctionAfterMovement > evaluationFunctionBeforeMovement) {
                    route.addMinutesInRoute(j, data);
                    actualizeSolution(solution, i, route);
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

        for (int i = 0; i < solution.getNumberOfRoutes(); i++) {
            long evaluationFunctionBeforeMovement = solution.getEvaluationFunction();
            LinkedHashSet<Integer> firstRoute = new LinkedHashSet<>();
            firstRoute.addAll(returnUsedIds(solution, i));

            for (int j = i + 1; j < solution.getNumberOfRoutes(); j++) {
                LinkedHashSet<Integer> secondRoute = new LinkedHashSet<>();
                secondRoute.addAll(returnUsedIds(solution, j));

                for (int firstId : firstRoute) {
                    for (int secondId : secondRoute) {
                        solution.getRoute(i).replaceRequest(firstId, secondId, data);
                        solution.getRoute(j).replaceRequest(secondId, firstId, data);
                        solution.calculateEvaluationFunction(data);
                        long evaluationFunctionAfterMovement = solution.getEvaluationFunction();
                        if (evaluationFunctionAfterMovement < evaluationFunctionBeforeMovement) {
                            return solution;
                        } else {
                            solution.getRoute(i).replaceRequest(secondId, firstId, data);
                            solution.getRoute(j).replaceRequest(firstId, secondId, data);
                            evaluateSolution(solution);
                        }
                    }
                }
            }
        }

        return this.solution;
    }

    private void evaluateSolution(Solution solution) {
        solution.calculateEvaluationFunction(data);
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
        boolean canContinue = true;
        while (canContinue) {
            Solution solution = new Solution(this.solution);

            for (int i = 0; i < solution.getNumberOfRoutes(); i++) {
                long evaluationFunctionBeforeMovement = solution.getEvaluationFunction();
                List<Integer> firstRoute = new ArrayList<>();
                firstRoute.addAll(returnUsedIds(solution, i));

                for (int j = i + 1; j < solution.getNumberOfRoutes(); j++) {
                    List<Integer> secondRoute = new ArrayList<>();
                    secondRoute.addAll(returnUsedIds(solution, j));

                    for (int k = 0; k < firstRoute.size(); k++) {
                        for (int l = 0; l < secondRoute.size(); l++) {

                            swapRequestsInDifferentRoutes(solution, i, firstRoute, k, secondRoute, l, j);
                            long evaluationFunctionAfterMovement = solution.getEvaluationFunction();

//                        solution.getRoutes().forEach(System.out::println);
                            if (evaluationFunctionAfterMovement > evaluationFunctionBeforeMovement) {
                                swapRequestsInDifferentRoutes(solution, i, secondRoute, l, firstRoute, k, j);
                            } else {
                                //System.out.println("iteration = " + solution);
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
                evaluateSolution(solution);
                return (Solution) solution.clone();
            } else {
                //canContinue = false;
                return (Solution) this.solution.clone();
            }
        }
        return (Solution) this.solution.clone();
    }

    private void swapRequestsInDifferentRoutes(Solution solution1, int i, List<Integer> firstRoute, int k, List<Integer> secondRoute, int l, int j) {
        solution1.getRoute(i).replaceRequest(firstRoute.get(k), secondRoute.get(l), data);
        solution1.getRoute(j).replaceRequest(secondRoute.get(l), firstRoute.get(k), data);
        evaluateSolution(solution1);
    }

    private void insertIdInNewSequence(List<Integer> newIdSequence, List<Integer> idSequenceToInsertRequest,
            int l, int requestId, int m) {
        newIdSequence.addAll(idSequenceToInsertRequest.subList(0, l));
        newIdSequence.add(requestId);
        newIdSequence.addAll(idSequenceToInsertRequest.subList(l, m - 1));
        newIdSequence.add(requestId);
        newIdSequence.addAll(idSequenceToInsertRequest.subList(m - 1, idSequenceToInsertRequest.size()));
    }

    private Solution addRouteFirstImprovement() {
        Solution solution = new Solution((Solution) this.solution.clone());
        for (int i = 0; i < solution.getNumberOfRoutes(); i++) {
            Route firstRoute = new Route(solution.getRoute(i));
            long evaluationFunctionBeforeMovement = solution.getEvaluationFunction();
            for (int j = 0; j < solution.getNumberOfRoutes(); j++) {
                if (i != j) {
                    Route secondRoute = new Route(solution.getRoute(j));
                    List<Integer> initialSequence = new ArrayList<>();
                    List<Integer> finalSequence = new ArrayList<>();
                    List<Integer> newSequence = new ArrayList<>();
                    initialSequence.addAll(secondRoute.getIntegerSequenceOfAttendedRequests());
                    initialSequence.remove(initialSequence.size() - 1);
                    finalSequence.addAll(firstRoute.getIntegerSequenceOfAttendedRequests());
                    finalSequence.remove(0);
                    //initialSequence.addAll(finalSequence);
                    newSequence.addAll(initialSequence);
                    newSequence.addAll(finalSequence);
                    System.out.println(newSequence);
                    secondRoute.clear();
                    secondRoute.rebuild(newSequence, data);

                    System.out.println(secondRoute.getIntegerRouteRepresetation());

                    actualizeSolution(solution, j, secondRoute);

                    long evaluationFunctionAfterMovement = solution.getEvaluationFunction();
                    //System.out.println(evaluationFunctionAfterMovement - evaluationFunctionBeforeMovement);
                    if (evaluationFunctionAfterMovement < evaluationFunctionBeforeMovement) {
                        solution.getRoutes().remove(i);
                        i = 0;
                        System.out.println("entrou");
                        return solution;
                    } else {
                        secondRoute.removeAddedRequests(finalSequence, data);
                        actualizeSolution(solution, j, secondRoute);
                    }
                }
            }
        }
        return this.solution;
    }

    private Solution addRouteBestImprovement() {
        Solution solution = new Solution((Solution) this.solution.clone());

        return this.solution;
    }

    private Solution requestReallocationFirstImprovement() {
        boolean canContinue = true;
        while (canContinue) {
            Solution solution = new Solution(this.solution);
            Solution bestSolution = new Solution(solution);
            for (int i = 0; i < solution.getNumberOfRoutes(); i++) {
                Route firstRouteOriginal = new Route(solution.getRoute(i));
                long evaluationFunctionBeforeMovement = bestSolution.getEvaluationFunction();
                List<Integer> firstRouteIdSequence = new ArrayList<>();
                firstRouteIdSequence.addAll(returnUsedIds(solution, i));

                for (int j = 0; j < solution.getNumberOfRoutes(); j++) {
                    if (i != j) {

                        Route secondRouteOriginal = new Route((Route) solution.getRoute(j).clone());

                        if (!firstRouteIdSequence.isEmpty()) {
                            for (int k = 0; k < firstRouteIdSequence.size(); k++) {
                                Route secondRoute = new Route((Route) secondRouteOriginal.clone());
                                int requestId = firstRouteIdSequence.get(k);
                                Route firstRoute = new Route((Route) firstRouteOriginal.clone());
                                firstRoute.removeRequest(requestId, data);

                                List<Integer> idSequenceToInsertRequest = new ArrayList<>();
                                idSequenceToInsertRequest.addAll(secondRouteOriginal.getIntegerSequenceOfAttendedRequests());

                                for (int l = 1; l < idSequenceToInsertRequest.size(); l++) {
                                    for (int m = l + 1; m < idSequenceToInsertRequest.size() + 1; m++) {
                                        List<Integer> newIdSequence = new ArrayList<>();

                                        insertIdInNewSequence(newIdSequence, idSequenceToInsertRequest, l, requestId, m);
                                        secondRoute.rebuild(newIdSequence, data);

                                        actualizeSolution(solution, i, firstRoute);
                                        actualizeSolution(solution, j, secondRoute);
                                        secondRoute.setRoute((Route) secondRouteOriginal.clone());

                                        long evaluationFunctionAfterMovement = solution.getEvaluationFunction();

                                        if (evaluationFunctionAfterMovement < bestSolution.getEvaluationFunction()) {
                                            bestSolution.setSolution((Solution) solution.clone());
                                            evaluateSolution(bestSolution);
                                            bestSolution.removeEmptyRoutes();
                                            return (Solution) bestSolution.clone();
                                        }
                                        actualizeSolution(solution, i, firstRouteOriginal);
                                        actualizeSolution(solution, j, secondRoute);
                                    }
                                }
                                firstRoute.setRoute(firstRouteOriginal);
                            }
                        }
                    }
                }
            }

            if (bestSolution.getEvaluationFunction() < this.solution.getEvaluationFunction()) {
                evaluateSolution(bestSolution);
                bestSolution.removeEmptyRoutes();
                System.out.println(bestSolution);
                this.solution = (Solution) bestSolution.clone();
            } else {
                canContinue = false;
            }
        }
        return (Solution) this.solution.clone();
    }

    private Solution requestReallocationBestImprovement() {
        boolean canContinue = true;
        while (canContinue) {
            Solution solution = new Solution(this.solution);
            Solution bestSolution = new Solution(solution);
            for (int i = 0; i < solution.getNumberOfRoutes(); i++) {
                Route firstRouteOriginal = new Route(solution.getRoute(i));
                long evaluationFunctionBeforeMovement = bestSolution.getEvaluationFunction();
                List<Integer> firstRouteIdSequence = new ArrayList<>();
                firstRouteIdSequence.addAll(returnUsedIds(solution, i));

                for (int j = 0; j < solution.getNumberOfRoutes(); j++) {
                    if (i != j) {

                        Route secondRouteOriginal = new Route((Route) solution.getRoute(j).clone());

                        if (!firstRouteIdSequence.isEmpty()) {
                            for (int k = 0; k < firstRouteIdSequence.size(); k++) {
                                Route secondRoute = new Route((Route) secondRouteOriginal.clone());
                                int requestId = firstRouteIdSequence.get(k);
                                Route firstRoute = new Route((Route) firstRouteOriginal.clone());
                                firstRoute.removeRequest(requestId, data);

                                List<Integer> idSequenceToInsertRequest = new ArrayList<>();
                                idSequenceToInsertRequest.addAll(secondRouteOriginal.getIntegerSequenceOfAttendedRequests());

                                for (int l = 1; l < idSequenceToInsertRequest.size(); l++) {
                                    for (int m = l + 1; m < idSequenceToInsertRequest.size() + 1; m++) {
                                        List<Integer> newIdSequence = new ArrayList<>();

                                        insertIdInNewSequence(newIdSequence, idSequenceToInsertRequest, l, requestId, m);
                                        secondRoute.rebuild(newIdSequence, data);

                                        actualizeSolution(solution, i, firstRoute);
                                        actualizeSolution(solution, j, secondRoute);
                                        secondRoute.setRoute((Route) secondRouteOriginal.clone());

                                        long evaluationFunctionAfterMovement = solution.getEvaluationFunction();

                                        if (evaluationFunctionAfterMovement < bestSolution.getEvaluationFunction()) {
                                            bestSolution.setSolution((Solution) solution.clone());
//                                            System.out.println("changed request = " + requestId);
                                        }
                                        actualizeSolution(solution, i, firstRouteOriginal);
                                        actualizeSolution(solution, j, secondRoute);
                                    }
                                }
                                firstRoute.setRoute(firstRouteOriginal);
                            }
                        }
                    }
                }
            }

            if (bestSolution.getEvaluationFunction() < this.solution.getEvaluationFunction()) {
                evaluateSolution(bestSolution);
                bestSolution.removeEmptyRoutes();
                System.out.println(bestSolution);
                this.solution = (Solution) bestSolution.clone();
            } else {
                canContinue = false;
            }
        }
        return (Solution) this.solution.clone();
    }

    private Solution requestReallocationTest() {
        boolean canContinue = true;
        while (canContinue) {
            Solution solution = new Solution(this.solution);
            Solution bestSolution = new Solution(solution);
            for (int i = 0; i < solution.getNumberOfRoutes(); i++) {
                Route firstRouteOriginal = new Route(solution.getRoute(i));
                long evaluationFunctionBeforeMovement = bestSolution.getEvaluationFunction();
                List<Integer> firstRouteIdSequence = new ArrayList<>();
                firstRouteIdSequence.addAll(returnUsedIds(solution, i));

                for (int j = 0; j < solution.getNumberOfRoutes(); j++) {
                    if (i != j) {

                        Route secondRouteOriginal = new Route((Route) solution.getRoute(j).clone());

                        if (!firstRouteIdSequence.isEmpty()) {
                            for (int k = 0; k < firstRouteIdSequence.size(); k++) {
                                Route secondRoute = new Route((Route) secondRouteOriginal.clone());
                                int requestId = firstRouteIdSequence.get(k);
                                Route firstRoute = new Route((Route) firstRouteOriginal.clone());
                                firstRoute.removeRequest(requestId, data);

                                List<Integer> idSequenceToInsertRequest = new ArrayList<>();
                                idSequenceToInsertRequest.addAll(secondRouteOriginal.getIntegerSequenceOfAttendedRequests());

                                for (int l = 1; l < idSequenceToInsertRequest.size(); l++) {
                                    for (int m = l + 1; m < idSequenceToInsertRequest.size() + 1; m++) {
                                        List<Integer> newIdSequence = new ArrayList<>();

                                        insertIdInNewSequence(newIdSequence, idSequenceToInsertRequest, l, requestId, m);
                                        secondRoute.rebuild(newIdSequence, data);

                                        actualizeSolution(solution, i, firstRoute);
                                        actualizeSolution(solution, j, secondRoute);
                                        secondRoute.setRoute((Route) secondRouteOriginal.clone());

                                        long evaluationFunctionAfterMovement = solution.getEvaluationFunction();

                                        if (evaluationFunctionAfterMovement < bestSolution.getEvaluationFunction()) {
                                            bestSolution.setSolution((Solution) solution.clone());
//                                            System.out.println("changed request = " + requestId);
                                        }
                                        actualizeSolution(solution, i, firstRouteOriginal);
                                        actualizeSolution(solution, j, secondRoute);
                                    }
                                }
                                firstRoute.setRoute(firstRouteOriginal);
                            }
                        }
                    }
                }
            }

            if (bestSolution.getEvaluationFunction() < this.solution.getEvaluationFunction()) {
                evaluateSolution(bestSolution);
                bestSolution.removeEmptyRoutes();
                System.out.println(bestSolution);
                this.solution = (Solution) bestSolution.clone();
            } else {
                canContinue = false;
            }
        }
        return (Solution) this.solution.clone();
    }

    @Override
    public void perturbation(int typeOfPerturbation, int intensity) {
        switch (typeOfPerturbation) {
            case 1:
                this.solution = swapIntraRoutePerturbation(intensity);
                break;
            case 2:
                this.solution = swapInterRoutePerturbation(intensity);
                break;
            case 3:
                this.solution = addMinutesInSolutionSchedulePerturbation(intensity);
                break;
            case 4:
                this.solution = removeMinutesInSolutionSchedulePerturbation(intensity);
                break;
            case 5:
                this.solution = reallocateRequestPerturbation(intensity);
                break;
        }
    }

    private Solution swapIntraRoutePerturbation(int intensity) {
        Solution solution = new Solution(this.solution);

        int routeIndex = generateRouteIndex(solution);
        Route route = new Route(solution.getRoute(routeIndex));

        for (int i = 0; i < intensity; i++) {
            List<Integer> positions = generateTwoDiffentRouteRequests(solution, routeIndex);
            int firstRequestIndex = positions.get(0);
            int secondRequestIndex = positions.get(1);

            route.swapRequests(firstRequestIndex, secondRequestIndex, data);
            actualizeSolution(solution, routeIndex, route);
        }
        return solution;
    }

    private List<Integer> generateTwoDiffentRouteRequests(Solution solution, int routeIndex) {
        Random rnd = new Random();
        List<Integer> indexes = new ArrayList<>();
        int routeSize = solution.getRoute(routeIndex).getIntegerSequenceOfAttendedRequests().size();
        int firstRequest, secondRequest;
        firstRequest = rnd.nextInt(routeSize - 2) + 1;
        do {
            secondRequest = rnd.nextInt(routeSize - 2) + 1;
        } while (firstRequest == secondRequest && isTheSameRequest(solution, routeIndex, firstRequest, secondRequest));
        indexes.add(firstRequest);
        indexes.add(secondRequest);

        return indexes;
    }

    private List<Integer> generateTwoDiffentRouteRequests(Solution solution, int firstRouteIndex, int secondRouteIndex) {
        Random rnd = new Random();
        List<Integer> indexes = new ArrayList<>();
        int firstRouteSize = solution.getRoute(firstRouteIndex).getIntegerSequenceOfAttendedRequests().size();
        int secondRouteSize = solution.getRoute(secondRouteIndex).getIntegerSequenceOfAttendedRequests().size();
        int firstRequest, secondRequest;
        firstRequest = rnd.nextInt(firstRouteSize - 2) + 1;
        secondRequest = rnd.nextInt(secondRouteSize - 2) + 1;
        indexes.add(firstRequest);
        indexes.add(secondRequest);

        return indexes;
    }

    private static boolean isTheSameRequest(Solution solution1, int routeIndex, int firstRequest, int secondRequest) {
        return solution1.getRoute(routeIndex).getIntegerSequenceOfAttendedRequests().get(firstRequest)
                .equals(solution1.getRoute(routeIndex).getIntegerSequenceOfAttendedRequests().get(secondRequest));
    }

    private Solution swapInterRoutePerturbation(int intensity) {
        Solution solution = new Solution(this.solution);
        List<Integer> routeIndexes = generateTwoDiffentRouteIndexes(solution);

        int firstRoute = routeIndexes.get(0);
        int secondRoute = routeIndexes.get(1);

        List<Integer> requestIndexes = generateTwoDiffentRouteRequests(solution, firstRoute, secondRoute);
        List<Integer> firstIdSequence = solution.getRoute(firstRoute).getIntegerSequenceOfAttendedRequests();
        List<Integer> secondIdSequence = solution.getRoute(secondRoute).getIntegerSequenceOfAttendedRequests();
        int firstRequestId = firstIdSequence.get(requestIndexes.get(0));
        int secondRequestId = secondIdSequence.get(requestIndexes.get(1));

        solution.getRoute(firstRoute).replaceRequest(firstRequestId, secondRequestId, data);
        solution.getRoute(secondRoute).replaceRequest(secondRequestId, firstRequestId, data);
        evaluateSolution(solution);

        return solution;
    }

    private List<Integer> generateTwoDiffentRouteIndexes(Solution solution) {
        Random rnd = new Random();
        List<Integer> indexes = new ArrayList<>();
        int totalRoutes = solution.getNumberOfRoutes();
        int firstRoute, secondRoute;
        firstRoute = rnd.nextInt(totalRoutes);
        do {
            secondRoute = rnd.nextInt(totalRoutes);
        } while (firstRoute == secondRoute);
        indexes.add(firstRoute);
        indexes.add(secondRoute);

        return indexes;
    }

    private Integer generateRouteIndex(Solution solution) {
        return new Random().nextInt(solution.getNumberOfRoutes());
    }

    private Solution reallocateRequestPerturbation(int intensity) {
        Solution solution = new Solution(this.solution);
        for (int i = 0; i < intensity; i++) {

            List<Integer> routeIndexes = generateTwoDiffentRouteIndexes(solution);
            int firstRouteIndex = routeIndexes.get(0);
            int secondRouteIndex = routeIndexes.get(1);

            List<Integer> idSequenceToRemoveRequest = new ArrayList<>();
            List<Integer> idSequenceToInsertRequest = new ArrayList<>();

            idSequenceToRemoveRequest.addAll(solution.getRoute(firstRouteIndex).getIntegerSequenceOfAttendedRequests());
            idSequenceToInsertRequest.addAll(solution.getRoute(secondRouteIndex).getIntegerSequenceOfAttendedRequests());

            List<Integer> newIdSequence = new ArrayList<>();
            List<Integer> indexesToRemove = generateTwoDiffentRequestsToOneRoute(idSequenceToRemoveRequest);
            List<Integer> indexesToInsert = generateTwoDiffentRequestsToOneRoute(idSequenceToInsertRequest);
            int firstIndex = indexesToInsert.get(0);
            int secondIndex = indexesToInsert.get(1);
            int requestId = idSequenceToRemoveRequest.get(indexesToRemove.get(0));

            insertRequestInRoute(newIdSequence, idSequenceToInsertRequest, firstIndex, requestId, secondIndex);

            Route firstRoute = solution.getRoute(firstRouteIndex);
            Route secondRoute = solution.getRoute(secondRouteIndex);

            firstRoute.removeRequest(requestId, data);
            secondRoute.rebuild(newIdSequence, data);

            actualizeSolution(solution, firstRouteIndex, firstRoute);
            actualizeSolution(solution, secondRouteIndex, secondRoute);

            evaluateSolution(solution);
            solution.removeEmptyRoutes();
        }
        return solution;
    }

    private void insertRequestInRoute(List<Integer> newIdSequence, List<Integer> idSequenceToInsertRequest, int firstIndex, int requestId, int secondIndex) {
        try {
            newIdSequence.addAll(idSequenceToInsertRequest.subList(0, firstIndex));
            newIdSequence.add(requestId);
            newIdSequence.addAll(idSequenceToInsertRequest.subList(firstIndex, secondIndex - 1));
            newIdSequence.add(requestId);
            newIdSequence.addAll(idSequenceToInsertRequest.subList(secondIndex - 1, idSequenceToInsertRequest.size()));
        } catch (IllegalArgumentException e) {
            System.out.println(firstIndex + "\t" + secondIndex);
            System.out.println(newIdSequence);
            e.printStackTrace();
        }
    }

    private List<Integer> generateTwoDiffentRequestsToOneRoute(List<Integer> idSequence) {
        Random rnd = new Random();
        List<Integer> indexes = new ArrayList<>();
        int routeSize = idSequence.size();
        int firstRequest, secondRequest;
        do {
            if (idSequence.get(0) == 0 && idSequence.get(routeSize - 1) == 0) {
                firstRequest = rnd.nextInt(routeSize - 1) + 1;
                secondRequest = rnd.nextInt(routeSize - 1) + 1;
            } else {
                firstRequest = rnd.nextInt(routeSize);
                secondRequest = rnd.nextInt(routeSize);
            }
        } while (firstRequest == secondRequest);
        indexes.add(firstRequest);
        indexes.add(secondRequest);
        Collections.sort(indexes);

        return indexes;
    }

    public Solution addMinutesInSolutionSchedulePerturbation(int intensity) {
        Solution solution = new Solution(this.solution);
        Random rnd = new Random();
        if (solution.getNumberOfRoutes() <= 0) {
            int k = 0;
        }
        int routeIndex = rnd.nextInt(solution.getNumberOfRoutes());
        int timeInterval = rnd.nextInt(6);
        Route route = new Route(solution.getRoute(routeIndex));

        route.addMinutesInRoute(timeInterval, data);
        actualizeSolution(this.solution, routeIndex, route);

        return this.solution;
    }

    public Solution removeMinutesInSolutionSchedulePerturbation(int intensity) {
        Solution solution = new Solution(this.solution);
        Random rnd = new Random();
        if (solution.getNumberOfRoutes() <= 0) {
            int k = 0;
        }
        int routeIndex = rnd.nextInt(solution.getNumberOfRoutes());
        int timeInterval = rnd.nextInt(6);
        Route route = new Route(solution.getRoute(routeIndex));

        route.removeMinutesInRoute(timeInterval, data);
        actualizeSolution(this.solution, routeIndex, route);

        return this.solution;
    }

    private boolean isRouteForOnlyOneRequest(List<Integer> idSequence) {
        int sequenceSize = idSequence.size();
        if (sequenceSize == 4 && idSequence.get(0) == 0 && idSequence.get(sequenceSize - 1) == 0) {
            return true;
        } else if (sequenceSize == 1) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void multiStart() {
        int numberOfIterations = 100;
        int currentIteration = 0;
        Solution initialSolution = new Solution();
        while (currentIteration < numberOfIterations) {
            buildRandomSolution();
            initialSolution.setSolution(this.getSolution());
            vnd();
            keepBestSolutionFound(initialSolution);
            currentIteration++;
        }
        System.out.println("final solution");
        System.out.println(initialSolution);
        initialSolution.printAllInformations();
    }

    public void multiStartForExperiment() {
        int numberOfExecutions = 30;
        int numberOfIterations = 100;
        String algorithmName = "MultiStart";
        Solution bestSolutionFound = new Solution();
        DataOutput outputForBestSolutions = new DataOutput(algorithmName, instanceName);
        for (int execution = 0; execution < numberOfExecutions; execution++) {

            DataOutput output = new DataOutput(algorithmName, instanceName, execution);
            int currentIteration = 0;
            Solution initialSolution = new Solution();
            Solution bestSolution = new Solution();
            buildRandomSolution();
            bestSolution.setSolution(this.getSolution());
            //System.out.println("initial solution " + bestSolution);
            while (currentIteration < numberOfIterations) {
                initialSolution.setSolution(this.getSolution());
//                vnd();
                localSearch(1);
                keepBestSolutionFound(initialSolution);
                if (bestSolution.getEvaluationFunction() > solution.getEvaluationFunction()) {
                    bestSolution.setSolution(solution);
                    bestSolutionFound.setSolution(solution);
                }

                output.saveBestSolutionInTxtFile(bestSolutionFound, currentIteration);//alterei aqui
                currentIteration++;
                buildRandomSolution();
            }
//            outputForBestSolutions.saveBestSolutionFoundInTxtFile(initialSolution);
            outputForBestSolutions.saveBestSolutionFoundInTxtFile(bestSolutionFound);
            //System.out.println("final solution");
//            System.out.println(initialSolution);
            System.out.println(bestSolutionFound);
            //bestSolutionFound.printAllInformations();
        }

    }

    @Override
    public void simulatedAnnealing() {
        buildGreedySolution();
        Solution bestSolution = new Solution(solution);
        Solution bestSolutionFound = new Solution(solution);
        int numberOfIterations = 100;
        int numberOfMovements = 4;
        int currentIteration = 0;
        int initialTemperature = 70000; // worst objective function in 10 random neighborhoods
        double currentTemperature = initialTemperature;
        double alpha = 0.70;
        Random rnd = new Random();

        while (currentTemperature > 0.01) {
            while (currentIteration < numberOfIterations) {
                currentIteration++;
                Solution solutionBefore = new Solution(solution);
                generateRandomNeighborhood(rnd, numberOfMovements);
                long delta = solution.getEvaluationFunction() - solutionBefore.getEvaluationFunction();
                //vnd();
                localSearch(2);
                System.out.println(solution);
                keepBestSolutionFound(bestSolutionFound);

                if (delta < 0) {
                    solutionBefore.setSolution(solution);
                    if (solutionBefore.getEvaluationFunction() < bestSolution.getEvaluationFunction()) {
                        bestSolution.setSolution(solution);
                    }

                } else {
                    double x = rnd.nextDouble();
                    if (x < Math.pow(Math.E, (-delta / currentTemperature))) {
                        solutionBefore.setSolution(solution);
                    }
                }
            }
            currentTemperature = alpha * currentTemperature;
            currentIteration = 0;
        }
        System.out.println("Best Solution Found");
        bestSolutionFound.printAllInformations();
    }

    public void simulatedAnnealingForExperiment() {
        int numberOfExecutions = 30;
        int numberOfIterations = 100;
        int numberOfMovements = 4;
        String algorithmName = "SimulatedAnnealing";
        DataOutput outputForBestSolutions = new DataOutput(algorithmName, instanceName);
        for (int execution = 0; execution < numberOfExecutions; execution++) {
            refreshInstanceData();
            buildGreedySolution();
            Solution bestSolution = new Solution(solution);
            Solution bestSolutionFound = new Solution(solution);
            int currentIteration = 0;
            int initialTemperature = 50000; // worst objective function in 10 random neighborhoods
            double currentTemperature = initialTemperature;
            double alpha = 0.70;
            Random rnd = new Random();
            DataOutput output = new DataOutput(algorithmName, instanceName, execution);

            while (currentTemperature > 0.01) {
                while (currentIteration < numberOfIterations) {
                    currentIteration++;
                    Solution solutionBefore = new Solution(solution);
                    generateRandomNeighborhood(rnd, numberOfMovements);
                    long delta = solution.getEvaluationFunction() - solutionBefore.getEvaluationFunction();
                    localSearch(1);
                    //localSearch(2);
                    //vnd();
                    //System.out.println(currentTemperature);
                    keepBestSolutionFound(bestSolutionFound);

                    if (delta < 0) {
                        solutionBefore.setSolution(solution);
                        if (solutionBefore.getEvaluationFunction() < bestSolution.getEvaluationFunction()) {
                            bestSolution.setSolution(solution);
                        }

                    } else {
                        double x = rnd.nextDouble();
                        if (x < Math.pow(Math.E, (-delta / currentTemperature))) {
                            solutionBefore.setSolution(solution);
                        }
                    }
                }
                currentTemperature = alpha * currentTemperature;
                currentIteration = 0;
                output.saveBestSolutionInTxtFile(bestSolution, currentIteration);

            }
            outputForBestSolutions.saveBestSolutionFoundInTxtFile(bestSolution);
            System.out.println("Best Solution Found");
            System.out.println(bestSolutionFound);
        }
    }

    private void refreshInstanceData() {
        if (this.excelDataFilesPath != null) {
            this.readExcelInstance();
        } else {
            this.readInstance();
        }
    }

    private void keepBestSolutionFound(Solution bestSolutionFound) {
        if (bestSolutionFound.getEvaluationFunction() > solution.getEvaluationFunction()) {
            bestSolutionFound.setSolution(solution);
        }
    }

    private void generateRandomNeighborhood(Random rnd, int numberOfMovements) {
        int type = rnd.nextInt(numberOfMovements) + 1;
        int intensity = rnd.nextInt(3) + 1;
        perturbation(type, intensity);
    }

    @Override
    public void vnd() {
        //buildGreedySolution();
        Solution initialSolution = new Solution(this.getSolution());
        int numberOfNeighborhoods = 6;
        List<Integer> neighborhoods = generateNeighborhoodList(numberOfNeighborhoods, localSearchType);
        int currentIndex = 0;
        int currentNeighborhood = neighborhoods.get(currentIndex);
        int lastNeighborhood = neighborhoods.get(neighborhoods.size() - 1);
        while (currentNeighborhood <= lastNeighborhood) {
            localSearch(currentNeighborhood);
            if (solution.getEvaluationFunction() < initialSolution.getEvaluationFunction()) {
                initialSolution.setSolution(solution);
                currentIndex = 0;
                currentNeighborhood = neighborhoods.get(0);
            } else {
                currentNeighborhood = currentNeighborhood + 2;
            }
        }
        solution.setSolution(initialSolution);
    }

    public void vndForLocalSearchInVNS(Integer excludedNeighborhood) {
        Solution initialSolution = new Solution(this.getSolution());
        int numberOfNeighborhoods = 8;
        List<Integer> neighborhoods = generateNeighborhoodListWithout(numberOfNeighborhoods, localSearchType, excludedNeighborhood);
        int currentIndex = 0;
        int currentNeighborhood = neighborhoods.get(currentIndex);
        int lastNeighborhood = neighborhoods.get(neighborhoods.size() - 1);
        while (currentNeighborhood <= lastNeighborhood) {
            localSearch(currentNeighborhood);
            if (solution.getEvaluationFunction() < initialSolution.getEvaluationFunction()) {
                initialSolution.setSolution(solution);
                currentIndex = 0;
                currentNeighborhood = neighborhoods.get(0);
                break;
            } else {
                currentNeighborhood = currentNeighborhood + 2;
            }
        }

        solution.setSolution(initialSolution);
    }

    public Solution vndForLocalSearchInVnsFirstImprovement(Integer excludedNeighborhood) {
        Solution initialSolution = new Solution(this.getSolution());
        int numberOfNeighborhoods = 8;
        List<Integer> neighborhoods = generateNeighborhoodListWithout(numberOfNeighborhoods, localSearchType, excludedNeighborhood);
        int currentIndex = 0;
        int currentNeighborhood = neighborhoods.get(currentIndex);
        int lastNeighborhood = neighborhoods.get(neighborhoods.size() - 1);
        while (currentNeighborhood <= lastNeighborhood) {
            localSearch(currentNeighborhood);
            if (solution.getEvaluationFunction() < initialSolution.getEvaluationFunction()) {
                initialSolution.setSolution(solution);
                return initialSolution;
            } else {
                currentNeighborhood = currentNeighborhood + 2;
            }
        }

        solution.setSolution(initialSolution);
        return solution;
    }

    private void printAlgorithmInformations(Solution solution, int currentNeighborhood) {
        System.out.println("Objective Function = " + solution.getEvaluationFunction() + "\t Neighborhood = " + currentNeighborhood);
    }

    private List<Integer> generateNeighborhoodList(int numberOfNeighborhoods, int localSearchType) {
        List<Integer> neighborhoods = new ArrayList<>();
        for (int i = localSearchType; i <= numberOfNeighborhoods; i = i + 2) {
            if (i != 4) {
                neighborhoods.add(i);
            }

        }
        return neighborhoods;
    }

    private List<Integer> generateNeighborhoodListWithout(int numberOfNeighborhoods, int localSearchType, int excluded) {
        List<Integer> neighborhoods = new ArrayList<>();
        for (int i = localSearchType; i <= numberOfNeighborhoods; i = i + 2) {
            if (i != excluded) {
                neighborhoods.add(i);
            }

        }
        return neighborhoods;
    }

    private List<Integer> generateNeighborhoodList(int numberOfNeighborhoods, int localSearchType, Integer excluded) {
        List<Integer> neighborhoods = new ArrayList<>();
        for (int i = localSearchType; i <= numberOfNeighborhoods; i = i + 2) {
            neighborhoods.add(i);
        }
        neighborhoods.remove(excluded);
        return neighborhoods;
    }

    @Override
    public void vns() {
        int numberOfIterations = 100;
        int currentIteration = 0;
        int numberOfNeighborhoods = 8;
        int excludedNeighborhood = 4;
        int currentNeighborhood;
        List<Integer> neighborhoods = generateNeighborhoodList(numberOfNeighborhoods, localSearchType, excludedNeighborhood);
        int lastNeighborhood = neighborhoods.get(neighborhoods.size() - 1);
        buildGreedySolution();
        Solution bestSolution = new Solution(solution);
        while (currentIteration < numberOfIterations) {
            currentNeighborhood = 1;

            //System.out.println(solution.getIntegerRepresentation());
            while (currentNeighborhood <= lastNeighborhood) {
                perturbation(2, 2);
                vndForLocalSearchInVNS(excludedNeighborhood);
                //VND();
                //System.out.println(solution);
                if (bestSolution.getEvaluationFunction() > solution.getEvaluationFunction()) {
                    bestSolution.setSolution(solution);
                    currentNeighborhood = 1;
                } else {
                    currentNeighborhood = currentNeighborhood + 2;
                }
            }
            currentIteration++;
        }
        System.out.println();
        System.out.println(bestSolution);
        this.solution.setSolution(bestSolution);
        bestSolution.printAllInformations();
    }

    public void vnsForExperiment() {
        int numberOfExecutions = 30;
        int numberOfIterations = 100;
        String algorithmName = "VNS";
        DataOutput outputForBestSolutions = new DataOutput(algorithmName, instanceName);

        int numberOfNeighborhoods = 6;
        int excludedNeighborhood = 4;
        int currentNeighborhood;
        List<Integer> neighborhoods = generateNeighborhoodList(numberOfNeighborhoods, localSearchType, excludedNeighborhood);
        int lastNeighborhood = neighborhoods.get(neighborhoods.size() - 1);

        for (int execution = 0; execution < numberOfExecutions; execution++) {
            refreshInstanceData();
            int currentIteration = 0;
            DataOutput output = new DataOutput(algorithmName, instanceName, execution);
            buildGreedySolution();
            Solution bestSolution = new Solution(solution);
            Solution bestSolutionFound = new Solution();
            while (currentIteration < numberOfIterations) {
                currentNeighborhood = 1;

                while (currentNeighborhood <= lastNeighborhood) {
                    perturbation(2, 1);
                    //solution.setSolution(vndForLocalSearchInVnsFirstImprovement(excludedNeighborhood));
                    vndForLocalSearchInVNS(excludedNeighborhood);
                    if (bestSolution.getEvaluationFunction() > solution.getEvaluationFunction()) {
                        bestSolution.setSolution(solution);
                        bestSolutionFound.setSolution(solution);
                        System.out.println(bestSolution);
                        currentNeighborhood = 1;
                    } else {
                        currentNeighborhood++;
                    }

                }
                output.saveBestSolutionInTxtFile(bestSolutionFound, currentIteration);
                currentIteration++;
            }
            outputForBestSolutions.saveBestSolutionFoundInTxtFile(bestSolution);
        }
    }
    
    public void vnsTest() {
        int numberOfExecutions = 30;
        int numberOfIterations = 100;
        String algorithmName = "VNS";
        DataOutput outputForBestSolutions = new DataOutput(algorithmName, instanceName);

        int numberOfNeighborhoods = 6;
        int excludedNeighborhood = 4;
        int currentNeighborhood;
        List<Integer> neighborhoods = generateNeighborhoodList(numberOfNeighborhoods, localSearchType, excludedNeighborhood);
        int lastNeighborhood = neighborhoods.get(neighborhoods.size() - 1);

        for (int execution = 0; execution < numberOfExecutions; execution++) {
            refreshInstanceData();
            int currentIteration = 0;
            DataOutput output = new DataOutput(algorithmName, instanceName, execution);
            buildGreedySolution();
            Solution bestSolution = new Solution(solution);
            Solution bestSolutionFound = new Solution();
            while (currentIteration < numberOfIterations) {
                currentNeighborhood = 1;

                while (currentNeighborhood <= lastNeighborhood) {
                    perturbation(2, 1);
                    //solution.setSolution(vndForLocalSearchInVnsFirstImprovement(excludedNeighborhood));
                    vndForLocalSearchInVNS(excludedNeighborhood);
                    if (bestSolution.getEvaluationFunction() > solution.getEvaluationFunction()) {
                        bestSolution.setSolution(solution);
                        bestSolutionFound.setSolution(solution);
                        System.out.println(bestSolution);
                        currentNeighborhood = 1;
                    } else {
                        currentNeighborhood++;
                    }
                }
                output.saveBestSolutionInTxtFile(bestSolutionFound, currentIteration);
                currentIteration++;
            }
            outputForBestSolutions.saveBestSolutionFoundInTxtFile(bestSolution);
        }
    }

    @Override
    public void grasp() {

    }

    @Override
    public void ils() {

    }

    @Override
    public void tabuSearch() {

    }
}
