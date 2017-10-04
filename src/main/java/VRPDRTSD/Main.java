package VRPDRTSD;
/**
 *
 * @author renansantos
 */
public class Main {

    public static void main(String[] args) {
        String instanceName = "r150n12tw10";
        String nodesData = "bh_n12s";
        String adjacenciesData = "bh_adj_n12s";
        int numberOfVehicles = 50;
        int vehicleCapacity = 11;
        System.out.println("Testing VRPDRTSD class");
        VRPDRTSD problem = new VRPDRTSD(instanceName, nodesData, adjacenciesData, numberOfVehicles, vehicleCapacity);
        
        
        problem.buildGreedySolution();
        //problem.getData().getInstanceRequests().forEach(System.out::println);
       // problem.getData().getAllocatedVehicles().forEach(System.out::println);
    }

}
