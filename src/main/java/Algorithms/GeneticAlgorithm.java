package Algorithms;

import InstanceReader.DataOutput;
import InstanceReader.Instance;
import ProblemRepresentation.*;
import VRPDRTSD.VRPDRTSD;
import java.util.*;
import java.util.stream.Collectors;

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
    private long numberOfGenerations;
    private Instance instance;
    private VRPDRTSD problem;
    private int currentGeneration = 0;
    private int numberOfExecutions = 1;
    private EvolutionarySolution bestIndividual = new EvolutionarySolution();
    private DataOutput output;
    DataOutput outputForBestSolutions;

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

    public EvolutionarySolution getBestIndividual() {
        return this.bestIndividual;
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

    public long getNumberOfGenerations() {
        return numberOfGenerations;
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

    public GeneticAlgorithm setNumberOfGenerations(int numberOfGenerations) {
        this.numberOfGenerations = numberOfGenerations;
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

    public void runWithLocalSearch() {
        initializeFilesToSaveData();
        initializePopulation();
        while (stopCriterionIsNotSatisfied()) {
            printInformations();
            calculateFitness();
            storeBestIndividual();
            selection();
            crossOver();
            mutation();
            localSeach();
            insertBestIndividual();
            incrementsCurrentIteration();
            saveData();
        }
    }

    public void runExperiment() {
        initializeFileToSaveBestSolutions();
        for (int execution = 0; execution < numberOfExecutions; execution++) {
            printExecutionInformations(execution);
            initializeFilesToSaveData(execution);
            initializePopulation();
            while (stopCriterionIsNotSatisfied()) {
                printInformations();
                calculateFitness();
                storeBestIndividual();
                selection();
                crossOverAddRoute();
                
                if(this.bestIndividual.getEvaluationFunction() < 400){
                    this.printPopulation();
                }
                //crossOver();
                mutation();
                insertBestIndividual();
                incrementsCurrentIteration();
                saveData();
                removeEmptySolutions();
            }
            finalizeExecution();
        }
    }

    private void finalizeExecution() {
        currentGeneration = 0;
        //this.population.clear();
        this.parents.clear();
        //this.printPopulation();

        saveExecutionData();
        this.bestIndividual.printAllInformations();
        this.bestIndividual = new EvolutionarySolution();
        this.population.clear();
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

    private void initializeFileToSaveBestSolutions() {
        String algorithmName = "GeneticAlgorithm";
        String instanceName = problem.getData().getInstanceName();
        outputForBestSolutions = new DataOutput(algorithmName, instanceName);
    }

    private void saveData() {
        output.saveBestSolutionFoundInTxtFile(bestIndividual, currentGeneration);
    }

    private void saveExecutionData() {
        outputForBestSolutions.saveBestSolutionFoundInTxtFile(bestIndividual);
    }

    private void printInformations() {
        System.out.println("Current Generation = " + currentGeneration + "\t" + this.bestIndividual);
    }

    private void printExecutionInformations(int execution) {
        System.out.println("\nExecution = " + execution);
    }

    private void incrementsCurrentIteration() {
        currentGeneration++;

    }

    private void removeEmptySolutions() {
        List<EvolutionarySolution> newPopulation = new ArrayList<>();
        newPopulation = this.population
                .stream()
                .filter(EvolutionarySolution::isNotEmpty)
                .collect(Collectors.toCollection(ArrayList::new));
        this.population.clear();
        this.population.addAll(newPopulation);
        if (newPopulation.size() != this.populationSize) {
            int numberOfSolutions = (int) (this.populationSize - newPopulation.size());
            for (int i = 0; i < numberOfSolutions; i++) {
                this.problem.buildRandomSolution();
                EvolutionarySolution solution = new EvolutionarySolution(this.problem.getSolution());
                this.population.add(solution);
            }
            System.out.println("number of solutions empty = " + numberOfSolutions);
        }

    }

    private boolean stopCriterionIsNotSatisfied() {
        return currentGeneration < numberOfGenerations;
    }

    @Override
    public void initializePopulation() {
        for (int i = 0; i < this.populationSize; i++) {
            this.problem.buildRandomSolutionWithSeed(12345);
            this.population.add(new EvolutionarySolution(this.problem.getSolution()));
        }
        calculateFitness();
        this.bestIndividual.setSolution(population.get(0));
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
//            System.out.println("P1 = " + firstParent.getIdsIntegerRepresentation() +
//                    "\nP2 = " + secondParent.getIdsIntegerRepresentation());
            
            firstChild.setSolution(firstParent);
            secondChild.setSolution(secondParent);

            int firstRouteIndex = firstChild.getRandomRoutePosition();
            int secondRouteIndex = secondChild.getRandomRoutePosition();

            //testing
            if (firstChild.getRoutes().size() >= secondChild.getRoutes().size()) {
                firstRouteIndex = secondRouteIndex;
            }

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

    public void crossOverAddRoute() {
        List<EvolutionarySolution> offspring = new ArrayList<>();
        EvolutionarySolution firstParent = new EvolutionarySolution();
        EvolutionarySolution secondParent = new EvolutionarySolution();
        EvolutionarySolution firstChild = new EvolutionarySolution();
        EvolutionarySolution secondChild = new EvolutionarySolution();
        List<Integer> firstIdSequence = new ArrayList<>();
        List<Integer> secondIdSequence = new ArrayList<>();

        for (int i = 0; i < 2 * this.populationSize; i = i + 2) {
            firstParent.setSolution(this.population.get(parents.get(i)));
            secondParent.setSolution(this.population.get(parents.get(i + 1)));
            firstChild.setSolution(firstParent);
            secondChild.setSolution(secondParent);

            int firstRouteIndex = firstChild.getRandomRoutePosition();
            int secondRouteIndex = secondChild.getRandomRoutePosition();

            if (firstChild.getRoutes().size() >= secondChild.getRoutes().size()) {
                firstRouteIndex = secondRouteIndex;
            }

            secondIdSequence.addAll(secondChild.getRoute(secondRouteIndex).getUsedIds());
            firstChild.removeSequenceFromAllSolution(secondIdSequence, firstRouteIndex, problem.getData());
            firstIdSequence.add(0);
            firstIdSequence.addAll(firstChild.getRoute(firstRouteIndex).getIntegerSequenceOfAttendedRequests()
                    .stream().filter(u -> u.intValue() > 0)
                    .collect(Collectors.toCollection(ArrayList::new)));
            firstIdSequence.addAll(secondChild.getRoute(secondRouteIndex).getIntegerSequenceOfAttendedRequests()
                    .stream().filter(u -> u.intValue() > 0)
                    .collect(Collectors.toCollection(ArrayList::new)));
            firstIdSequence.add(0);

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
//                System.out.println("before mutation = " + problem.getSolution());
                double localSearchProbability = rnd.nextDouble();
                if (localSearchProbability < 0.95) {
                    problem.perturbation(5, 1);
                } else {
                    System.out.println("Local Search");
//                    problem.localSearch(5);
                    //System.out.println(problem.getSolution());
                    //problem.vns();
                    //problem.setLocalSearchType(1);
                    //problem.vndForLocalSearchInIls(6);
                    problem.ils();
                }
//                problem.perturbation(5, 1);
//                problem.setLocalSearchType(1);
//                problem.localSearch(1);
//                problem.vns();

                this.population.get(i).setSolution(problem.getSolution());
//                System.out.println("after mutation = " + problem.getSolution());
            }
        }
    }

    @Override
    public void storeBestIndividual() {
        if (bestIndividual.getEvaluationFunction() > this.population.get(0).getEvaluationFunction()) {
            this.bestIndividual.setSolution(population.get(0));
        }
    }

    @Override
    public void insertBestIndividual() {
        if (bestIndividual.getEvaluationFunction() < this.population.get(0).getEvaluationFunction()) {
            this.population.get(this.population.size() - 1).setSolution(bestIndividual);
        }

    }

    public void localSeach() {
        if (this.currentGeneration % 25 == 0 && this.currentGeneration != 0) {
            problem.setSolution(bestIndividual);
//            problem.vns();
            problem.localSearch(1);
            bestIndividual.setSolution(problem.getSolution());
        }
    }

}
