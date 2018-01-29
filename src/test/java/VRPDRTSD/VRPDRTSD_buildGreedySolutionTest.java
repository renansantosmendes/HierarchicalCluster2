/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VRPDRTSD;

import InstanceReader.Instance;
import ProblemRepresentation.*;
import java.io.FileNotFoundException;
import java.util.*;
import org.junit.*;

/**
 *
 * @author renansantos
 */
public class VRPDRTSD_buildGreedySolutionTest {

    @Test
    public void greedySolutionTest() throws FileNotFoundException {
        System.out.println("Testing Metaheuristics");

        Instance instance = new Instance();
        instance.setNumberOfRequests(10)
                .setRequestTimeWindows(10)
                .setInstanceSize("s")
                .setNumberOfNodes(12)
                .setNumberOfVehicles(250)
                .setVehicleCapacity(4);

        VRPDRTSD problem = new VRPDRTSD(instance);
        //problem.multiStartForExperiment();
    }
}
