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

    public void multiStart();

    public void simulatedAnnealing();

    public void vnd();

    public void vns();

    public void grasp();

    public void ils();

    public void tabuSearch();

}
