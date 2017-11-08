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

    public Route(long totalRouteDistance, long routeTravelTime, long totalTimeWindowAnticipation, long totalTimeWindowDelay,
            long evaluationFunction, Set<Request> notServedRequests, List<Node> nodesSequence, List<Request> sequenceOfServedRequests,
            List<Integer> integerRouteRepresetation) {
        this.totalDistanceTraveled = totalRouteDistance;
        this.routeTravelTime = routeTravelTime;
        this.totalTimeWindowAnticipation = totalTimeWindowAnticipation;
        this.totalTimeWindowDelay = totalTimeWindowDelay;
        this.evaluationFunction = evaluationFunction;
        this.notServedRequests = notServedRequests;
        this.nodesSequence = nodesSequence;
        this.sequenceOfAttendedRequests = sequenceOfServedRequests;
        this.integerRouteRepresetation = integerRouteRepresetation;
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
        calculateTravelTime(data);
        calculateDistanceTraveled(data);
        calculateTotalDeliveryAnticipation();
        calculateTotalDeliveryDelay();
        calculateEvaluationFunction();
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

        this.totalDistanceTraveled = totalDistance;
    }

    public void calculateTotalDeliveryAnticipation() {
        Duration violations = Duration.ofMinutes(0);
        long test = 0;
        Set<Request> attendedRequests = new HashSet<>();
        for (Request request : this.sequenceOfAttendedRequests) {
            attendedRequests.add(request);
        }

        for (Request request : attendedRequests) {
            if (request.getDeliveryTimeWindowLower().isAfter(request.getDeliveryTime())) {
                Duration time = Duration.between(request.getDeliveryTime(), request.getDeliveryTimeWindowLower());
                violations = violations.plus(time);
                long diference = Math.abs(request.getDeliveryTime().getHour() * 60 + request.getDeliveryTime().getMinute()
                        - request.getDeliveryTimeWindowLower().getHour() * 60 - request.getDeliveryTimeWindowLower().getMinute());
                test += diference;
            }
        }

        this.totalTimeWindowAnticipation = violations.getSeconds() / 60;
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
                this.clearIntegerRepresentation();
                this.clearNodesSequence();
                this.clearSequenceOfAttendeRequests();
                this.setIntegerRouteRepresetation(idSequence);
                this.scheduleRoute(data);
                this.buildSequenceOfAttendedRequests(data);
                this.buildNodesSequence(data);
                this.evaluateRoute(data);
            }
        }
    }

    public void addMinutesInRoute(int timeInterval, ProblemData data) {
        for (int i = 0; i < this.integerRouteRepresetation.size(); i++) {
            if (this.integerRouteRepresetation.get(i) < 0) {
                this.integerRouteRepresetation.set(i, this.integerRouteRepresetation.get(i) - timeInterval);
            }
        }
        setPickupAndDeliveryTimeForEachAttendedRequest(data);
        this.evaluateRoute(data);
    }

    public void removeMinutesInRoute(int timeInterval, ProblemData data) {
        for (int i = 0; i < this.integerRouteRepresetation.size(); i++) {
            if (this.integerRouteRepresetation.get(i) < 0) {
                this.integerRouteRepresetation.set(i, this.integerRouteRepresetation.get(i) + timeInterval);
            }
        }
        setPickupAndDeliveryTimeForEachAttendedRequest(data);
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
        for (int i = positionInSequenceOfFirstDelivery; i < idSequence.size() - 1; i++) {
            int originPassengerId = idSequence.get(i);
            int destinationPassengerId = idSequence.get(i + 1);
            Request originRequest = getRequestUsingId(originPassengerId, data);
            Request destinationRequest = getRequestUsingId(destinationPassengerId, data);
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
        }
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

        this.clearIntegerRepresentation();
        this.clearNodesSequence();
        this.clearSequenceOfAttendeRequests();
        this.setIntegerRouteRepresetation(idSequence);
        this.scheduleRoute(data);
        this.buildSequenceOfAttendedRequests(data);
        this.buildNodesSequence(data);
        this.evaluateRoute(data);
    }

    @Override
    public String toString() {
        return "Route - Evaluation Function = " + this.evaluationFunction + " - Total Distance = " + this.totalDistanceTraveled + " meters - Travel Time = " + this.routeTravelTime
                + " min - Total of Anticipation  = " + this.totalTimeWindowAnticipation + " min"
                + " - Total of Delay  = " + this.totalTimeWindowDelay + " min";
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
                evaluationFunction, notServedRequests, nodesSequenceClone, sequenceOfAttendedRequestsClone, integerRouteRepresetation);
    }
}
