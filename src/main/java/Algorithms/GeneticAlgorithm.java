package Algorithms;

import InstanceReader.DataOutput;
import InstanceReader.Instance;
import ProblemRepresentation.*;
import VRPDRTSD.VRPDRTSD;
import java.util.*;

/**
 *
 * @author renansantos
 */
public class GeneticAlgorithm implements EvolutionaryAlgorithms {

    private List<EvolutionarySolution> population;
    private List<Integer> parents;
    private double mutationProbabilty;
    private double crossOverProbability;
    private long populationSize;
    private long numberOfIterations;
    private Instance instance;
    private VRPDRTSD problem;
    private int currentIteration = 0;
    private int numberOfExecutions = 1;
    private EvolutionarySolution bestIndividual = new EvolutionarySolution();
    private DataOutput output;

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

    public List<EvolutionarySolution> getPopulation() {
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

    public VRPDRTSD getProblem() {
        return problem;
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

    public GeneticAlgorithm setNumberOfIterations(int numberOfIterations) {
        this.numberOfIterations = numberOfIterations;
        return this;
    }

    public GeneticAlgorithm setNumberOfExecutions(int numberOfExecutions) {
        this.numberOfExecutions = numberOfExecutions;
        return this;
    }

    @Override
    public void run() {
        initializeFilesToSaveData();
        initializePopulation();
        while (stopCriterionIsNotSatisfied()) {
            printInformations();
            calculateFitness();
            storeBestIndividual();
            selection();
            crossOver();
            mutation();
            insertBestIndividual();
            incrementsCurrentIteration();
            saveData();
        }
    }

    public void runExperiment() {
        for (int execution = 0; execution < numberOfExecutions; execution++) {
            initializeFilesToSaveData(execution);
            initializePopulation();
            while (stopCriterionIsNotSatisfied()) {
                printInformations();
                calculateFitness();
                storeBestIndividual();
                selection();
                crossOver();
                mutation();
                insertBestIndividual();
                incrementsCurrentIteration();
                saveData();
            }
        }
    }

    private void initializeFilesToSaveData() {
        String algorithmName = "GeneticAlgorithm";
        String instanceName = problem.getData().getInstanceName();
        output = new DataOutput(algorithmName, instanceName);
    }
    
     private void initializeFilesToSaveData(int execution) {
        String algorithmName = "GeneticAlgorithm";
        String instanceName = problem.getData().getInstanceName();
        output = new DataOutput(algorithmName, instanceName, execution);
    }
    
    private void saveData() {
        output.saveBestSolutionFoundInTxtFile(bestIndividual, currentIteration);
    }

    private void printInformations() {
        System.out.println("Current Iteration = " + currentIteration + "\t" + this.bestIndividual);
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
            this.population.add(new EvolutionarySolution(this.problem.getSolution()));
        }
        calculateFitness();
    }

    public void calculateFitness() {
        double sum = population.stream().mapToDouble(EvolutionarySolution::getEvaluationFunction).sum();
        population.forEach(u -> u.setFitness(u.getEvaluationFunction() / sum));

        double max = population.stream().mapToDouble(EvolutionarySolution::getFitness).max().getAsDouble();
        double min = population.stream().mapToDouble(EvolutionarySolution::getFitness).min().getAsDouble();
        population.forEach(u -> u.setFitness((max - u.getFitness()) / (max - min)));

        double fitnessSum = population.stream().mapToDouble(EvolutionarySolution::getFitness).sum();
        population.forEach(u -> u.setFitness(u.getFitness() / fitnessSum));
        population.sort(Comparator.comparing(EvolutionarySolution::getFitness).reversed());
    }

    @Override
    public void selection() {
        Random rnd = new Random();
        double cursor;
        double currentSum;
        int positsion;
        this.parents.clear();
        for (int i = 0; i < 2 * this.populationSize; i++) {
            currentSum = 0;
            cursor = rnd.nextDouble() / 2;
            findCursorPosition(currentSum, cursor, rnd);
        }
    }

    private void findCursorPosition(double currentSum, double cursor, Random rnd) {
        int position = -1;
        for (int j = 0; j < population.size(); j++) {
            currentSum += population.get(j).getFitness();
            if (cursor <= currentSum) {
                position = j;
                this.parents.add(position);
                break;
            }
        }
        if (position == -1) {
            position = rnd.nextInt(population.size());
            this.parents.add(position);
        }
    }

    @Override
    public void crossOver() {
        List<EvolutionarySolution> offspring = new ArrayList<>();
        EvolutionarySolution firstParent = new EvolutionarySolution();
        EvolutionarySolution secondParent = new EvolutionarySolution();
        EvolutionarySolution firstChild = new EvolutionarySolution();
        EvolutionarySolution secondChild = new EvolutionarySolution();
        List<Integer> firstIdSequence = new ArrayList<>();
        List<Integer> secondIdSequence = new ArrayList<>();

        for (int i = 0; i < 2 * this.population.size(); i = i + 2) {
            firstParent.setSolution(this.population.get(parents.get(i)));
            secondParent.setSolution(this.population.get(parents.get(i + 1)));
            firstChild.setSolution(firstParent);
            secondChild.setSolution(secondParent);

            int firstRouteIndex = firstChild.getRandomRoutePosition();
            int secondRouteIndex = secondChild.getRandomRoutePosition();

            secondIdSequence.addAll(secondChild.getRoute(secondRouteIndex).getUsedIds());
            firstChild.removeSequenceFromAllSolution(secondIdSequence, firstRouteIndex, problem.getData());
            firstIdSequence.addAll(firstChild.getRoute(firstRouteIndex).getIntegerSequenceOfAttendedRequests());

            for (Integer id : secondIdSequence) {
                List<Integer> indexesToInsert = generateTwoDiffentRequestsToOneRoute(firstIdSequence);
                insertIdInNewSequence(firstIdSequence, indexesToInsert.get(0), id, indexesToInsert.get(1));
            }

            firstChild.getRoute(firstRouteIndex).rebuild(firstIdSequence, problem.getData());

            firstIdSequence.clear();
            secondIdSequence.clear();
            firstChild.removeEmptyRoutes();
            firstChild.calculateEvaluationFunction(problem.getData());
            offspring.add((EvolutionarySolution) firstChild.clone());
        }
        this.population.clear();
        this.population.addAll(offspring);
    }

    public void printPopulation() {
        System.out.println();
        this.population.forEach(System.out::println);
    }

    private void insertIdInNewSequence(List<Integer> idSequenceToInsertRequest, int l, int requestId, int m) {
        List<Integer> newIdSequence = new ArrayList<>();
        try {
            newIdSequence.addAll(idSequenceToInsertRequest.subList(0, l));
            newIdSequence.add(requestId);
            newIdSequence.addAll(idSequenceToInsertRequest.subList(l, m - 1));
            newIdSequence.add(requestId);
            newIdSequence.addAll(idSequenceToInsertRequest.subList(m - 1, idSequenceToInsertRequest.size()));
        } catch (IllegalArgumentException e) {
            System.out.println("Sequence = " + idSequenceToInsertRequest);
            System.out.println("first position " + l);
            System.out.println("second position " + m);
            System.out.println();
        }
        idSequenceToInsertRequest.clear();
        idSequenceToInsertRequest.addAll(newIdSequence);
    }

    private List<Integer> generateTwoDiffentRequestsToOneRoute(List<Integer> idSequence) {
        Random rnd = new Random();
        List<Integer> indexes = new ArrayList<>();
        int routeSize = idSequence.size();
        int firstRequest, secondRequest;
        if (idSequence.size() == 0) {
            indexes.add(0);
            indexes.add(1);
            return indexes;
        }
        do {
            if (idSequence.get(0) == 0 && idSequence.get(routeSize - 1) == 0) {
                firstRequest = rnd.nextInt(routeSize - 1) + 1;
                secondRequest = rnd.nextInt(routeSize - 1) + 1;
            } else {
                firstRequest = rnd.nextInt(routeSize);
                secondRequest = rnd.nextInt(routeSize);
            }
        } while (firstRequest == secondRequest);
        indexes.add(firstRequest);
        indexes.add(secondRequest);
        Collections.sort(indexes);

        return indexes;
    }

    @Override
    public void mutation() {
        Random rnd = new Random();
        for (int i = 0; i < this.populationSize; i++) {
            double probability = rnd.nextDouble();
            if (probability < this.mutationProbabilty) {
                problem.setSolution(this.population.get(i));
                problem.perturbation(1, 1);
                problem.localSearch(1);
            }
        }
    }

    @Override
    public void storeBestIndividual() {
        this.bestIndividual.setSolution(population.get(0));
    }

    @Override
    public void insertBestIndividual() {
        this.population.get(this.population.size() - 1).setSolution(bestIndividual);
    }

}
