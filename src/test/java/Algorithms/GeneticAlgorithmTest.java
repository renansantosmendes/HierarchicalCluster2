/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithms;

import InstanceReader.Instance;
import ProblemRepresentation.Route;
import java.util.*;
import junit.framework.Assert;
import org.junit.Test;
/**
 *
 * @author renansantos
 */
public class GeneticAlgorithmTest {

    public GeneticAlgorithmTest() {
    }

    @Test
    public void testPopulationInitalization() {

        String path = "/home/renansantos/Área de Trabalho/Excel Instances/";

        Instance instance = new Instance();
        instance.setNumberOfRequests(10)
                .setRequestTimeWindows(10)
                .setInstanceSize("s")
                .setNumberOfNodes(12)
                .setNumberOfVehicles(250)
                .setVehicleCapacity(4);

        GeneticAlgorithm algorithm = new GeneticAlgorithm(instance, path);
        algorithm.setCrossOverProbability(0.7)
                .setMutationProbabilty(0.02)
                .setNumberOfIterations(1000)
                .setPopulationSize(20);
        
        algorithm.initializePopulation();
//        algorithm.getPopulation().forEach(System.out::println);
        
        Assert.assertEquals(algorithm.getPopulationSize(), algorithm.getPopulation().size());
    }

    @Test
    public void testSelection() {
        String path = "/home/renansantos/Área de Trabalho/Excel Instances/";

        Instance instance = new Instance();
        instance.setNumberOfRequests(100)
                .setRequestTimeWindows(10)
                .setInstanceSize("s")
                .setNumberOfNodes(12)
                .setNumberOfVehicles(250)
                .setVehicleCapacity(4);

        GeneticAlgorithm algorithm = new GeneticAlgorithm(instance, path);
        algorithm.setCrossOverProbability(0.7)
                .setMutationProbabilty(0.02)
                .setNumberOfIterations(1000)
                .setPopulationSize(10);
        
        algorithm.initializePopulation();
        algorithm.selection();
        algorithm.mutation();
        System.out.println(algorithm.getParents());
        //Assert.assertEquals(algorithm.getParents().size(), algorithm.getPopulationSize());
    }

    @Test
    public void testCrossOver() {
        String path = "/home/renansantos/Área de Trabalho/Excel Instances/";

        Instance instance = new Instance();
        instance.setNumberOfRequests(10)
                .setRequestTimeWindows(10)
                .setInstanceSize("s")
                .setNumberOfNodes(12)
                .setNumberOfVehicles(250)
                .setVehicleCapacity(4);

        GeneticAlgorithm algorithm = new GeneticAlgorithm(instance, path);
        algorithm.setCrossOverProbability(0.7)
                .setMutationProbabilty(0.02)
                .setNumberOfIterations(1000)
                .setPopulationSize(10);
        
        algorithm.initializePopulation();
        algorithm.selection();
        algorithm.printPopulation();
        //algorithm.mutation();
        algorithm.crossOver();
        algorithm.printPopulation();
        
    }
    
    @Test
    public void testMutation() {
    
    }

//    @Test
//    public void testingSolutionsMethods(){
//        String path = "/home/renansantos/Área de Trabalho/Excel Instances/";
//
//        Instance instance = new Instance();
//        instance.setNumberOfRequests(10)
//                .setRequestTimeWindows(10)
//                .setInstanceSize("s")
//                .setNumberOfNodes(12)
//                .setNumberOfVehicles(250)
//                .setVehicleCapacity(4);
//
//        GeneticAlgorithm algorithm = new GeneticAlgorithm(instance, path);
//        algorithm.setCrossOverProbability(0.7)
//                .setMutationProbabilty(0.02)
//                .setNumberOfIterations(1000)
//                .setPopulationSize(100);
//        
//        algorithm.getProblem().buildGreedySolution();
//        
//        List<Integer> list = new ArrayList<>();
//        list.add(3);
//        list.add(4);
//        list.add(10);
//        list.add(5);
        
//        algorithm.getProblem().getSolution().removeSequenceFromAllSolution(list,0, algorithm.getProblem().getData());
//        Route route = new Route(algorithm.getProblem().getSolution().getRoute(0));
//        System.out.println("Route test = " + route.getIntegerSequenceOfAttendedRequests());
//        algorithm.getProblem().getSolution().printAllInformations();
//    }
}
