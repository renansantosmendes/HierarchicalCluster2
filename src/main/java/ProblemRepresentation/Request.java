package ProblemRepresentation;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 *
 * @author renansantos The Request Class represents the request for transport
 * used in VRPDRTSD
 */
public class Request {

    private final Integer id;
    private final Node passengerOrigin;
    private final Node passengerDestination;
    private LocalDateTime dayRequestWasMade;
    private LocalDateTime pickUpTime;
    private LocalDateTime deliveryTime;
    private LocalDateTime deliveryTimeWindowLower;
    private LocalDateTime deliveryTimeWindowUpper;
    private boolean feasible;
    private boolean boarded;
    private double requestRankingFunction;
    private double distanceRankingFunction;
    private double distanceToAttendThisRequest;
    private double deliveryTimeWindowLowerRankingFunction;
    private double deliveryTimeWindowUpperRankingFunction;
    private double originNodeRankingFunction;
    private double destinationNodeRankingFunction;

    public Request(Integer requestId, Node passengerOrigin, Node passengerDestination, LocalDateTime dayRequestWasMade,
            LocalDateTime pickUpTime, LocalDateTime deliveryTimeWindowLower, LocalDateTime deliveryTimeWindowUpper) {
        this.id = requestId;
        this.passengerOrigin = passengerOrigin;
        this.passengerDestination = passengerDestination;
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

    public void setDayRequestWasMade(LocalDateTime dayRequestWasMade) {
        this.dayRequestWasMade = dayRequestWasMade;
    }

    public void setPickUpTime(LocalDateTime pickUpTime) {
        this.pickUpTime = pickUpTime;
    }

    public void setDeliveryTime(LocalDateTime deliveryTime) {
        this.deliveryTime = deliveryTime;
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

    public void setDistanceRankingFunction(double maxDistance, double minDistance) {
        this.distanceRankingFunction
                = (maxDistance - this.getDistanceToAttendThisRequest()) / (maxDistance - minDistance);
    }

    public void setDistanceToAttendThisRequest(Node currentNode, long distanceMatrix[][]) {
        this.distanceToAttendThisRequest = distanceMatrix[currentNode.getNodeId()][this.getPassengerOrigin().getNodeId()]
                + distanceMatrix[this.getPassengerOrigin().getNodeId()][this.getPassengerDestination().getNodeId()];
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
                = (double) (this.getPassengerOrigin().getLoadIndex() - minLoadIndex) / (maxLoadIndex - minLoadIndex);
    }

    public void setDestinationNodeRankingFunction(int maxLoadIndex, int minLoadIndex) {
        this.destinationNodeRankingFunction
                = (double) (this.getPassengerDestination().getLoadIndex() - minLoadIndex) / (maxLoadIndex - minLoadIndex);;
    }

    public Integer getId() {
        return id;
    }

    public Node getPassengerOrigin() {
        return passengerOrigin;
    }

    public Node getPassengerDestination() {
        return passengerDestination;
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

    public void determineFeasibility(LocalDateTime currentTime, Node currentNode, Duration timeMatrix[][]) {

        Duration durationUntilVehicleArrivesPickUpNode = timeMatrix[currentNode.getNodeId()][this.getPassengerOrigin().getNodeId()];
        Duration durationBetweenOriginAndDestination = timeMatrix[this.getPassengerOrigin().getNodeId()][this.getPassengerDestination().getNodeId()];

        Duration totalDuration = durationUntilVehicleArrivesPickUpNode.plus(durationBetweenOriginAndDestination);

        if (currentTime.plus(totalDuration).isBefore(this.getDeliveryTimeWindowUpper())) {
            this.setFeasible(true);
        }
    }

    public String toString() {
        return "Request: id = " + this.id + " Passenger Origin = " + this.passengerOrigin.getNodeId()
                + " Passenger Destination = " + this.passengerDestination.getNodeId()
                + "\nTime Window Lower = " + this.deliveryTimeWindowLower
                + "\nTime Window Upper = " + this.deliveryTimeWindowUpper + "\nRRF = " + this.requestRankingFunction + "\n";
    }

}
