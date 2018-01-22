package VRPDRTSD;

import InstanceReader.Instance;
import InstanceReader.ScriptGenerator;
import java.io.IOException;

/**
 *
 * @author renansantos
 */
public class Main {

    public static void main(String[] args) throws IOException {
        new ScriptGenerator("MultiStart","1d","small");
        new ExperimentalDesign().runMultiStartExperiment();
    }
}
