package ProblemRepresentation;

import java.time.*;

/**
 *
 * @author renansantos The Request Class represents the request for transport
 * used in VRPDRTSD
 */
public class Request implements Cloneable {

    private final Integer id;
    private final Node origin;
    private final Node destination;
    private LocalDateTime dayRequestWasMade;
    private LocalDateTime pickUpTime;
    private LocalDateTime deliveryTime;
    private LocalDateTime deliveryTimeWindowLower;
    private LocalDateTime deliveryTimeWindowUpper;
    private Duration delay;
    private Duration anticipation;
    private boolean feasible;
    private boolean boarded;
    private double requestRankingFunction;
    private double distanceRankingFunction;
    private double distanceToAttendThisRequest;
    private double deliveryTimeWindowLowerRankingFunction;
    private double deliveryTimeWindowUpperRankingFunction;
    private double originNodeRankingFunction;
    private double destinationNodeRankingFunction;
    static int toleranceTime = 10;

    public Request(Integer requestId, Node passengerOrigin, Node passengerDestination, LocalDateTime dayRequestWasMade,
            LocalDateTime pickUpTime, LocalDateTime deliveryTimeWindowLower, LocalDateTime deliveryTimeWindowUpper) {
        this.id = requestId;
        this.origin = passengerOrigin;
        this.destination = passengerDestination;
        this.dayRequestWasMade = dayRequestWasMade;
        this.pickUpTime = pickUpTime;
        this.deliveryTimeWindowLower = deliveryTimeWindowLower;
        this.deliveryTimeWindowUpper = deliveryTimeWindowUpper;
        this.feasible = false;
        this.boarded = false;
        this.requestRankingFunction = -1.0;
        this.distanceRankingFunction = 0.0;
        this.distanceToAttendThisRequest = 0.0;
        this.deliveryTimeWindowLowerRankingFunction = 0.0;
        this.deliveryTimeWindowUpperRankingFunction = 0.0;
        this.originNodeRankingFunction = 0.0;
        this.destinationNodeRankingFunction = 0.0;
    }

    public Request(Integer id, Node origin, Node destination, LocalDateTime dayRequestWasMade, LocalDateTime pickUpTime,
            LocalDateTime deliveryTime, LocalDateTime deliveryTimeWindowLower, LocalDateTime deliveryTimeWindowUpper,
            Duration anticipation, Duration delay, boolean feasible, boolean boarded, double requestRankingFunction, 
            double distanceRankingFunction, double distanceToAttendThisRequest, double deliveryTimeWindowLowerRankingFunction,
            double deliveryTimeWindowUpperRankingFunction, double originNodeRankingFunction,
            double destinationNodeRankingFunction) {
        this.id = id;
        this.origin = origin;
        this.destination = destination;
        this.dayRequestWasMade = dayRequestWasMade;
        this.pickUpTime = pickUpTime;
        this.deliveryTime = deliveryTime;
        this.deliveryTimeWindowLower = deliveryTimeWindowLower;
        this.deliveryTimeWindowUpper = deliveryTimeWindowUpper;
        this.anticipation = anticipation;
        this.delay = delay;
        this.feasible = feasible;
        this.boarded = boarded;
        this.requestRankingFunction = requestRankingFunction;
        this.distanceRankingFunction = distanceRankingFunction;
        this.distanceToAttendThisRequest = distanceToAttendThisRequest;
        this.deliveryTimeWindowLowerRankingFunction = deliveryTimeWindowLowerRankingFunction;
        this.deliveryTimeWindowUpperRankingFunction = deliveryTimeWindowUpperRankingFunction;
        this.originNodeRankingFunction = originNodeRankingFunction;
        this.destinationNodeRankingFunction = destinationNodeRankingFunction;
    }

    public void setDayRequestWasMade(LocalDateTime dayRequestWasMade) {
        this.dayRequestWasMade = dayRequestWasMade;
    }

    public void setPickUpTime(LocalDateTime pickUpTime) {
        this.pickUpTime = pickUpTime;
    }

    public void setPickUpTime(Integer pickUpTime) {
        if (pickUpTime < 0) {
            pickUpTime = -pickUpTime;
            int hour = pickUpTime / 60;
            int minute = pickUpTime % 60;
            this.pickUpTime = LocalDateTime.of(dayRequestWasMade.toLocalDate(), LocalTime.of(hour, minute));
        } else {
            int hour = pickUpTime / 60;
            int minute = pickUpTime % 60;
            this.pickUpTime = LocalDateTime.of(dayRequestWasMade.toLocalDate(), LocalTime.of(hour, minute));
        }
    }

