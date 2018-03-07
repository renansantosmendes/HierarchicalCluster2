/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VRPDRTSD;

import InstanceReader.*;
import java.io.*;
import jxl.read.biff.BiffException;
import org.junit.Test;

/**
 *
 * @author renansantos
 */
public class AlgorithmTest {

    @Test
    public void algorithmTests() throws IOException, FileNotFoundException, BiffException {
        int requestsNumber = 50;
        String path = "/home/renansantos/Área de Trabalho/Excel Instances/";
//
        Instance instance = new Instance();
        instance.setNumberOfRequests(requestsNumber)
                .setRequestTimeWindows(10)
                .setInstanceSize("s")
                .setNumberOfNodes(12)
                .setNumberOfVehicles(250)
                .setVehicleCapacity(4);

        VRPDRTSD problem = new VRPDRTSD(instance, path);
//        problem.vnsForExperiment();
//        problem.multiStartForExperiment();
//        String path = "/home/rmendes/VRPDRT/";
//        new ScriptGenerator("SimulatedAnnealing", "1d", "medium");
        new ExperimentalDesign().runMultiStartExperimentWithExcelData(path);
    }
}
