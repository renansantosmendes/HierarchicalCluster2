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
public interface Metaheuristic extends Heuristic {

    public void MultiStart();

    public void SimulatedAnnealing();

    public void VND();

    public void VNS();

    public void GRASP();

    public void ILS();

    public void TabuSearch();

}
