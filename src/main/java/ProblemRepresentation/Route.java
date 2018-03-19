package ProblemRepresentation;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author renansantos - The Route Class represents a vehicle route for the
 * problem
 */
public class Route implements Cloneable {

    private long totalDistanceTraveled;
    private long routeTravelTime;
    private long totalTimeWindowAnticipation;
    private long totalTimeWindowDelay;
    private long evaluationFunction;
    private Set<Request> notServedRequests;
    private List<Node> nodesSequence;
    private List<Request> sequenceOfAttendedRequests;
    private List<Integer> integerRouteRepresetation;
    private boolean violatedSomeConstraint = false;

    public Route(long totalRouteDistance, long routeTravelTime, long totalTimeWindowAnticipation, long totalTimeWindowDelay,
            long evaluationFunction, Set<Request> notServedRequests, List<Node> nodesSequence,
            List<Request> sequenceOfServedRequests, List<Integer> integerRouteRepresetation, boolean violatedSomeConstraint) {
        this.totalDistanceTraveled = totalRouteDistance;
        this.routeTravelTime = routeTravelTime;
        this.totalTimeWindowAnticipation = totalTimeWindowAnticipation;
        this.totalTimeWindowDelay = totalTimeWindowDelay;
        this.evaluationFunction = evaluationFunction;
        this.notServedRequests = notServedRequests;
        this.nodesSequence = nodesSequence;
        this.sequenceOfAttendedRequests = sequenceOfServedRequests;
        this.integerRouteRepresetation = integerRouteRepresetation;
        this.violatedSomeConstraint = violatedSomeConstraint;
    }

    public Route() {
        initializeAttributes();
    }

    private void initializeAttributes() {
        this.notServedRequests = new HashSet<>();
        this.nodesSequence = new ArrayList<>();
        this.sequenceOfAttendedRequests = new ArrayList<>();
        this.integerRouteRepresetation = new ArrayList<>();
    }

    public Route(Route route) {
        initializeAttributes();
        this.totalDistanceTraveled = route.getTotalRouteDistance();
        this.routeTravelTime = route.getRouteTravelTime();
        this.totalTimeWindowAnticipation = route.getTotalTimeWindowAnticipation();
        this.totalTimeWindowDelay = route.getTotalTimeWindowDelay();
        this.evaluationFunction = route.getEvaluationFunction();
        this.notServedRequests = route.getNotServedRequests();
        this.nodesSequence = route.getNodesSequence();
        this.sequenceOfAttendedRequests = route.getSequenceOfAttendedRequests();
        //this.integerRouteRepresetation = route.getIntegerRouteRepresetation();
        this.integerRouteRepresetation.addAll(new ArrayList<>(route.getIntegerRouteRepresetation()));
    }

    public void setRoute(Route route) {
        this.totalDistanceTraveled = route.getTotalRouteDistance();
        this.routeTravelTime = route.getRouteTravelTime();
        this.totalTimeWindowAnticipation = route.getTotalTimeWindowAnticipation();
        this.totalTimeWindowDelay = route.getTotalTimeWindowDelay();
        this.evaluationFunction = route.getEvaluationFunction();
        this.notServedRequests = route.getNotServedRequests();
        this.nodesSequence = route.getNodesSequence();
        this.sequenceOfAttendedRequests = route.getSequenceOfAttendedRequests();
        this.integerRouteRepresetation = route.getIntegerRouteRepresetation();
    }

    public long getTotalRouteDistance() {
        return totalDistanceTraveled;
    }

    public void setTotalDistanceTraveled(long totalDistanceTraveled) {
        this.totalDistanceTraveled = totalDistanceTraveled;
    }

    public long getEvaluationFunction() {
        return evaluationFunction;
    }

    public void setEvaluationFunction(long evaluationFunction) {
        this.evaluationFunction = evaluationFunction;
    }

    public long getRouteTravelTime() {
        return routeTravelTime;
    }

    public void setRouteTravelTime(int routeTravelTime) {
        this.routeTravelTime = routeTravelTime;
    }

    public long getTotalTimeWindowAnticipation() {
        return totalTimeWindowAnticipation;
    }

    public void setTotalTimeWindowAnticipation(long totalTimeWindowAnticipation) {
        this.totalTimeWindowAnticipation = totalTimeWindowAnticipation;
    }

    public long getTotalTimeWindowDelay() {
        return totalTimeWindowDelay;
    }

