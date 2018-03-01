package Algorithms;

import InstanceReader.Instance;
import ProblemRepresentation.*;
import VRPDRTSD.VRPDRTSD;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import jxl.read.biff.BiffException;

/**
 *
 * @author renansantos
 */
public class GeneticAlgorithm implements EvolutionaryAlgorithms {

    private List<SolutionForEA> population;
    private double mutationProbabilty;
    private double crossOverProbability;
    private long populationSize;
    private long numberOfIterations;
    private Instance instance;
    private VRPDRTSD problem;
    private long currentIteration;

    public GeneticAlgorithm(Instance instance) {
        this.population = new ArrayList<>();
        this.instance = instance;
        tryToInitializeProblem(instance);
    }

    public GeneticAlgorithm(Instance instance, String path) {
        this.population = new ArrayList<>();
        this.instance = instance;
        tryToInitializeProblem(instance, path);
    }

    private void tryToInitializeProblem(Instance instance) {
        this.problem = new VRPDRTSD(instance);
    }

    private void tryToInitializeProblem(Instance instance, String path) {
        try {
            this.problem = new VRPDRTSD(instance, path);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (BiffException ex) {
            ex.printStackTrace();
        }
    }

    public List<SolutionForEA> getPopulation() {
        return population;
    }

    public double getMutationProbabilty() {
        return mutationProbabilty;
    }

    public double getCrossOverProbability() {
        return crossOverProbability;
    }

    public long getPopulationSize() {
        return populationSize;
    }

    public long getNumberOfIterations() {
        return numberOfIterations;
    }

    public Instance getInstance() {
        return instance;
    }

    public GeneticAlgorithm setMutationProbabilty(double mutationProbabilty) {
        this.mutationProbabilty = mutationProbabilty;
        return this;
    }

    public GeneticAlgorithm setCrossOverProbability(double crossOverProbability) {
        this.crossOverProbability = crossOverProbability;
        return this;
    }

    public GeneticAlgorithm setPopulationSize(long populationSize) {
        this.populationSize = populationSize;
        return this;
    }

    public GeneticAlgorithm setNumberOfIterations(long numberOfIterations) {
        this.numberOfIterations = numberOfIterations;
        return this;
    }

    public void run(){
        populationInitalization();
        long currentIteration = 0; 
        while(stopCriterionIsNotSatisfied()){
            selection();
            crossOver();
            mutation();
        }
    }

    private boolean stopCriterionIsNotSatisfied() {
        return currentIteration < numberOfIterations;
    }
    
    @Override
    public void populationInitalization() {
        for(int i = 0; i < this.populationSize; i++){
            this.problem.buildRandomSolution();
            this.population.add(new SolutionForEA(this.problem.getSolution()));
        }
    }

    @Override
    public void selection() {

    }

    @Override
    public void crossOver() {

    }

    @Override
    public void mutation() {

    }

}
