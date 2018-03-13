/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VRPDRTSD;

import InstanceReader.Instance;
import java.io.FileNotFoundException;
import java.io.IOException;
import jxl.read.biff.BiffException;
import org.junit.Test;

/**
 *
 * @author renansantos
 */
public class localSearchTest {

    @Test
    public void ilsTest() throws IOException, FileNotFoundException, BiffException {
        String path = "/home/renansantos/Área de Trabalho/Excel Instances/";

        Instance instance = new Instance();
        instance.setNumberOfRequests(10)
                .setRequestTimeWindows(10)
                .setInstanceSize("s")
                .setNumberOfNodes(12)
                .setNumberOfVehicles(250)
                .setVehicleCapacity(4);

        VRPDRTSD problem = new VRPDRTSD(instance, path);
//        problem.buildGreedySolution();
//        problem.printSolutionInformations();
//        problem.localSearch(6);
//        problem.printSolutionInformations();
//        problem.ilsForExperiment();
        //new ExperimentalDesign().runIlsExperimentWithExcelData(path);
    }
}
