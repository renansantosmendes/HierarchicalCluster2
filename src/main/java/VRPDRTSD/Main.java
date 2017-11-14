package VRPDRTSD;

import java.io.IOException;

/**
 *
 * @author renansantos
 */
public class Main {

    public static void main(String[] args) throws IOException {
        String instanceName = "r050n12tw10";
        String nodesData = "bh_n12s";
        String adjacenciesData = "bh_adj_n12s";
        int numberOfVehicles = 50;
        int vehicleCapacity = 11;
        System.out.println("Testing VRPDRTSD class");
        VRPDRTSD problem = new VRPDRTSD(instanceName, nodesData, adjacenciesData, numberOfVehicles, vehicleCapacity);

        problem.buildGreedySolution();
        System.out.println(problem.getSolution());
//        problem.getSolution().getStaticMapWithAllRoutes(problem.getData().getNodes(), adjacenciesData, nodesData);
//        problem.localSearch(2);
//        problem.getSolution().printAllInformations();
//        problem.localSearch(4);
//        problem.getSolution().printAllInformations();
//        problem.getSolution().getStaticMapWithAllRoutes(problem.getData().getNodes(), adjacenciesData, nodesData);
    }
}