    public void setTotalTimeWindowDelay(long totalTimeWindowAnticipation) {
        this.totalTimeWindowAnticipation = totalTimeWindowAnticipation;
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

    public List<Integer> getIntegerSequenceOfAttendedRequests() {
        return this.getIntegerRouteRepresetation()
                .stream()
                .filter(u -> u.intValue() >= 0)
                .collect(Collectors.toCollection(ArrayList::new));
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

    public void buildSequenceOfAttendedRequests(ProblemData data) {
        List<Integer> idSequence = new ArrayList<>();
        idSequence = this.integerRouteRepresetation.stream().filter(u -> u.longValue() > 0)
                .collect(Collectors.toCollection(ArrayList::new));

        if (this.sequenceOfAttendedRequests == null) {
            this.sequenceOfAttendedRequests = new ArrayList<>();
        }
        this.sequenceOfAttendedRequests.clear();

        for (Integer id : idSequence) {
            Request request = data.getRequests().stream().filter(u -> u.getId().equals(id)).findAny().get();
            this.sequenceOfAttendedRequests.add(request);
        }

    }

    public void buildNodesSequence(ProblemData data) {
        List<Integer> idSequence = new ArrayList<>();
        idSequence = this.integerRouteRepresetation.stream().filter(u -> u.longValue() >= 0)
                .collect(Collectors.toCollection(ArrayList::new));

        if (this.nodesSequence == null) {
            this.nodesSequence = new ArrayList<>();
        }
        this.nodesSequence.clear();

        Set<Integer> idCrossed = new HashSet<>();
        int currentPosition = 0;
        for (int i = 0; i < idSequence.size(); i++) {
            Integer id = idSequence.get(i);
            if (!id.equals(0)) {
                Request passenger = null;
                passenger = findRequestWithIdentification(data, id, passenger);

                if (idCrossed.contains(id)) {
                    if (!this.nodesSequence.get(currentPosition - 1).getId().equals(passenger.getDestination().getId())) {
                        this.nodesSequence.add(passenger.getDestination());
                        currentPosition++;
                    }

                } else if (!this.nodesSequence.get(currentPosition - 1).getId().equals(passenger.getOrigin().getId())) {
                    this.nodesSequence.add(passenger.getOrigin());
                    currentPosition++;
                }
                idCrossed.add(id);
            } else {
                this.nodesSequence.add(data.getNodes().get(0));
                currentPosition++;
            }
        }
    }

    private Request findRequestWithIdentification(ProblemData data, Integer id, Request passenger) {
        for (Request request : data.getRequests()) {
            if (request.getId().equals(id)) {
                passenger = request;
            }
        }
        return passenger;
    }

    public void evaluateRoute(ProblemData data) {
        if (this.integerRouteRepresetation.size() > 2) {
            calculateTravelTime(data);
            calculateDistanceTraveled(data);
            calculateTotalDeliveryAnticipation(data);
            calculateTotalDeliveryDelay();
            calculateEvaluationFunction();
            if (this.violatedSomeConstraint) {
                penalizeRoute();
            }
        } else {
            clearAtributes();
        }
    }

    private void clearAtributes() {
        this.totalDistanceTraveled = 0;
        this.routeTravelTime = 0;
        this.totalTimeWindowAnticipation = 0;
        this.totalTimeWindowDelay = 0;
        this.evaluationFunction = 0;
        this.notServedRequests = new HashSet();
        this.nodesSequence = new ArrayList();
        this.sequenceOfAttendedRequests = new ArrayList();
        //this.integerRouteRepresetation = null;
    }

    public void calculateTravelTime(ProblemData data) {
        long totalTravelTime = 0;
        for (int i = 0; i < this.nodesSequence.size() - 1; i++) {
            totalTravelTime += data.getDuration()[this.nodesSequence.get(i).getId()][this.nodesSequence.get(i + 1).getId()].getSeconds();
        }

        this.routeTravelTime = totalTravelTime / 60;
    }

    public void calculateDistanceTraveled(ProblemData data) {
        long totalDistance = 0;
        for (int i = 0; i < this.nodesSequence.size() - 1; i++) {
            totalDistance += data.getDistance()[this.nodesSequence.get(i).getId()][this.nodesSequence.get(i + 1).getId()];
        }

        this.totalDistanceTraveled = totalDistance / 1000;
    }

    public void calculateTotalDeliveryAnticipation(ProblemData data) {
        Duration violations = Duration.ofMinutes(0);
        long totalSumTime = 0;
        Set<Request> attendedRequests = new HashSet<>();
        List<Integer> ids = returnUsedIds();
        for (Integer id : ids) {
            attendedRequests.add(getRequestUsingId(id, data));
        }
//        for (Request request : this.sequenceOfAttendedRequests) {//treta aqui
//            attendedRequests.add(request);
//        }

        for (Request request : attendedRequests) {
            if (request.getAnticipation().getSeconds() > 0) {
                totalSumTime += request.getAnticipation().getSeconds() / 60;
            }
        }
        this.totalTimeWindowAnticipation = totalSumTime;
    }

    private List<Integer> returnUsedIds() {
        Set<Integer> setOfIds = this.getIntegerRouteRepresetation()
                .stream()
                .filter(u -> u.intValue() > 0)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        List<Integer> idsUsed = new ArrayList<>();
        for (int id : setOfIds) {
            idsUsed.add(id);
        }
        return idsUsed;
    }

    public void calculateTotalDeliveryDelay() {
        Duration violations = Duration.ofMinutes(0);
        Set<Request> attendedRequests = new HashSet<>();
        for (Request request : this.sequenceOfAttendedRequests) {
            attendedRequests.add(request);
        }

        for (Request request : attendedRequests) {
            if (request.getDeliveryTimeWindowUpper().isBefore(request.getDeliveryTime())) {
                Duration time = Duration.between(request.getDeliveryTimeWindowUpper(), request.getDeliveryTime());
                violations = violations.plus(time);
            }
        }
        this.totalTimeWindowDelay = violations.getSeconds() / 60;
    }

    public void calculateEvaluationFunction() {
        if (this.totalTimeWindowDelay > 0) {
            this.evaluationFunction = this.totalDistanceTraveled + this.routeTravelTime * this.totalTimeWindowDelay
                    + this.totalTimeWindowAnticipation;
        } else {
            this.evaluationFunction = this.totalDistanceTraveled + this.routeTravelTime + this.totalTimeWindowAnticipation;
        }

    }

    public List<Integer> getNodesVisitationInIntegerRepresentation() {
        List<Integer> nodesSequence = new ArrayList<>();

        for (Node node : this.nodesSequence) {
            nodesSequence.add(node.getId());
        }
        return nodesSequence;
    }

    public void clearIntegerRepresentation() {
        this.integerRouteRepresetation.clear();
    }

    public void clearNodesSequence() {
        this.nodesSequence.clear();
    }

    public void clearSequenceOfAttendeRequests() {
        this.sequenceOfAttendedRequests.clear();
    }

    public void setPickupAndDeliveryTimeForEachAttendedRequest(ProblemData data) {
        Set<Integer> visitedIds = new HashSet<>();
        for (int i = 0; i < this.integerRouteRepresetation.size(); i += 2) {
            if (this.integerRouteRepresetation.get(i) != 0) {
                if (visitedIds.contains(this.integerRouteRepresetation.get(i))) {
                    Request request = getRequestUsingId(this.integerRouteRepresetation.get(i), data);
                    request.setDeliveryTime(this.integerRouteRepresetation.get(i + 1));
                } else {
                    Request request = getRequestUsingId(this.integerRouteRepresetation.get(i), data);
                    request.setPickUpTime(this.integerRouteRepresetation.get(i + 1));
                }
                visitedIds.add(this.integerRouteRepresetation.get(i));
            }
        }
    }

    public Request getRequestUsingId(Integer id, ProblemData data) {
        if (id != 0) {
            return data.getRequests().stream().filter(u -> u.getId().equals(id)).findAny().get();
        } else {
            return null;
        }
    }

    public void swapRequests(int firstPosition, int secondPosition, ProblemData data) {
        List<Integer> idSequence = this.integerRouteRepresetation
                .stream()
                .filter(u -> u >= 0)
                .collect(Collectors.toCollection(ArrayList::new));

        if ((idSequence.get(firstPosition) != 0) && (idSequence.get(secondPosition) != 0)) {
            if (!idSequence.get(firstPosition).equals(idSequence.get(secondPosition))) {
                Collections.swap(idSequence, firstPosition, secondPosition);
                clear();
                rebuild(idSequence, data);
            }
        }
    }

    public void rebuild(List<Integer> idSequence, ProblemData data) {
        this.setIntegerRouteRepresetation(idSequence);
        this.scheduleRouteUsingBestScheduling(data);
        this.buildSequenceOfAttendedRequests(data);
        this.buildNodesSequence(data);
        this.evaluateRoute(data);
    }

    public void clear() {
        this.clearIntegerRepresentation();
        this.clearNodesSequence();
        this.clearSequenceOfAttendeRequests();
    }

    public void addMinutesInRoute(int timeInterval, ProblemData data) {
        for (int i = 0; i < this.integerRouteRepresetation.size(); i++) {
            if (this.integerRouteRepresetation.get(i) < 0) {
                this.integerRouteRepresetation.set(i, this.integerRouteRepresetation.get(i) - timeInterval);
            }
        }
        List<Request> attendedRequests = new ArrayList<>();
        List<Integer> ids = returnUsedIds();
        for (Integer id : ids) {
            attendedRequests.add(getRequestUsingId(id, data));
        }

        for (Request request : attendedRequests) {
            request.setPickUpTime(request.getPickUpTimeInMinutes() + timeInterval);
            request.setDeliveryTime(request.getDeliveryTimeInMinutes() + timeInterval);
            int a = 0;
        }

        this.evaluateRoute(data);
    }

    public void removeMinutesInRoute(int timeInterval, ProblemData data) {
        for (int i = 0; i < this.integerRouteRepresetation.size(); i++) {
            if (this.integerRouteRepresetation.get(i) < 0) {
                this.integerRouteRepresetation.set(i, this.integerRouteRepresetation.get(i) + timeInterval);
            }
        }

        List<Request> attendedRequests = new ArrayList<>();
        List<Integer> ids = returnUsedIds();
        for (Integer id : ids) {
            attendedRequests.add(getRequestUsingId(id, data));
        }

        for (Request request : attendedRequests) {
            request.setPickUpTime(request.getPickUpTimeInMinutes() - timeInterval);
            request.setDeliveryTime(request.getDeliveryTimeInMinutes() - timeInterval);
            int a = 0;
        }
        this.evaluateRoute(data);
    }

    public void scheduleRoute(ProblemData data) {
        List<Integer> deliveryIdSequence = getOnlyIdSequence();
        List<Integer> pickupIdSequence = getOnlyIdSequence();
        List<Integer> idSequence = new ArrayList<>();
        Set<Integer> visitedIds = new HashSet<>();
        int positionInSequenceOfFirstDelivery = 0;

        idSequence.addAll(pickupIdSequence);

        positionInSequenceOfFirstDelivery = findFirstDelivery(idSequence, visitedIds, positionInSequenceOfFirstDelivery);

        deliveryIdSequence.subList(0, positionInSequenceOfFirstDelivery).clear();
        pickupIdSequence.subList(positionInSequenceOfFirstDelivery, pickupIdSequence.size()).clear();

        List<Integer> deliveryTimes = new ArrayList<>();
        List<Integer> pickupTimes = new ArrayList<>();

        int currentTimeForDelivery = getRequestUsingId(idSequence.get(positionInSequenceOfFirstDelivery), data)
                .getDeliveryTimeWindowLowerInMinutes();
        int currentTimeForPickup = -currentTimeForDelivery;
        deliveryTimes.add(-currentTimeForDelivery);

        currentTimeForDelivery = scheludePassengerDeliveries(positionInSequenceOfFirstDelivery,
                idSequence, visitedIds, deliveryTimes, currentTimeForDelivery, data);

        addDepotInPickupAndDeliverySequences(deliveryIdSequence, pickupIdSequence);

        int timeBetween = (int) data.getDuration()[getRequestUsingId(idSequence.get(idSequence.size() - 1), data).getDestination().getId()][0]
                .getSeconds() / 60;
        deliveryTimes.add(-currentTimeForDelivery - timeBetween);

        List<Integer> times = schedulePassengerPickups(pickupIdSequence, idSequence, positionInSequenceOfFirstDelivery,
                currentTimeForPickup, deliveryTimes, data);

        addDepotInPickupAndDeliverySequences(idSequence, idSequence);
        setIntegerRepresentation(idSequence, times, data);
        capacityAnalysis(data);
        improveSchedule(data);
    }

    public void scheduleRouteUsingBestScheduling(ProblemData data) {
        List<Integer> deliveryIdSequence = getOnlyIdSequence();
        List<Integer> pickupIdSequence = getOnlyIdSequence();
        List<Integer> idSequence = new ArrayList<>();
        Set<Integer> visitedIds = new HashSet<>();
        int positionInSequenceOfFirstDelivery = 0;

        idSequence.addAll(pickupIdSequence);

        positionInSequenceOfFirstDelivery = findFirstDelivery(idSequence, visitedIds, positionInSequenceOfFirstDelivery);

        deliveryIdSequence.subList(0, positionInSequenceOfFirstDelivery).clear();
        pickupIdSequence.subList(positionInSequenceOfFirstDelivery, pickupIdSequence.size()).clear();

        List<Integer> deliveryTimes = new ArrayList<>();
        List<Integer> pickupTimes = new ArrayList<>();

        int currentTimeForDelivery = getRequestUsingId(idSequence.get(positionInSequenceOfFirstDelivery), data)
                .getDeliveryTimeWindowLowerInMinutes();
        deliveryTimes.add(-currentTimeForDelivery);
//        currentTimeForDelivery = bestScheludePassengerDeliveries(positionInSequenceOfFirstDelivery,
//                idSequence, visitedIds, deliveryTimes, currentTimeForDelivery, data);
        currentTimeForDelivery = scheludePassengerDeliveries(positionInSequenceOfFirstDelivery,
                idSequence, visitedIds, deliveryTimes, currentTimeForDelivery, data);
        int currentTimeForPickup = deliveryTimes.get(0);
        deliveryTimes.add(-currentTimeForDelivery);
        addDepotInPickupAndDeliverySequences(deliveryIdSequence, pickupIdSequence);

        int timeBetween = (int) data.getDuration()[getRequestUsingId(idSequence.get(idSequence.size() - 1), data).getDestination().getId()][0]
                .getSeconds() / 60;
        deliveryTimes.add(-currentTimeForDelivery - timeBetween);

        List<Integer> times = schedulePassengerPickups(pickupIdSequence, idSequence, positionInSequenceOfFirstDelivery,
                currentTimeForPickup, deliveryTimes, data);

        addDepotInPickupAndDeliverySequences(idSequence, idSequence);
        setIntegerRepresentation(idSequence, times, data);
        capacityAnalysis(data);

        improveSchedule(data);

    }

    public void improveSchedule(ProblemData data) {
        Set<Integer> times = new HashSet<>();

        for (Request request : sequenceOfAttendedRequests) {
            if (!request.getAnticipation().isNegative() && !request.getAnticipation().isZero()) {
                times.add((int) request.getAnticipation().getSeconds() / 60);
            }

            if (request.getDelay().isNegative()) {
                times.add((int) -request.getDelay().getSeconds() / 60);
            }
        }
        try {
            tryToInsertTimesInRoute(times, data);
        } catch (DateTimeException e) {
            e.printStackTrace();
            System.out.println("List of times = " + times);
        }
    }

    private void tryToInsertTimesInRoute(Set<Integer> times, ProblemData data) {
        Route route = new Route((Route) this.clone());
        Route bestRoute = new Route((Route) this.clone());
        long evaluationBefore = route.getEvaluationFunction();
        for (Integer time : times) {
            route.addMinutesInRoute(time, data);
            if (bestRoute.getEvaluationFunction() > route.getEvaluationFunction()) {
                bestRoute.setRoute((Route) route.clone());
            }
            route.removeMinutesInRoute(time, data);
        }
        this.setRoute((Route) bestRoute.clone());
    }

    private List<Integer> getOnlyIdSequence() {
        List<Integer> idSequence = this.getIntegerRouteRepresetation()
                .stream().filter(u -> u.intValue() > 0)
                .collect(Collectors.toCollection(ArrayList::new));
        return idSequence;
    }

    private int findFirstDelivery(List<Integer> idSequence, Set<Integer> visitedIds, int positionInSequenceOfFirstDelivery) {
        for (int i = 0; i < idSequence.size(); i++) {
            int id = idSequence.get(i);
            if (visitedIds.contains(id)) {
                positionInSequenceOfFirstDelivery = i;
                break;
            }
            visitedIds.add(id);
        }
        return positionInSequenceOfFirstDelivery;
    }

    private int scheludePassengerDeliveries(int positionInSequenceOfFirstDelivery, List<Integer> idSequence,
            Set<Integer> visitedIds, List<Integer> deliveryTimes, int currentTimeForDelivery, ProblemData data) {
        List<Integer> anticipations = new ArrayList<>();
        for (int i = positionInSequenceOfFirstDelivery; i < idSequence.size() - 1; i++) {
            int originPassengerId = idSequence.get(i);
            int destinationPassengerId = idSequence.get(i + 1);
            Request originRequest = getRequestUsingId(originPassengerId, data);
            Request destinationRequest = getRequestUsingId(destinationPassengerId, data);
            int timeBetween;

            if (originPassengerId == destinationPassengerId) {
                currentTimeForDelivery = getTimeForTheSameRequest(data, originRequest, destinationRequest,
                        deliveryTimes, currentTimeForDelivery);
            } else {
                currentTimeForDelivery = getTimeForDifferentRequests(visitedIds, originPassengerId, destinationPassengerId,
                        data, originRequest, destinationRequest, deliveryTimes, currentTimeForDelivery);
            }
            saveAnticipations(originRequest, anticipations);
        }
        addTimeToDepot(deliveryTimes, currentTimeForDelivery, data, idSequence);
        Request lastRequest = getRequestUsingId(idSequence.get(idSequence.size() - 1), data);
        saveAnticipations(lastRequest, anticipations);

        return currentTimeForDelivery;
    }

    private void addTimeToDepot(List<Integer> deliveryTimes, int currentTimeForDelivery, ProblemData data, List<Integer> idSequence) {
        deliveryTimes.add(-currentTimeForDelivery - (int) data
                .getDuration()[getRequestUsingId(idSequence.get(idSequence.size() - 1), data)
                .getDestination().getId()][data.getNodes().get(0).getId()].getSeconds() / 60);
    }

    private int bestScheludePassengerDeliveries(int positionInSequenceOfFirstDelivery, List<Integer> idSequence,
            Set<Integer> visitedIds, List<Integer> deliveryTimes, int currentTimeForDelivery, ProblemData data) {
        List<Integer> anticipations = new ArrayList<>();

        for (int i = positionInSequenceOfFirstDelivery; i < idSequence.size() - 1; i++) {
            int originPassengerId = idSequence.get(i);
            int destinationPassengerId = idSequence.get(i + 1);
            Request originRequest = getRequestUsingId(originPassengerId, data);
            Request destinationRequest = getRequestUsingId(destinationPassengerId, data);
            int timeBetween;

            if (originPassengerId == destinationPassengerId) {
                currentTimeForDelivery = getTimeForTheSameRequest(data, originRequest, destinationRequest,
                        deliveryTimes, currentTimeForDelivery);
            } else {
                currentTimeForDelivery = getTimeForDifferentRequests(visitedIds, originPassengerId, destinationPassengerId,
                        data, originRequest, destinationRequest, deliveryTimes, currentTimeForDelivery);
            }
            saveAnticipations(originRequest, anticipations);
        }
        Request lastRequest = getRequestUsingId(idSequence.get(idSequence.size() - 1), data);
        saveAnticipations(lastRequest, anticipations);

        if (isNotARouteForOnePassenger(idSequence)) {
            currentTimeForDelivery = rescheduleDeliveriesAfterConstruction(anticipations, positionInSequenceOfFirstDelivery, idSequence,
                    visitedIds, deliveryTimes, currentTimeForDelivery, data);
        } else {
            Request request = getRequestUsingId(idSequence.get(0), data);
            deliveryTimes.add(-request.getDeliveryTimeWindowLowerInMinutes());
            int time = (int) -data.getDuration()[request.getDestination().getId()][0].getSeconds() / 60;
            deliveryTimes.add(-currentTimeForDelivery - time);
        }

        return currentTimeForDelivery;
    }

    private static boolean isNotARouteForOnePassenger(List<Integer> idSequence) {
        return idSequence.size() > 2;
    }

    private void saveAnticipations(Request originRequest, List<Integer> anticipations) {
        int anticipation = (int) (Duration.between(originRequest.getDeliveryTimeWindowLower(), originRequest.getDeliveryTime()).getSeconds() / 60);
        if (anticipation < 0) {
            anticipations.add(anticipation);
        }
    }

    private int rescheduleDeliveriesAfterConstruction(List<Integer> anticipations, int positionInSequenceOfFirstDelivery,
            List<Integer> idSequence, Set<Integer> visitedIds, List<Integer> deliveryTimes, int currentTimeForDelivery,
            ProblemData data) {

        int lastAnticipation = 0;
        int totalAnticipation = anticipations.stream().mapToInt(Integer::valueOf).sum();
        List<Request> requests = new ArrayList<>();

        getDeliveryRequests(positionInSequenceOfFirstDelivery, idSequence, data, requests);
        int oldDelay = getTotalDelay(requests);

        for (Integer anticipation : anticipations) {

            addAnticipationToRequestsDeliveries(requests, anticipation, lastAnticipation);
            addAnticipationToTimesList(deliveryTimes, anticipation, lastAnticipation);

            int newAnticipation = getTotalAnticipationAfterTimeAdded(requests);
            int newDelay = getTotalDelay(requests);
            if (newAnticipation < totalAnticipation && newDelay > oldDelay) {
                removeAnticipationAdded(deliveryTimes, anticipation);
            } else {
                lastAnticipation = anticipation;
                totalAnticipation = newAnticipation;
            }
        }
        return -deliveryTimes.get(deliveryTimes.size() - 1);

    }

    private int getTotalDelay(List<Request> requests) {
        return requests.stream().mapToInt(u -> (int) u.getAnticipation().toMinutes() / 60).sum();
    }

    private void removeAnticipationAdded(List<Integer> deliveryTimes, Integer anticipation) {
        for (int j = 0; j < deliveryTimes.size(); j++) {
            deliveryTimes.set(j, deliveryTimes.get(j) - anticipation);
        }
    }

    private void subtractAddedAnticipationFromSet(List<Integer> anticipations, int lastAnticipation) {
        if (anticipations.size() != 0) {
            for (int i = 0; i < anticipations.size(); i++) {
                anticipations.set(i, anticipations.get(i) - lastAnticipation);
            }
        }
    }

    private int getTotalAnticipationAfterTimeAdded(List<Request> requests) {
        return requests.stream().mapToInt(u -> (int) u.getAnticipation().toMinutes()).sum();
    }

    private void addAnticipationToTimesList(List<Integer> deliveryTimes, Integer anticipation, int lastAnticipation) {
        for (int j = 0; j < deliveryTimes.size(); j++) {
            deliveryTimes.set(j, deliveryTimes.get(j) + anticipation - lastAnticipation);
        }
    }

    private void addAnticipationToRequestsDeliveries(List<Request> requests, Integer anticipation, int lastAnticipation) {
        for (int i = 0; i < requests.size(); i++) {
            Request request = requests.get(i);
            request.setDeliveryTime(request.getDeliveryTimeInMinutes() - anticipation - lastAnticipation);
            int k = 0;
        }
    }

    private void getDeliveryRequests(int positionInSequenceOfFirstDelivery, List<Integer> idSequence, ProblemData data, List<Request> requests) {
        for (int i = positionInSequenceOfFirstDelivery; i < idSequence.size(); i++) {
            Request request = getRequestUsingId(idSequence.get(i), data);
            requests.add((Request) request.clone());
        }
    }

    private void rescheduleDeliveriesDuringConstruction() {

    }

    private int getTimeForDifferentRequests(Set<Integer> visitedIds, int originPassengerId, int destinationPassengerId,
            ProblemData data, Request originRequest, Request destinationRequest, List<Integer> deliveryTimes,
            int currentTimeForDelivery) {
        int timeBetween;
        if (visitedIds.contains(originPassengerId)) {
            if (visitedIds.contains(destinationPassengerId)) {
                timeBetween = (int) data.getDuration()[originRequest.getDestination().getId()][destinationRequest.getDestination().getId()]
                        .getSeconds() / 60;
            } else {
                timeBetween = (int) data.getDuration()[originRequest.getDestination().getId()][destinationRequest.getOrigin().getId()]
                        .getSeconds() / 60;
            }
            deliveryTimes.add(-currentTimeForDelivery - timeBetween);
            currentTimeForDelivery -= -timeBetween;
        } else {
            if (visitedIds.contains(destinationPassengerId)) {
                timeBetween = (int) data.getDuration()[originRequest.getOrigin().getId()][destinationRequest.getDestination().getId()]
                        .getSeconds() / 60;
            } else {
                timeBetween = (int) data.getDuration()[originRequest.getOrigin().getId()][destinationRequest.getOrigin().getId()]
                        .getSeconds() / 60;
            }
            deliveryTimes.add(-currentTimeForDelivery - timeBetween);
            currentTimeForDelivery -= -timeBetween;
        }
        visitedIds.add(originPassengerId);
        visitedIds.add(destinationPassengerId);
        return currentTimeForDelivery;
    }

    private int getTimeForTheSameRequest(ProblemData data, Request originRequest, Request destinationRequest, List<Integer> deliveryTimes, int currentTimeForDelivery) {
        int timeBetween;
        timeBetween = (int) data.getDuration()[originRequest.getOrigin().getId()][destinationRequest.getDestination().getId()]
                .getSeconds() / 60;
        originRequest.setDeliveryTime(-currentTimeForDelivery - timeBetween);
        deliveryTimes.add(-currentTimeForDelivery - timeBetween);
        currentTimeForDelivery -= -timeBetween;
        return currentTimeForDelivery;
    }

    private void addDepotInPickupAndDeliverySequences(List<Integer> deliveryIdSequence, List<Integer> pickupIdSequence) {
        deliveryIdSequence.add(0);
        pickupIdSequence.add(0, 0);
    }

    private List<Integer> schedulePassengerPickups(List<Integer> pickupIdSequence, List<Integer> idSequence,
            int positionInSequenceOfFirstDelivery, int currentTimeForPickup, List<Integer> deliveryTimes, ProblemData data) {
        List<Long> displacementTimesBetweenPassengers = new ArrayList<>();
        for (int i = 0; i < pickupIdSequence.size() - 1; i++) {
            int originPassengerId = pickupIdSequence.get(i);
            int destinationPassengerId = pickupIdSequence.get(i + 1);
            long displacementTime = 0;

            Request originRequest = getRequestUsingId(originPassengerId, data);
            Request destinationRequest = getRequestUsingId(destinationPassengerId, data);

            if (originRequest == null) {
                displacementTime = data.getDuration()[0][destinationRequest.getOrigin().getId()]
                        .getSeconds();
            } else if (destinationRequest == null) {
                displacementTime = data.getDuration()[originRequest.getOrigin().getId()][0]
                        .getSeconds();
            } else {
                displacementTime = data.getDuration()[originRequest.getOrigin().getId()][destinationRequest.getOrigin().getId()]
                        .getSeconds();
            }
            displacementTimesBetweenPassengers.add(-displacementTime / 60);
        }
        Request lastPickup = getRequestUsingId(idSequence.get(positionInSequenceOfFirstDelivery - 1), data);
        Request firstDelivery = getRequestUsingId(idSequence.get(positionInSequenceOfFirstDelivery), data);
        long timeBetweenLastPickupAndFirstDelivery = data
                .getDuration()[lastPickup.getOrigin().getId()][firstDelivery.getDestination().getId()].getSeconds() / 60;

        displacementTimesBetweenPassengers.add(-timeBetweenLastPickupAndFirstDelivery);

        List<Integer> times = new ArrayList<>();
        for (int i = displacementTimesBetweenPassengers.size() - 1; i >= 0; i--) {
            currentTimeForPickup -= displacementTimesBetweenPassengers.get(i);
            times.add((int) currentTimeForPickup);
        }

        Collections.reverse(times);
        times.addAll(deliveryTimes);
        return times;
    }

    private void setIntegerRepresentation(List<Integer> idSequence, List<Integer> times, ProblemData data) {
        List<Integer> integerRepresentation = buildIntegerRepresentation(idSequence, times);

        this.clearIntegerRepresentation();
        this.setIntegerRouteRepresetation(integerRepresentation);
        this.setPickupAndDeliveryTimeForEachAttendedRequest(data);
    }

    public void capacityAnalysis(ProblemData data) {
        List<Integer> idSequence = getOnlyIdSequence();
        List<Integer> vehicleOccupation = new ArrayList<>();
        this.violatedSomeConstraint = false;
        int busySeats = 0;
        vehicleOccupation.add(busySeats);
        for (int i = 0; i < idSequence.size(); i++) {
            int currentId = idSequence.get(i);
            if (idSequence.subList(0, i).contains(currentId)) {
                busySeats--;
            } else {
                busySeats++;
                if (busySeats > data.getVehicleCapacity()) {
                    this.violatedSomeConstraint = true;
                }
            }
            vehicleOccupation.add(busySeats);
        }
    }

    private void penalizeRoute() {
        this.evaluationFunction = this.totalDistanceTraveled * this.routeTravelTime * this.routeTravelTime;
        //this.totalDistanceTraveled = this.totalDistanceTraveled * this.routeTravelTime * this.routeTravelTime;
    }

    private List<Integer> buildIntegerRepresentation(List<Integer> idSequence, List<Integer> times) {
        List<Integer> integerRepresentation = new ArrayList<>();
        for (int i = 0; i < idSequence.size(); i++) {
            integerRepresentation.add(idSequence.get(i));
            integerRepresentation.add(times.get(i));
        }
        return integerRepresentation;
    }

    public void replaceRequest(int oldId, int newId, ProblemData data) {
        List<Integer> idSequence = new ArrayList<>();
        idSequence = this.integerRouteRepresetation.stream().filter(u -> u.longValue() >= 0)
                .collect(Collectors.toCollection(ArrayList::new));

        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < idSequence.size(); i++) {
            if (idSequence.get(i) == oldId) {
                positions.add(i);
            }
        }

        for (int id : positions) {
            idSequence.set(id, newId);
        }

        clear();
        rebuild(idSequence, data);
    }

    public void removeReallocatedRequest(int requestId, ProblemData data) {

        this.rebuild(integerRouteRepresetation.stream()
                .filter(u -> u.intValue() >= 0 && u.intValue() != requestId)
                .collect(Collectors.toCollection(ArrayList::new)), data);
    }

    public void removeRequest(int requestId, ProblemData data) {
        List<Integer> idSequence = new ArrayList<>();
        if (this.integerRouteRepresetation.size() > 2) {
            idSequence.addAll(this.integerRouteRepresetation.stream()
                    .filter(u -> u.intValue() >= 0 && u.intValue() != requestId)
                    .collect(Collectors.toCollection(ArrayList::new)));
        }
        idSequence.remove((Integer) requestId);
        this.clear();
        if (idSequence.stream().mapToInt(u -> u.intValue()).sum() == 0) {
            this.clearAtributes();
        } else {
            this.rebuild(idSequence, data);
        }
    }

    public void removeAddedRequests(List<Integer> idSequence, ProblemData data) {
        if (integerRouteRepresetation.removeAll(idSequence)) {
            this.rebuild(integerRouteRepresetation, data);
        }
    }

    public boolean isEmpty() {
        return this.evaluationFunction == 0;
    }

    public boolean isPenalized() {
        return this.violatedSomeConstraint == true;
    }

    public List<Integer> getUsedIds() {
        Set<Integer> setOfIds = this.getIntegerRouteRepresetation()
                .stream()
                .filter(u -> u.intValue() > 0)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        List<Integer> usedIds = new ArrayList<>();
        for (int id : setOfIds) {
            usedIds.add(id);
        }
        return usedIds;
    }

    public void addOtherRouteInRandomPosition(List<Integer> idSequence) {

    }

    @Override
    public String toString() {
        return "Route - Evaluation Function = " + this.evaluationFunction + " - Total Distance = " + this.totalDistanceTraveled + " km - Travel Time = " + this.routeTravelTime
                + " min - Total of Anticipation  = " + this.totalTimeWindowAnticipation + " min"
                + " - Total of Delay  = " + this.totalTimeWindowDelay + " min\t" + this.integerRouteRepresetation
                + "Violate some constraint = " + this.violatedSomeConstraint;
    }

    public Object clone() {
        List<Request> sequenceOfAttendedRequestsClone = new ArrayList<>();
        List<Node> nodesSequenceClone = new ArrayList<>();
        for (Request request : sequenceOfAttendedRequests) {
            sequenceOfAttendedRequestsClone.add((Request) request.clone());
        }

        for (Node node : nodesSequence) {
            nodesSequenceClone.add((Node) node.clone());
        }

        return new Route(totalDistanceTraveled, routeTravelTime, totalTimeWindowAnticipation, totalTimeWindowDelay,
                evaluationFunction, notServedRequests, nodesSequenceClone, sequenceOfAttendedRequestsClone,
                new ArrayList(integerRouteRepresetation), violatedSomeConstraint);
    }
}
