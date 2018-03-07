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
            long numberOfVehicles, long evaluationFunction, List<Route> routes, Set<Request> nonAttendedRequests
            , List<Integer> integerRepresentation) {

        super(totalDistance, totalTravelTime, totalTimeWindowAnticipation, totalTimeWindowDelay, numberOfVehicles,
                evaluationFunction, routes, nonAttendedRequests, integerRepresentation);
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
   
}
