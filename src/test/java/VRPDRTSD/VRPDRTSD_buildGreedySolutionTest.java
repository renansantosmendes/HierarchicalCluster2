/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VRPDRTSD;

import ProblemRepresentation.*;
import java.util.*;
import org.junit.*;

/**
 *
 * @author renansantos
 */
public class VRPDRTSD_buildGreedySolutionTest {

    @Test
    public void greedySolutionTest() {
        int numberOfRequests = 250;
        String instanceName = "r" + numberOfRequests + "n12tw10";
        String nodesData = "bh_n12s";
        String adjacenciesData = "bh_adj_n12s";
        int numberOfVehicles = 50;
        int vehicleCapacity = 4;
        System.out.println("Testing Metaheuristics");
        VRPDRTSD problem = new VRPDRTSD(instanceName, nodesData, adjacenciesData, numberOfVehicles, vehicleCapacity);
        problem.buildGreedySolution();
        //problem.getSolution().printAllInformations();

        Solution solution = new Solution();
        solution.setSolution(problem.getSolution());
        System.out.println(solution);

        Set<Request> set = new HashSet<>();
        getSolutionRequestSet(solution, set);
        
        Assert.assertEquals(numberOfRequests, set.size());
        Assert.assertEquals(numberOfRequests, problem.getData().getRequests().size());
    }

    private void getSolutionRequestSet(Solution solution, Set<Request> set) {
        for (Route route : solution.getRoutes()) {
            for (Request request : route.getSequenceOfAttendedRequests()) {
                set.add(request);
            }
        }
    }
}
