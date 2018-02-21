/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InstanceReader;

import ProblemRepresentation.Solution;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

/**
 *
 * @author renansantos
 */
public class DataOutput {

    private String algorithmName;
    private String path;
    private String fileName;
    private PrintStream streamForTxt;
    private PrintStream streamForSolutions; 
    private PrintStream streamForBestSolutions;
    private PrintStream streamForBestSolutionsObjectiveFunctions;
    private PrintStream streamForConvergence;
    private PrintStream streamForCsv;

    public DataOutput(String algorithmName, String instanceName, int execution) throws FileNotFoundException {
        this.algorithmName = algorithmName;
        this.path = "AlgorithmsResults//" + algorithmName + "//" + instanceName + "//";
        this.fileName = this.algorithmName + "_execution_" + execution;
        initalizePathAndFiles();
        initalizeStreams();
    }

    public DataOutput(String algorithmName, String instanceName) throws FileNotFoundException {
        this.algorithmName = algorithmName;
        this.path = "AlgorithmsResults//" + algorithmName + "//" + instanceName + "//";
        this.fileName = this.algorithmName;
        initalizePathAndFiles();
        initalizeStreams();
    }

    private void initalizePathAndFiles() {
        boolean success = (new File(this.path)).mkdirs();
        if (!success) {
            //System.out.println("Folder already exists!");
        }
    }

    private void initalizeStreams() throws FileNotFoundException {
        streamForTxt = new PrintStream(path + "/" + fileName + ".txt");
        streamForSolutions = new PrintStream(path + "/" + fileName + "_Solutions.txt");
        streamForBestSolutions = new PrintStream(path + "/" + fileName + "_BEST_Solutions.txt");
        streamForBestSolutionsObjectiveFunctions = new PrintStream(path + "/" + fileName + "_BEST_Solutions_Objectives.txt");
        streamForConvergence = new PrintStream(path + "/" + fileName + "_Convergence.txt");
        //streamForCsv  = new PrintStream(path + "/" + fileName + ".csv");
    }

    public void saveBestSolutionInTxtFile(Solution solution, int currentIteration) {
        this.streamForTxt.print(currentIteration + "\t" + solution + "\n");
        this.streamForSolutions.print(solution.getIntegerRepresentation() + "\n");
        this.streamForConvergence.print(solution.getEvaluationFunction() + "\n");
    }

    public void saveBestSolutionFoundInTxtFile(Solution solution, int currentIteration) {
        this.streamForTxt.print(currentIteration + "\t" + solution + "\n");
        this.streamForSolutions.print(solution.getIntegerRepresentation() + "\n");
        this.streamForConvergence.print(solution.getEvaluationFunction() + "\n");
    }

    public void saveBestSolutionFoundInTxtFile(Solution solution) {
        this.streamForBestSolutions.print(solution.getIntegerRepresentation() + "\n");
        this.streamForBestSolutionsObjectiveFunctions.print(solution + "\n");
    }

}
