/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithms;

import ProblemRepresentation.*;
import java.util.*;

/**
 *
 * @author renansantos
 */
public class GeneticAlgorithm implements EvolutionaryAlgorithms {

    private List<SolutionForEA> population;
    private double mutationProbabilty;
    private double crossOverProbability;
    private long populationSize;
    private long numberOfIterations;

    public GeneticAlgorithm() {
        this.population = new ArrayList<>();
    }

    public GeneticAlgorithm setMutationProbabilty(double mutationProbabilty) {
        this.mutationProbabilty = mutationProbabilty;
        return this;
    }

    public GeneticAlgorithm setCrossOverProbability(double crossOverProbability) {
        this.crossOverProbability = crossOverProbability;
        return this;
    }

    public GeneticAlgorithm setPopulationSize(long populationSize) {
        this.populationSize = populationSize;
        return this;
    }

    public GeneticAlgorithm setNumberOfIterations(long numberOfIterations) {
        this.numberOfIterations = numberOfIterations;
        return this;
    }

    @Override
    public void populationInitalization() {

    }

    @Override
    public void selection() {

    }

    @Override
    public void crossOver() {

    }

    @Override
    public void mutation() {

    }

}
