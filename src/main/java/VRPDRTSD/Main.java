package VRPDRTSD;

import InstanceReader.Instance;
import InstanceReader.ScriptGenerator;
import java.io.FileNotFoundException;
import java.io.IOException;
import jxl.read.biff.BiffException;

/**
 *
 * @author renansantos
 */
public class Main {

    public static void main(String[] args) throws IOException, FileNotFoundException, BiffException {
        String path = "/home/rmendes/VRPDRT/";
        new ScriptGenerator("SimulatedAnnealing","3d","small");
        new ExperimentalDesign().runSimulatedAnnealingExperimentWithExcelData(path);
    }
}
