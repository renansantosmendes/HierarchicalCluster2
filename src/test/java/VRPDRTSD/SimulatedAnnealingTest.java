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
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author renansantos
 */
public class SimulatedAnnealingTest {

    @Test
    public void SA_Test() throws FileNotFoundException, IOException, BiffException {
        int requestsNumber = 10;

        Instance instance = new Instance();
        instance.setNumberOfRequests(requestsNumber)
                .setRequestTimeWindows(10)
                .setInstanceSize("s")
                .setNumberOfNodes(12)
                .setNumberOfVehicles(250)
                .setVehicleCapacity(4);

//        VRPDRTSD problem = new VRPDRTSD(instance, "/home/renansantos/Área de Trabalho/Excel Instances/");
        //VRPDRTSD problem = new VRPDRTSD(instance, "/home/renansantos/Área de Trabalho/Excel Instances/");

        //problem.simulatedAnnealing();
        //new ExperimentalDesign().runSimulatedAnnealingExperimentWithExcelData("/home/renansantos/Área de Trabalho/Excel Instances/");
    }
}