    public void setDeliveryTime(LocalDateTime deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public void setDeliveryTime(Integer deliveryTime) {
        if (deliveryTime < 0) {
            deliveryTime = -deliveryTime;
            int hour = deliveryTime / 60;
            int minute = deliveryTime % 60;
            this.deliveryTime = LocalDateTime.of(dayRequestWasMade.toLocalDate(), LocalTime.of(hour, minute));
            this.anticipation = Duration.between(this.deliveryTime, this.deliveryTimeWindowLower);
            this.delay = Duration.between(this.deliveryTime, this.deliveryTimeWindowUpper);
        } else {
            int hour = deliveryTime / 60;
            int minute = deliveryTime % 60;
            this.deliveryTime = LocalDateTime.of(dayRequestWasMade.toLocalDate(), LocalTime.of(hour, minute));
            this.anticipation = Duration.between(this.deliveryTime, this.deliveryTimeWindowLower);
            this.delay = Duration.between(this.deliveryTime, this.deliveryTimeWindowUpper);
        }
    }

    public void setDeliveryTimeWindowLower(LocalDateTime deliveryTimeWindowLower) {
        this.deliveryTimeWindowLower = deliveryTimeWindowLower;
    }

    public void setDeliveryTimeWindowUpper(LocalDateTime deliveryTimeWindowUpper) {
        this.deliveryTimeWindowUpper = deliveryTimeWindowUpper;
    }

    public void setFeasible(boolean feasible) {
        this.feasible = feasible;
    }

    public void setBoarded(boolean boarded) {
        this.boarded = boarded;
    }

    public void setRequestRankingFunction(double distanceCoeficient, double deliveryTimeWindowLowerCoeficient,
            double deliveryTimeWindowUpperCoeficient, double originNodeCoeficient, double destinationNodeCoeficient) {

        this.requestRankingFunction = distanceCoeficient * this.distanceRankingFunction
                + deliveryTimeWindowLowerCoeficient * this.deliveryTimeWindowLowerRankingFunction
                + deliveryTimeWindowUpperCoeficient * this.deliveryTimeWindowUpperRankingFunction
                + originNodeCoeficient * this.originNodeRankingFunction
                + destinationNodeCoeficient * this.destinationNodeRankingFunction;
    }

    public void setRequestRankingFunction(double requestRankingFunction) {
        this.requestRankingFunction = requestRankingFunction;
    }

    public void setDistanceRankingFunction(double maxDistance, double minDistance) {
        this.distanceRankingFunction
                = (maxDistance - this.getDistanceToAttendThisRequest()) / (maxDistance - minDistance);
    }

    public void setDistanceToAttendThisRequest(Node currentNode, long distanceMatrix[][]) {
        this.distanceToAttendThisRequest = distanceMatrix[currentNode.getId()][this.getOrigin().getId()]
                + distanceMatrix[this.getOrigin().getId()][this.getDestination().getId()];
    }

    public void setDeliveryTimeWindowLowerRankingFunction(int maxTimeWindowLower, int minTimeWindowLower) {
        this.deliveryTimeWindowLowerRankingFunction
                = (maxTimeWindowLower - this.getDeliveryTimeWindowLowerInMinutes()) / (maxTimeWindowLower - minTimeWindowLower);
    }

    public void setDeliveryTimeWindowUpperRankingFunction(int maxTimeWindowUpper, int minTimeWindowUpper) {
        this.deliveryTimeWindowUpperRankingFunction
                = (maxTimeWindowUpper - this.getDeliveryTimeWindowLowerInMinutes()) / (maxTimeWindowUpper - minTimeWindowUpper);
    }

    public void setOriginNodeRankingFunction(int maxLoadIndex, int minLoadIndex) {
        this.originNodeRankingFunction
                = (double) (this.getOrigin().getLoadIndex() - minLoadIndex) / (maxLoadIndex - minLoadIndex);
    }

    public void setDestinationNodeRankingFunction(int maxLoadIndex, int minLoadIndex) {
        this.destinationNodeRankingFunction
                = (double) (this.getDestination().getLoadIndex() - minLoadIndex) / (maxLoadIndex - minLoadIndex);;
    }

    public Integer getId() {
        return id;
    }

    public Node getOrigin() {
        return origin;
    }

    public Node getDestination() {
        return destination;
    }

    public LocalDateTime getDayRequestWasMade() {
        return dayRequestWasMade;
    }

    public LocalDateTime getPickUpTime() {
        return pickUpTime;
    }

    public LocalDateTime getDeliveryTime() {
        return deliveryTime;
    }

    public Integer getDeliveryTimeInMinutes() {
        return deliveryTime.getHour() * 60 + deliveryTime.getMinute();
    }

    public LocalDateTime getDeliveryTimeWindowLower() {
        return deliveryTimeWindowLower;
    }

    public Integer getDeliveryTimeWindowLowerInMinutes() {
        return deliveryTimeWindowLower.getHour() * 60 + deliveryTimeWindowLower.getMinute();
    }

    public LocalDateTime getDeliveryTimeWindowUpper() {
        return deliveryTimeWindowUpper;
    }

    public Integer getDeliveryTimeWindowUpperInMinutes() {
        return deliveryTimeWindowUpper.getHour() * 60 + deliveryTimeWindowUpper.getMinute();
    }

    public boolean isFeasible() {
        return feasible;
    }

    public boolean isBoarded() {
        return boarded;
    }

    public double getRequestRankingFunction() {
        return requestRankingFunction;
    }

    public double getDistanceRankingFunction() {
        return distanceRankingFunction;
    }

    public double getDistanceToAttendThisRequest() {
        return distanceToAttendThisRequest;
    }

    public double getDeliveryTimeWindowLowerRankingFunction() {
        return deliveryTimeWindowLowerRankingFunction;
    }

    public double getDeliveryTimeWindowUpperRankingFunction() {
        return deliveryTimeWindowLowerRankingFunction;
    }

    public double getOriginNodeRankingFunction() {
        return originNodeRankingFunction;
    }

    public double getDestinationNodeRankingFunction() {
        return destinationNodeRankingFunction;
    }
    
    public Duration getAnticipation(){
        return this.anticipation;
    }
    
    public Duration getDelay(){
        return this.delay;
    }

    public void determineInicialFeasibility(LocalDateTime currentTime, Node currentNode, Duration timeMatrix[][]) {

        Duration durationUntilVehicleArrivesPickUpNode = timeMatrix[currentNode.getId()][this.getOrigin().getId()];
        Duration durationBetweenOriginAndDestination = timeMatrix[this.getOrigin().getId()][this.getDestination().getId()];

        Duration totalDuration = durationUntilVehicleArrivesPickUpNode.plus(durationBetweenOriginAndDestination);

        if (currentTime.plus(totalDuration).isBefore(this.getDeliveryTimeWindowUpper())) {
            this.setFeasible(true);
        }
    }

    public void determineFeasibilityInConstructionFase(LocalDateTime currentTime, Request lastRequestAdded, Node currentNode, Duration timeMatrix[][]) {

        Duration durationFromCurrentNodeToThisDeliveryNode = timeMatrix[currentNode.getId()][this.getDestination().getId()];
        Duration durationFromCurrentNodeToOrigin = timeMatrix[this.getDestination().getId()][0];

        Duration durationBetweenTimeWindows = Duration.between(this.deliveryTimeWindowLower, lastRequestAdded.getDeliveryTimeWindowUpper());
        long test = durationFromCurrentNodeToOrigin.getSeconds()/60;
        long test2 = durationBetweenTimeWindows.getSeconds()/60;
        if (test2 < 0) {
            test2 = -test2;
        }
        if (currentTime.plus(durationFromCurrentNodeToThisDeliveryNode).isBefore(this.getDeliveryTimeWindowUpper())
                && (currentTime.plus(durationFromCurrentNodeToThisDeliveryNode).isAfter(this.getDeliveryTimeWindowLower()
                        .minusMinutes(toleranceTime)))) {
            if (durationBetweenTimeWindows.getSeconds() <= 0) {
                this.setFeasible(true);
            } else if (test2 < test) {
                this.setFeasible(true);
            } else {
                this.setFeasible(false);
            }
        } else {
            this.setFeasible(false);
        }
    }

    public void setRequest(Request request) {

    }

    public String toString() {
        return "Request: id = " + this.id + " Passenger Origin = " + this.origin.getId()
                + " Passenger Destination = " + this.destination.getId()
                + "\nTime Window Lower = " + this.deliveryTimeWindowLower
                + "\nTime Window Upper = " + this.deliveryTimeWindowUpper
                + "\nPickup Time = " + this.pickUpTime
                + "\nDelivery Time = " + this.deliveryTime
                + "\nRRF = " + this.requestRankingFunction
                + "\nIs Feasible = " + this.feasible;
    }

    public Object clone() {
        return new Request(id, (Node) origin.clone(), (Node) destination.clone(), dayRequestWasMade, pickUpTime, deliveryTime,
                deliveryTimeWindowLower, deliveryTimeWindowUpper, anticipation, delay, feasible, boarded, requestRankingFunction,
                distanceRankingFunction, distanceToAttendThisRequest, deliveryTimeWindowLowerRankingFunction,
                deliveryTimeWindowUpperRankingFunction, originNodeRankingFunction, destinationNodeRankingFunction);
    }

}
