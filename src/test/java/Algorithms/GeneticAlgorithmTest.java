/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithms;

import InstanceReader.Instance;
import ProblemRepresentation.Route;
import ProblemRepresentation.Solution;
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
                .setNumberOfGenerations(1000)
                .setNumberOfExecutions(30)
                .setPopulationSize(20);

        //algorithm.initializePopulation();
//        algorithm.getPopulation().forEach(System.out::println);
//        Assert.assertEquals(algorithm.getPopulationSize(), algorithm.getPopulation().size());
    }

    @Test
    public void testSelection() {
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
                .setNumberOfGenerations(1000)
                .setNumberOfExecutions(30)
                .setPopulationSize(10);

//        algorithm.initializePopulation();
//        algorithm.selection();
//        algorithm.mutation();
        //System.out.println(algorithm.getParents());
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
                .setNumberOfGenerations(1000)
                .setNumberOfExecutions(30)
                .setPopulationSize(100);

        //algorithm.initializePopulation();
        //algorithm.selection();
        //algorithm.printPopulation();
        //algorithm.mutation();
        //algorithm.crossOver();
        //algorithm.mutation();
        //algorithm.calculateFitness();
        //algorithm.printPopulation();
    }

    @Test
    public void geneticAlgorithmTest() {
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
                .setNumberOfGenerations(10)
                .setNumberOfExecutions(2)
                .setPopulationSize(100);

//        algorithm.runWithLocalSearch(); 
        algorithm.runExperiment();
        algorithm.printPopulation();
        algorithm.getBestIndividual().printAllInformations();
    }
}
