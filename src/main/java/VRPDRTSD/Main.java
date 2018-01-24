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
        new ScriptGenerator("MultiStart","2d","small");
        new ExperimentalDesign().runMultiStartExperimentWithExcelData(path);
    }
}
