/*

 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VRPDRTSD;

import java.util.ArrayList;
import java.util.stream.Collectors;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author renansantos
 */
public class VRPDRTSDTest {

    public VRPDRTSDTest() {
    }

    @Test
    public void testReadInstance() {
        String instanceName = "r050n12tw10";
        String nodesData = "bh_n12s";
        String adjacenciesData = "bh_adj_n12s";
        int numberOfVehicles = 10;
        int vehicleCapacity = 11;
        VRPDRTSD problem = new VRPDRTSD(instanceName, nodesData, adjacenciesData, numberOfVehicles, vehicleCapacity);
        assertEquals(50, problem.getData().getRequests().size());
        assertEquals(12, problem.getData().getNodes().size());
        assertEquals(10, problem.getData().getAvaibleVehicles().size());
        assertEquals(0, problem.getData().getAllocatedVehicles().size());

    }

    @Test
    public void testBuildGreedySolution() {
        String instanceName = "r050n12tw10";
        String nodesData = "bh_n12s";
        String adjacenciesData = "bh_adj_n12s";
        int numberOfVehicles = 10;
        int vehicleCapacity = 11;
        VRPDRTSD problem = new VRPDRTSD(instanceName, nodesData, adjacenciesData, numberOfVehicles, vehicleCapacity);
        //problem.buildGreedySolution();
    }

    @Test
    public void feasibilityTest() {
        String instanceName = "r050n12tw10";
        String nodesData = "bh_n12s";
        String adjacenciesData = "bh_adj_n12s";
        int numberOfVehicles = 10;
        int vehicleCapacity = 11;
        VRPDRTSD problem = new VRPDRTSD(instanceName, nodesData, adjacenciesData, numberOfVehicles, vehicleCapacity);
       
        problem.originalRequestsFeasibilityAnalysis();
        problem.getData().getRequests().stream().filter(r -> r.isFeasible()).forEach(System.out::println);
        
        assertEquals(problem.getData().getNodes().get(0), problem.getData().getCurrentNode());
        assertEquals(50, problem.getData().getRequests()
                .stream().filter(r -> r.isFeasible())
                .collect(Collectors.toCollection(ArrayList::new)).size());
    }

}
