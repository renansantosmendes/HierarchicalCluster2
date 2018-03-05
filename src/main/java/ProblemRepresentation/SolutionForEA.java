package ProblemRepresentation;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author renansantos
 */
public class SolutionForEA extends Solution {

    private double fitness = 0;
    private int numberOfDominatedSolutions = 0;
    private int numberOfSolutionsThatDominate = 0;
    private List<SolutionForEA> dominatedSolutions = null;
    private List<SolutionForEA> solutionsThatDominate = null;

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

    public void mutate(){
        Solution solution = new Solution(this);
        List<Integer> routeIndexes = generateTwoDiffentRouteIndexes(solution);
        int firstRoute = routeIndexes.get(0);
        int secondRoute = routeIndexes.get(1);

        List<Integer> idSequenceToRemoveRequest = new ArrayList<>();
        List<Integer> idSequenceToInsertRequest = new ArrayList<>();
        idSequenceToRemoveRequest.addAll(returnUsedIds(solution, firstRoute));
        idSequenceToInsertRequest.addAll(solution.getRoute(secondRoute).getIntegerSequenceOfAttendedRequests());

        List<Integer> newIdSequence = new ArrayList<>();
        List<Integer> indexesToRemove = generateTwoDiffentRequestsToOneRoute(idSequenceToRemoveRequest);
        List<Integer> indexesToInsert = generateTwoDiffentRequestsToOneRoute(idSequenceToInsertRequest);
        int firstIndex = indexesToInsert.get(0);
        int secondIndex = indexesToInsert.get(1);
        newIdSequence.addAll(idSequenceToInsertRequest.subList(0, firstIndex));
        newIdSequence.add(idSequenceToRemoveRequest.get(indexesToRemove.get(0)));
        newIdSequence.addAll(idSequenceToInsertRequest.subList(firstIndex, secondIndex - 1));
        newIdSequence.add(idSequenceToRemoveRequest.get(indexesToRemove.get(0)));
        newIdSequence.addAll(idSequenceToInsertRequest.subList(secondIndex - 1, idSequenceToInsertRequest.size()));
    }
    
    private List<Integer> generateTwoDiffentRouteIndexes(Solution solution) {
        Random rnd = new Random();
        List<Integer> indexes = new ArrayList<>();
        int totalRoutes = solution.getNumberOfRoutes();
        int firstRoute, secondRoute;
        firstRoute = rnd.nextInt(totalRoutes);
        do {
            secondRoute = rnd.nextInt(totalRoutes);
        } while (firstRoute == secondRoute);
        indexes.add(firstRoute);
        indexes.add(secondRoute);

        return indexes;
    }
    
    private static List<Integer> returnUsedIds(Solution solution, int routePosition) {
        Set<Integer> setOfIds = solution.getRoute(routePosition).getIntegerRouteRepresetation()
                .stream()
                .filter(u -> u.intValue() > 0)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        List<Integer> idsUsed = new ArrayList<>();
        for (int id : setOfIds) {
            idsUsed.add(id);
        }
        return idsUsed;
    }
    
    private List<Integer> generateTwoDiffentRequestsToOneRoute(List<Integer> idSequence) {
        Random rnd = new Random();
        List<Integer> indexes = new ArrayList<>();
        int routeSize = idSequence.size();
        int firstRequest, secondRequest;
        if (idSequence.get(0) == 0 && idSequence.get(routeSize - 1) == 0) {
            firstRequest = rnd.nextInt(routeSize - 1) + 1;
            secondRequest = rnd.nextInt(routeSize - 1) + 1;
        } else {
            firstRequest = rnd.nextInt(routeSize);
            secondRequest = rnd.nextInt(routeSize);
        }
        indexes.add(firstRequest);
        indexes.add(secondRequest);
        Collections.sort(indexes);

        return indexes;
    }
}
