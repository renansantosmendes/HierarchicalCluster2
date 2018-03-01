/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithms;

/**
 *
 * @author renansantos
 */
public interface EvolutionaryAlgorithms {

    public void populationInitalization();
    
    public void selection();

    public void crossOver();

    public void mutation();
}
