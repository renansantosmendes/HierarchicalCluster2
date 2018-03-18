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
//        String path = "/home/renansantos/Área de Trabalho/Excel Instances/";
        new ScriptGenerator("GeneticAlgorithm", "3d", "large");
        new ExperimentalDesign().runGeneticAlgorithmExperimentWithExcelData(path);

//        int requestsNumber = 10;
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
//        problem.buildGreedySolution();
//        problem.getSolution().getStaticMapWithAllRoutes(problem.getData().getNodes(),
//                problem.getData().getAdjacenciesInstanceName(),
//                problem.getData().getNodesInstanceName());
//        problem.vnd();

//        problem.getSolution().printAllInformations();
//        problem.multiStart();
//        problem.getSolution().getStaticMapForEveryRoute(problem.getData().getNodes(), 
//                problem.getData().getAdjacenciesInstanceName(), 
//                problem.getData().getNodesInstanceName());
//        problem.getSolution().getStaticMapWithAllRoutes(problem.getData().getNodes(),
//                problem.getData().getAdjacenciesInstanceName(),
//                problem.getData().getNodesInstanceName());
    }
}
