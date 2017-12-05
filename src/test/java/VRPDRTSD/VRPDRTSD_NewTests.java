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
public class VRPDRTSD_NewTests {
    @Test
    public void newBuildGreedySolution(){
        System.out.println("------ Testing newBuildGreedySolution ------");
        String instanceName = "r010n12tw10";
        String nodesData = "bh_n12s";
        String adjacenciesData = "bh_adj_n12s";
        int numberOfVehicles = 250;
        int vehicleCapacity = 11;

        VRPDRTSD problem = new VRPDRTSD(instanceName, nodesData, adjacenciesData, numberOfVehicles, vehicleCapacity);
        problem.buildGreedySolution();
        problem.getSolution().printAllInformations();
        
//        problem.localSearch(8);
//        problem.localSearch(2);
//        problem.getSolution().printAllInformations();
        
    }
}
