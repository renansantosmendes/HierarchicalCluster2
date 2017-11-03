package Algorithms;

import java.util.List;

/**
 *
 * @author renansantos
 */
public interface Heuristic {

    public void readInstance();

    public void buildGreedySolution();

    public void localSearch(int typeOfLocalSearch);
}
