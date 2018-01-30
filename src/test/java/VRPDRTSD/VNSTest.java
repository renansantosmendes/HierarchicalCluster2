/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VRPDRTSD;

import InstanceReader.Instance;
import java.io.FileNotFoundException;
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author renansantos
 */
public class VNSTest {

    @Test
    public void VNSTest() throws FileNotFoundException {
        int requestsNumber = 10;

        Instance instance = new Instance();
        instance.setNumberOfRequests(requestsNumber)
                .setRequestTimeWindows(10)
                .setInstanceSize("s")
                .setNumberOfNodes(12)
                .setNumberOfVehicles(250)
                .setVehicleCapacity(4);

        //VRPDRTSD problem = new VRPDRTSD(instance);
//        new ExperimentalDesign().runVnsExperiment();
//        problem.vnsForExperiment();
//        problem.VNS();
//        problem.buildGreedySolution();
//        System.out.println(problem.getSolution());
////        problem.localSearch(6);
//        System.out.println(problem.getSolution());
//        problem.VND();
//        System.out.println(problem.getSolution());

    }

}
