package Algorithms;

import ProblemRepresentation.Solution;
import java.util.List;

/**
 *
 * @author renansantos
 */
public interface Heuristic {

    public void readInstance();

    public void buildGreedySolution();

    public void buildRandomSolution();

    public void localSearch(int typeOfLocalSearch);

    public void perturbation(int typeOfPerturbation, int intensity);
}
