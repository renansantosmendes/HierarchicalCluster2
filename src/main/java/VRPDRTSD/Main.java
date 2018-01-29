package VRPDRTSD;

import InstanceReader.*;
import java.io.*;
import jxl.read.biff.BiffException;

/**
 *
 * @author renansantos
 */
public class Main {

    public static void main(String[] args) throws IOException, FileNotFoundException, BiffException {
        String path = "/home/rmendes/VRPDRT/";
        new ScriptGenerator("VNS","3d","small");
        new ExperimentalDesign().runVnsExperimentWithExcelData(path);
    }
}
