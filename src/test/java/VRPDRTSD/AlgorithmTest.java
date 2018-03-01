/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VRPDRTSD;

import InstanceReader.Instance;
import org.junit.Test;

/**
 *
 * @author renansantos
 */
public class AlgorithmTest {
    @Test
    public void algorithmTests() {
        int requestsNumber = 10;
        String path = "/home/renansantos/Área de Trabalho/Excel Instances/";

        Instance instance = new Instance();
        instance.setNumberOfRequests(requestsNumber)
                .setRequestTimeWindows(10)
                .setInstanceSize("s")
                .setNumberOfNodes(12)
                .setNumberOfVehicles(250)
                .setVehicleCapacity(4);

        VRPDRTSD problem = new VRPDRTSD(instance, path);
        problem.multiStartForExperiment();
        
        //zproblem.printSolutionInformations();
    }
}
