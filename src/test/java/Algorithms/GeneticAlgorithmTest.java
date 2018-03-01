/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithms;

import InstanceReader.Instance;
import junit.framework.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

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
        
        algorithm.populationInitalization();
        algorithm.getPopulation().forEach(System.out::println);
        
        Assert.assertEquals(algorithm.getPopulationSize(), algorithm.getPopulation().size());
    }

    @Test
    public void testSelection() {
    }

    @Test
    public void testCrossOver() {
    }

    @Test
    public void testMutation() {
    }

}
