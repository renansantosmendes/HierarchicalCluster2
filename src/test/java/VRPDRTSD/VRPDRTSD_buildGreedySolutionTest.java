/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VRPDRTSD;

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
        Assert.assertEquals(numberOfRequests, problem.getData().getRequests().size());
    }
}
