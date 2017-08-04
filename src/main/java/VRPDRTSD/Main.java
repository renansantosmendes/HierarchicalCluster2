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
        System.out.println("Testing VRPDRTSD class");
        VRPDRTSD problem = new VRPDRTSD(instanceName, nodesData, adjacenciesData);
        
        problem.buildGreedySolution();
        //problem.getData().getInstanceRequests().forEach(System.out::println);
    }

}
