/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VRPDRTSD;

import InstanceReader.Instance;
import java.io.FileNotFoundException;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author renansantos
 */
public class SimulatedAnnealingTest {

    @Test
    public void SA_Test() throws FileNotFoundException {
        int requestsNumber = 10;

        Instance instance = new Instance();
        instance.setNumberOfRequests(requestsNumber)
                .setRequestTimeWindows(10)
                .setInstanceSize("s")
                .setNumberOfNodes(12)
                .setNumberOfVehicles(250)
                .setVehicleCapacity(4);

//        VRPDRTSD problem = new VRPDRTSD(instance);
//        problem.SimulatedAnnealing();
//        problem.buildRandomSolution();
//        problem.getSolution().printAllInformations();
//        for (int i = 0; i < 10; i++) {
//            problem.perturbation(2, 1);
//            System.out.println(problem.getSolution().getEvaluationFunction());
//        }

        //Assert.assertEquals(217,problem.getSolution().getEvaluationFunction());
        
//        new ExperimentalDesign().runSimulatedAnnealingExperiment();
    }
}
