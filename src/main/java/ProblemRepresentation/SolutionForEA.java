package ProblemRepresentation;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author renansantos
 */
public class SolutionForEA extends Solution {
    private List<Double> objectiveFunctions = new ArrayList<>();
    private double fitness = 0;
    private int numberOfDominatedSolutions = 0;
    private int numberOfSolutionsThatDominate = 0;
    private List<SolutionForEA> dominatedSolutions = new ArrayList<>();
    private List<SolutionForEA> solutionsThatDominate = new ArrayList<>();

    public SolutionForEA() {
    }

    public SolutionForEA(Solution solution) {
        super(solution);
    }

    public SolutionForEA(long totalDistance, long totalTravelTime, long totalTimeWindowAnticipation, long totalTimeWindowDelay,
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

    public List<SolutionForEA> getDominatedSolutions() {
        return dominatedSolutions;
    }

    public void setDominatedSolutions(List<SolutionForEA> dominatedSolutions) {
        this.dominatedSolutions = dominatedSolutions;
    }

    public List<SolutionForEA> getSolutionsThatDominate() {
        return solutionsThatDominate;
    }

    public void setSolutionsThatDominate(List<SolutionForEA> solutionsThatDominate) {
        this.solutionsThatDominate = solutionsThatDominate;
    }

    public List<Double> getObjectiveFunctions() {
        return objectiveFunctions;
    }

    public void setObjectiveFunctions(List<Double> objectiveFunctions) {
        this.objectiveFunctions = objectiveFunctions;
    }
   
}
