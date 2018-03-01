/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProblemRepresentation;

import java.util.List;
import java.util.Set;

/**
 *
 * @author renansantos
 */
public class SolutionForEA extends Solution{
    private double fitness;
    private int numberOfDominatedSolutions;
    private int numberOfSolutionsThatDominate;
    private List<SolutionForEA> dominatedSolutions;
    private List<SolutionForEA> solutionsThatDominate;

    public SolutionForEA() {
    }

    public SolutionForEA(Solution solution) {
        super(solution);
    }

    public SolutionForEA(long totalDistance, long totalTravelTime, long totalTimeWindowAnticipation, long totalTimeWindowDelay, 
            long numberOfVehicles, long evaluationFunction, List<Route> routes, Set<Request> nonAttendedRequests) {
        
        super(totalDistance, totalTravelTime, totalTimeWindowAnticipation, totalTimeWindowDelay, numberOfVehicles, 
                evaluationFunction, routes, nonAttendedRequests);
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

    
    
    
}
