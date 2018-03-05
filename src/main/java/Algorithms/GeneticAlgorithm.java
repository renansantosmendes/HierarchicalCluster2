package Algorithms;

import InstanceReader.Instance;
import ProblemRepresentation.*;
import VRPDRTSD.VRPDRTSD;
import java.util.*;

/**
 *
 * @author renansantos
 */
public class GeneticAlgorithm implements EvolutionaryAlgorithms {

    private List<SolutionForEA> population;
    private List<Integer> parents;
    private double mutationProbabilty;
    private double crossOverProbability;
    private long populationSize;
    private long numberOfIterations;
    private Instance instance;
    private VRPDRTSD problem;
    private long currentIteration = 0;
    private SolutionForEA bestIndividual;

    public GeneticAlgorithm(Instance instance) {
        this.population = new ArrayList<>();
        this.parents = new ArrayList<>();
        this.instance = instance;
        tryToInitializeProblem(instance);
    }

    public GeneticAlgorithm(Instance instance, String path) {
        this.population = new ArrayList<>();
        this.parents = new ArrayList<>();
        this.instance = instance;
        tryToInitializeProblem(instance, path);
    }

    private void tryToInitializeProblem(Instance instance) {
        this.problem = new VRPDRTSD(instance);
    }

    private void tryToInitializeProblem(Instance instance, String path) {
        this.problem = new VRPDRTSD(instance, path);
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

    public List<Integer> getParents() {
        return parents;
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

    @Override    
    public void run() {
        initializePopulation();
        while (stopCriterionIsNotSatisfied()) {
            storeBestIndividual();
            selection();
            crossOver();
            mutation();
            insertBestIndividual();
            currentIteration++;
        }
    }

    private boolean stopCriterionIsNotSatisfied() {
        return currentIteration < numberOfIterations;
    }

    @Override
    public void initializePopulation() {
        for (int i = 0; i < this.populationSize; i++) {
            this.problem.buildRandomSolution();
            this.population.add(new SolutionForEA(this.problem.getSolution()));
        }
    }

    @Override
    public void selection() {
        Random rnd = new Random();
        for (int i = 0; i < this.populationSize; i++) {
            this.parents.add(rnd.nextInt((int) this.populationSize));
        }

    }

    @Override
    public void crossOver() {

    }

    @Override
    public void mutation() {
        Random rnd = new Random();
        for (int i = 0; i < this.populationSize; i++) {
            double probability = rnd.nextDouble();
            if(probability < this.mutationProbabilty){
                problem.setSolution(this.population.get(i));
                problem.perturbation(2, 1);
                //problem.localSearch(5);
                problem.vnd();
                //System.out.println(problem.getSolution());
            }
        }
    }
    
    @Override
    public void storeBestIndividual(){
        
    }
    
    @Override
    public void insertBestIndividual(){
        
    }

}
