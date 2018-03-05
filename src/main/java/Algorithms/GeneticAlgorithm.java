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
            incrementsCurrentIteration();
        }
    }

    private void incrementsCurrentIteration() {
        currentIteration++;
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
    public void selection() {//alter this function -> is just for test the algorithm
        Random rnd = new Random();
        int position;
        for (int i = 0; i < this.populationSize; i++) {
            do {
                position = rnd.nextInt((int) this.populationSize);
            } while (this.parents.contains(position));
            this.parents.add(position);
        }

    }

    @Override
    public void crossOver() {
        SolutionForEA dad = new SolutionForEA();
        SolutionForEA mom = new SolutionForEA();

        for (int i = 0; i < this.populationSize; i = i + 2) {
            dad.setSolution(this.population.get(parents.get(i)));
            mom.setSolution(this.population.get(parents.get(i + 1)));
            
            System.out.println("dad = " + dad);
            System.out.println("mom = " + mom);
        }
    }

    @Override
    public void mutation() {
        Random rnd = new Random();
        for (int i = 0; i < this.populationSize; i++) {
            double probability = rnd.nextDouble();
            if (probability < this.mutationProbabilty) {
                problem.setSolution(this.population.get(i));
                problem.perturbation(2, 1);
                problem.localSearch(5);
            }
        }
    }

    @Override
    public void storeBestIndividual() {

    }

    @Override
    public void insertBestIndividual() {

    }

}
