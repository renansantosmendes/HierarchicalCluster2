/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VRPDRTSD;

import InstanceReader.Instance;
import java.io.FileNotFoundException;
import java.io.IOException;
import jxl.read.biff.BiffException;

/**
 *
 * @author renansantos
 */
public class ExperimentalDesign {

    private int requestNumber[] = {10, 50, 100, 150, 200};//250
    private int timeWindows[] = {10};//5
    private int vehicleCapacities[] = {4, 11};
    private String instanceSizes[] = {"s", "m", "l"};
    private Instance instance = new Instance();

    public void runMultiStartExperiment() throws FileNotFoundException {
        for (int i = 0; i < requestNumber.length; i++) {
            for (int j = 0; j < timeWindows.length; j++) {
                instance = new Instance();
                instance.setNumberOfRequests(requestNumber[i])
                        .setRequestTimeWindows(timeWindows[j])
                        .setInstanceSize("s")
                        .setNumberOfNodes(12)
                        .setNumberOfVehicles(250)
                        .setVehicleCapacity(4);

                VRPDRTSD problem = new VRPDRTSD(instance);
                System.out.println(instance);
                problem.multiStartForExperiment();
            }
        }
    }
    
    public void runMultiStartExperimentWithExcelData(String path) throws FileNotFoundException, IOException, BiffException {
        for (int i = 0; i < requestNumber.length; i++) {
            for (int j = 0; j < timeWindows.length; j++) {
                instance = new Instance();
                instance.setNumberOfRequests(requestNumber[i])
                        .setRequestTimeWindows(timeWindows[j])
                        .setInstanceSize("s")
                        .setNumberOfNodes(12)
                        .setNumberOfVehicles(250)
                        .setVehicleCapacity(4);

                VRPDRTSD problem = new VRPDRTSD(instance, path);
                System.out.println(instance);
                problem.multiStartForExperiment();
            }
        }
    }
    
    public void runSimulatedAnnealingExperiment() throws FileNotFoundException {
        for (int i = 0; i < requestNumber.length; i++) {
            for (int j = 0; j < timeWindows.length; j++) {
                instance = new Instance();
                instance.setNumberOfRequests(requestNumber[i])
                        .setRequestTimeWindows(timeWindows[j])
                        .setInstanceSize("s")
                        .setNumberOfNodes(12)
                        .setNumberOfVehicles(250)
                        .setVehicleCapacity(4);

                VRPDRTSD problem = new VRPDRTSD(instance);
                System.out.println(instance);
                problem.simulatedAnnealingForExperiment();
            }
        }
    }
    
    public void runSimulatedAnnealingExperimentWithExcelData(String path) throws FileNotFoundException, IOException, BiffException {
        for (int i = 0; i < requestNumber.length; i++) {
            for (int j = 0; j < timeWindows.length; j++) {
                instance = new Instance();
                instance.setNumberOfRequests(requestNumber[i])
                        .setRequestTimeWindows(timeWindows[j])
                        .setInstanceSize("s")
                        .setNumberOfNodes(12)
                        .setNumberOfVehicles(250)
                        .setVehicleCapacity(4);

                VRPDRTSD problem = new VRPDRTSD(instance, path);
                System.out.println(instance);
                problem.simulatedAnnealingForExperiment();
            }
        }
    }
    
    public void runVnsExperiment() throws FileNotFoundException {
        for (int i = 0; i < requestNumber.length; i++) {
            for (int j = 0; j < timeWindows.length; j++) {
                instance = new Instance();
                instance.setNumberOfRequests(requestNumber[i])
                        .setRequestTimeWindows(timeWindows[j])
                        .setInstanceSize("s")
                        .setNumberOfNodes(12)
                        .setNumberOfVehicles(250)
                        .setVehicleCapacity(4);

                VRPDRTSD problem = new VRPDRTSD(instance);
                System.out.println(instance);
                problem.vnsForExperiment();
            }
        }
    }
    
    public void runVnsExperimentWithExcelData(String path) throws FileNotFoundException, IOException, BiffException {
        for (int i = 0; i < requestNumber.length; i++) {
            for (int j = 0; j < timeWindows.length; j++) {
                instance = new Instance();
                instance.setNumberOfRequests(requestNumber[i])
                        .setRequestTimeWindows(timeWindows[j])
                        .setInstanceSize("s")
                        .setNumberOfNodes(12)
                        .setNumberOfVehicles(250)
                        .setVehicleCapacity(4);

                VRPDRTSD problem = new VRPDRTSD(instance, path);
                System.out.println(instance);
                problem.vnsForExperiment();
            }
        }
    }
    
}
