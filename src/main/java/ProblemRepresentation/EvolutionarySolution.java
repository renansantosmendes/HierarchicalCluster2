package ProblemRepresentation;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author renansantos
 */
public class EvolutionarySolution extends Solution {

    private List<Double> objectiveFunctions = new ArrayList<>();
    private double fitness = 0;
    private int numberOfDominatedSolutions = 0;
    private int numberOfSolutionsThatDominate = 0;
    private List<EvolutionarySolution> dominatedSolutions = new ArrayList<>();
    private List<EvolutionarySolution> solutionsThatDominate = new ArrayList<>();

    public EvolutionarySolution() {
    }

    public EvolutionarySolution(Solution solution) {
        super(solution);
    }

    public EvolutionarySolution(long totalDistance, long totalTravelTime, long totalTimeWindowAnticipation, long totalTimeWindowDelay,
            long numberOfVehicles, long evaluationFunction, List<Route> routes, Set<Request> nonAttendedRequests, List<Integer> integerRepresentation) {

        super(totalDistance, totalTravelTime, totalTimeWindowAnticipation, totalTimeWindowDelay, numberOfVehicles,
                evaluationFunction, routes, nonAttendedRequests, integerRepresentation);
    }

    public EvolutionarySolution(long totalDistance, long totalTravelTime, long totalTimeWindowAnticipation, long totalTimeWindowDelay,
            long numberOfVehicles, long evaluationFunction, List<Route> routes, Set<Request> nonAttendedRequests,
            List<Integer> integerRepresentation, List<Double> objectiveFunctions, double fitness,
            int numberOfDominatedSolutions, int numberOfSolutionsThatDominate, List<EvolutionarySolution> dominatedSolutions,
            List<EvolutionarySolution> solutionsThatDominate) {

        super(totalDistance, totalTravelTime, totalTimeWindowAnticipation, totalTimeWindowDelay, numberOfVehicles,
                evaluationFunction, routes, nonAttendedRequests, integerRepresentation);

        this.fitness = fitness;
        this.numberOfDominatedSolutions = numberOfDominatedSolutions;
        this.numberOfSolutionsThatDominate = numberOfSolutionsThatDominate;
        this.dominatedSolutions.addAll(dominatedSolutions);
        this.solutionsThatDominate.addAll(solutionsThatDominate);
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public int getNumberOfDominatedSolutions() {
        return numberOfDominatedSolutions;
    }

    public void setNumberOfDominatedSolutions(int numberOfDominatedSolutions) {
        this.numberOfDominatedSolutions = numberOfDominatedSolutions;
    }

    public int getNumberOfSolutionsThatDominate() {
        return numberOfSolutionsThatDominate;
    }

    public void setNumberOfSolutionsThatDominate(int numberOfSolutionsThatDominate) {
        this.numberOfSolutionsThatDominate = numberOfSolutionsThatDominate;
    }

    public List<EvolutionarySolution> getDominatedSolutions() {
        return dominatedSolutions;
    }

    public void setDominatedSolutions(List<EvolutionarySolution> dominatedSolutions) {
        this.dominatedSolutions = dominatedSolutions;
    }

    public List<EvolutionarySolution> getSolutionsThatDominate() {
        return solutionsThatDominate;
    }

    public void setSolutionsThatDominate(List<EvolutionarySolution> solutionsThatDominate) {
        this.solutionsThatDominate = solutionsThatDominate;
    }

    public List<Double> getObjectiveFunctions() {
        return objectiveFunctions;
    }

    public void setObjectiveFunctions(List<Double> objectiveFunctions) {
        this.objectiveFunctions = objectiveFunctions;
    }

    @Override
    public Object clone() {
        List<Route> routesClone = new ArrayList<>();
        for (Route route : this.getRoutes()) {
            routesClone.add((Route) route.clone());
        }

        return new EvolutionarySolution(totalDistanceTraveled, totalTravelTime, totalTimeWindowAnticipation,
                totalTimeWindowDelay, numberOfVehicles, evaluationFunction, routesClone, nonAttendedRequests,
                integerRepresentation, objectiveFunctions, fitness, numberOfDominatedSolutions, numberOfSolutionsThatDominate,
                dominatedSolutions, solutionsThatDominate);
    }
    
    @Override
    public String toString() {
        return "Individual - "+ this.fitness + "\t" + this.evaluationFunction + "\t" + this.totalDistanceTraveled + "\t" + this.totalTravelTime + "\t"
                + this.totalTimeWindowAnticipation + "\t" + this.totalTimeWindowDelay + "\t" + this.numberOfVehicles
                + "\t" + this.integerRepresentation.stream()
                        .filter(u -> u.intValue() >= 0)
                        .collect(Collectors.toCollection(ArrayList::new));
    }
}
