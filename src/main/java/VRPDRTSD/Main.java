package VRPDRTSD;

import InstanceReader.*;
import java.io.*;
import jxl.read.biff.BiffException;

/**
 *
 * @author renansantos
 */
public class Main {

    public static void main(String[] args) throws IOException, FileNotFoundException, BiffException {
        String path = "/home/rmendes/VRPDRT/";
        new ScriptGenerator("SimulatedAnnealing","3d","small");
        new ExperimentalDesign().runSimulatedAnnealingExperimentWithExcelData(path);
        
        
//        int requestsNumber = 5;
//        String path = "/home/renansantos/Área de Trabalho/Excel Instances/";
//
//        Instance instance = new Instance();
//        instance.setNumberOfRequests(requestsNumber)
//                .setRequestTimeWindows(10)
//                .setInstanceSize("s")
//                .setNumberOfNodes(12)
//                .setNumberOfVehicles(250)
//                .setVehicleCapacity(4);
//
//        VRPDRTSD problem = new VRPDRTSD(instance, path);
//        problem.vns();
////        problem.getSolution().getStaticMapForEveryRoute(problem.getData().getNodes(), 
////                problem.getData().getAdjacenciesInstanceName(), 
////                problem.getData().getNodesInstanceName());
//        problem.getSolution().getStaticMapWithAllRoutes(problem.getData().getNodes(), 
//                problem.getData().getAdjacenciesInstanceName(), 
//                problem.getData().getNodesInstanceName());
    }
}
