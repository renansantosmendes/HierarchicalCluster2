package ProblemRepresentation;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 *
 * @author renansantos The Request Class represents the request for transport
 * used in VRPDRTSD
 */
public class Request {

    private final Integer id;
    private final Node origin;
    private final Node destination;
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
            //LocalDateTime pickUp = LocalDateTime.of(dayRequestWasMade.toLocalDate(), LocalTime.of(hour, minute));
            this.pickUpTime = LocalDateTime.of(dayRequestWasMade.toLocalDate(), LocalTime.of(hour, minute));
        } else {
            int hour = pickUpTime / 60;
            int minute = pickUpTime % 60;
            //LocalDateTime pickUp = LocalDateTime.of(dayRequestWasMade.toLocalDate(), LocalTime.of(hour, minute));
            this.pickUpTime = LocalDateTime.of(dayRequestWasMade.toLocalDate(), LocalTime.of(hour, minute));
        }
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

    public void determineInicialFeasibility(LocalDateTime currentTime, Node currentNode, Duration timeMatrix[][]) {

        Duration durationUntilVehicleArrivesPickUpNode = timeMatrix[currentNode.getId()][this.getOrigin().getId()];
        Duration durationBetweenOriginAndDestination = timeMatrix[this.getOrigin().getId()][this.getDestination().getId()];

        Duration totalDuration = durationUntilVehicleArrivesPickUpNode.plus(durationBetweenOriginAndDestination);

        if (currentTime.plus(totalDuration).isBefore(this.getDeliveryTimeWindowUpper())) {
            this.setFeasible(true);
        }
    }

//    public void determineFeasibilityInConstructionFase(LocalDateTime currentTime, Request lastRequestAdded,
//            Node currentNode, Duration timeMatrix[][]) {
//
//        Duration durationUntilNextDelivery = timeMatrix[currentNode.getId()][this.getPassengerDestination().getId()];
//        Duration durationToDepot = timeMatrix[currentNode.getId()][0];
//        LocalDateTime totalDuration = currentTime.plus(durationUntilNextDelivery);
//        Duration durationBetweenLastRequestAndThis = Duration.between(lastRequestAdded.getDeliveryTimeWindowUpper(),
//                this.deliveryTimeWindowLower);
//        
//        Duration delta = Duration.between(lastRequestAdded.getDeliveryTimeWindowUpper(), this.getDeliveryTimeWindowLower());
//        //Duration durationBetweenTimeWindows = Duration.between(durationToDepot, durationBetweenLastRequestAndThis);
//        //durationToDepot.
//
//        //System.out.println("testing = " + durationToDepot.minus(durationBetweenLastRequestAndThis).toMinutes());
////        System.out.println("duration to depot = " + durationToDepot.getSeconds()
////                + " delta = " + Duration.between(lastRequestAdded.getDeliveryTimeWindowUpper(),
////                        this.getDeliveryTimeWindowLower()).getSeconds());
////        System.out.println(this);
////        if (totalDuration.isBefore(this.getDeliveryTimeWindowUpper())
////                && durationToDepot.minus(durationBetweenLastRequestAndThis).toMinutes() >= 0) {
////            this.setFeasible(true);
////
////        }
//        if (totalDuration.isBefore(this.getDeliveryTimeWindowUpper())
//                && delta.getSeconds() < durationToDepot.getSeconds()) {
//            this.setFeasible(true);
//        }else{
//            this.setFeasible(false);
//        }
//    }
    public void determineFeasibilityInConstructionFase(LocalDateTime currentTime, Request lastRequestAdded, Node currentNode, Duration timeMatrix[][]) {

        //repensar nesse valores coletados
        Duration durationFromCurrentNodeToThisDeliveryNode = timeMatrix[currentNode.getId()][this.getDestination().getId()];
        //Duration durationFromCurrentNodeToOrigin = timeMatrix[currentNode.getId()][0];
        Duration durationFromCurrentNodeToOrigin = timeMatrix[this.getDestination().getId()][0];

        Duration durationBetweenTimeWindows = Duration.between(this.deliveryTimeWindowLower, lastRequestAdded.getDeliveryTimeWindowUpper());

//        if (currentTime.plus(durationFromCurrentNodeToThisDeliveryNode).isBefore(this.getDeliveryTimeWindowUpper())
//                && (Math.abs(durationBetweenTimeWindows.getSeconds()) < durationFromCurrentNodeToOrigin.getSeconds()) 
//                && durationBetweenTimeWindows.getSeconds() >= 0) {
//            this.setFeasible(true);
//        } else {
//            this.setFeasible(false);
//        }
        if (currentTime.plus(durationFromCurrentNodeToThisDeliveryNode).isBefore(this.getDeliveryTimeWindowUpper())) {
            if (durationBetweenTimeWindows.getSeconds() <= 0) {
                this.setFeasible(true);
            } else if (durationBetweenTimeWindows.getSeconds() < durationFromCurrentNodeToOrigin.getSeconds()) {
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
                + "\nRRF = " + this.requestRankingFunction
                + "\nIs Feasible = " + this.feasible;
    }

}
