/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VRPDRTSD;

import org.junit.Test;

/**
 *
 * @author renansantos
 */
public class VRPDRTSD_TestingRescheduling {
    @Test
    public void newBuildGreedySolution(){
        System.out.println("------ Testing newBuildGreedySolution ------");
        String instanceName = "r010n12tw10";
        String nodesData = "bh_n12s";
        String adjacenciesData = "bh_adj_n12s";
        int numberOfVehicles = 250;
        int vehicleCapacity = 4;

        VRPDRTSD problem = new VRPDRTSD(instanceName, nodesData, adjacenciesData, numberOfVehicles, vehicleCapacity);
        problem.buildGreedySolution();
        problem.getSolution().printAllInformations();
        problem.getSolution().getRoutes().forEach(System.out::println);
        
//        problem.localSearch(4);
//        problem.getSolution().printAllInformations();
//        problem.getSolution().getRoutes().forEach(System.out::println);
    }
}
