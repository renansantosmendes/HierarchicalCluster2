/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InstanceReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

/**
 *
 * @author renansantos
 */
public class ScriptGenerator {
    private String instance;
    private String instanceFullName;
    private String fileName;
    private String folder = "scripts_for_cluster_executions";

    public ScriptGenerator(Instance instance) {
        this.instance = instance.getInstanceName();
        this.instanceFullName = this.instance;
        this.fileName = "script_" + this.instanceFullName + ".sh";
        createFolder();
    }

    public ScriptGenerator(String algorithmName, String timeOfExecution, String partition) throws FileNotFoundException{
        this.fileName = algorithmName;
        createFolder();
        this.generateScriptForExperiment(timeOfExecution, partition);
    }
    
    private void createFolder() {
        boolean success = (new File(folder)).mkdirs();
        if (!success) {
            System.out.println("Folder already exists!");
        }
    }

    public void generate(String timeOfExecution, String partition) throws FileNotFoundException {
        PrintStream printStream = new PrintStream(folder + "/" + fileName);
        printStream.print("#!/bin/bash \n");
        printStream.print("#SBATCH --qos=part" + timeOfExecution + "\n");
        printStream.print("#SBATCH --partition=" + partition + "\n");
        printStream.print("module load jdk8_32" + "\n");
        printStream.print("java -jar " + this.instanceFullName + ".jar");
    }
    
    public void generateScriptForExperiment(String timeOfExecution, String partition) throws FileNotFoundException {
        PrintStream printStream = new PrintStream(folder + "/" + fileName + ".sh");
        printStream.print("#!/bin/bash \n");
        printStream.print("#SBATCH --qos=part" + timeOfExecution + "\n");
        printStream.print("#SBATCH --partition=" + partition + "\n");
        printStream.print("module load jdk8_32" + "\n");
        printStream.print("java -jar " + this.fileName + ".jar");
    }
}
