package VRPDRTSD;
/**
 *
 * @author renansantos
 */
public class Main {

    public static void main(String[] args) {
        String instanceName = "r050n12tw10";
        String nodesData = "bh_n12s";
        String adjacenciesData = "bh_adj_n12s";
        int numberOfVehicles = 50;
        int vehicleCapacity = 11;
        System.out.println("Testing VRPDRTSD class");
        VRPDRTSD problem = new VRPDRTSD(instanceName, nodesData, adjacenciesData, numberOfVehicles, vehicleCapacity);
        
        
        problem.buildGreedySolution();
        System.out.println(problem.getSolution());
    }

}
