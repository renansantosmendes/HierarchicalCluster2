/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VRPDRTSD;

import InstanceReader.Instance;
import java.io.IOException;
import jxl.read.biff.BiffException;
import org.junit.Test;

/**
 *
 * @author renansantos
 */
public class InstanceExampleTest {

    @Test
    public void instanceExample() throws IOException, BiffException {
        int requestsNumber = 5;
        String path = "/home/renansantos/Área de Trabalho/Excel Instances/";

        Instance instance = new Instance();
        instance.setNumberOfRequests(requestsNumber)
                .setRequestTimeWindows(10)
                .setInstanceSize("s")
                .setNumberOfNodes(12)
                .setNumberOfVehicles(250)
                .setVehicleCapacity(4);

//        new ExperimentalDesign().runSimulatedAnnealingExperimentWithExcelData(path);
//        new ExperimentalDesign().runMultiStartExperimentWithExcelData(path);
        //new ExperimentalDesign().runVnsExperimentWithExcelData(path);

//        VRPDRTSD problem = new VRPDRTSD(instance, path);
////        //problem.getData().getRequests().forEach(r -> System.out.println(r.getId() + "\t" + r.getDeliveryTimeWindowLowerInMinutes()));
//        problem.buildGreedySolution();
//        System.out.println("first solution");
//        System.out.println(problem.getSolution());
////        problem.vns();
//        //problem.multiStart();
//        //problem.localSearch(6);
//        problem.getSolution().printAllInformations();
////        problem.getSolution().getRoutes().forEach(r -> System.out.println(r.getSequenceOfAttendedRequests()));
//        problem.getSolution().getRoutes().forEach(System.out::println);
    }
}
