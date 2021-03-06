/*

 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VRPDRTSD;

import ProblemRepresentation.Node;
import ProblemRepresentation.ProblemData;
import ProblemRepresentation.Route;
import ProblemRepresentation.Solution;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author renansantos
 */
public class VRPDRTSDTest {

    public VRPDRTSDTest() {
    }

    @Test
    public void testReadInstance() {
        String instanceName = "r050n12tw10";
        String nodesData = "bh_n12s";
        String adjacenciesData = "bh_adj_n12s";
        int numberOfVehicles = 10;
        int vehicleCapacity = 11;
        VRPDRTSD problem = new VRPDRTSD(instanceName, nodesData, adjacenciesData, numberOfVehicles, vehicleCapacity);
        assertEquals(50, problem.getData().getRequests().size());
        assertEquals(12, problem.getData().getNodes().size());
        assertEquals(10, problem.getData().getAvaibleVehicles().size());
        assertEquals(0, problem.getData().getAllocatedVehicles().size());

    }

    @Test
    public void testBuildGreedySolution() {
        String instanceName = "r050n12tw10";
        String nodesData = "bh_n12s";
        String adjacenciesData = "bh_adj_n12s";
        int numberOfVehicles = 10;
        int vehicleCapacity = 11;
        VRPDRTSD problem = new VRPDRTSD(instanceName, nodesData, adjacenciesData, numberOfVehicles, vehicleCapacity);
        //problem.buildGreedySolution();
    }

    @Test
    public void feasibilityTest() {
        String instanceName = "r050n12tw10";
        String nodesData = "bh_n12s";
        String adjacenciesData = "bh_adj_n12s";
        int numberOfVehicles = 10;
        int vehicleCapacity = 11;
        VRPDRTSD problem = new VRPDRTSD(instanceName, nodesData, adjacenciesData, numberOfVehicles, vehicleCapacity);

        problem.originalRequestsFeasibilityAnalysis();
        //problem.getData().getRequests().stream().filter(r -> r.isFeasible()).forEach(System.out::println);

        assertEquals(problem.getData().getNodes().get(0), problem.getData().getCurrentNode());
        assertEquals(50, problem.getData().getRequests()
                .stream().filter(r -> r.isFeasible())
                .collect(Collectors.toCollection(ArrayList::new)).size());
    }

    @Test
    public void actualizeDataTest() {
        String instanceName = "r010n12tw10";
        String nodesData = "bh_n12s";
        String adjacenciesData = "bh_adj_n12s";
        int numberOfVehicles = 10;
        int vehicleCapacity = 11;
        VRPDRTSD problem = new VRPDRTSD(instanceName, nodesData, adjacenciesData, numberOfVehicles, vehicleCapacity);

        Node node = new Node(0, -19.914106, -43.941739, "Av do Contorno, 340 - Santa Efigênia Belo Horizonte - MG");
        List<Node> nodes = new ArrayList<>();
        nodes.addAll(problem.getData().getNodes());
        nodes.remove(node);
        assertEquals(false, nodes.contains(node));
        //nodes.forEach(System.out::println);

    }

    @Test
    public void scheduleRouteTest() {
        String instanceName = "r005n12tw10";
        String nodesData = "bh_n12s";
        String adjacenciesData = "bh_adj_n12s";
        int numberOfVehicles = 10;
        int vehicleCapacity = 11;
        VRPDRTSD problem = new VRPDRTSD(instanceName, nodesData, adjacenciesData, numberOfVehicles, vehicleCapacity);
        problem.buildGreedySolution();

        Solution solution = problem.getSolution();
        int routeNumber = 1;
        List<Integer> integerRepresentation = problem.getSolution().getRoutes().get(routeNumber).getIntegerRouteRepresetation();
        List<Integer> idSequence = integerRepresentation
                .stream()
                .filter(u -> u >= 0)
                .collect(Collectors.toCollection(ArrayList::new));

        Route route = solution.getRoutes().get(routeNumber);

        //System.out.println("before");
        //System.out.println(route);
//        Collections.swap(idSequence, 3, 4);
        route.clearIntegerRepresentation();
        route.setIntegerRouteRepresetation(idSequence);

        // problem.scheduleRoute(route);
        route.buildSequenceOfAttendedRequests(problem.getData());
        route.buildNodesSequence(problem.getData());
        route.evaluateRoute(problem.getData());
        //System.out.println("after");
        //System.out.println(route);
//s        assertEquals(4, problem.getSolution().getRoutes().get(1).getNodesSequence().size());
    }

    @Test
    public void localSearchTest() throws IOException {
        System.out.println("------ Testing local search method ------");
        String instanceName = "r010n12tw10";
        String nodesData = "bh_n12s";
        String adjacenciesData = "bh_adj_n12s";
        int numberOfVehicles = 10;
        int vehicleCapacity = 11;
        VRPDRTSD problem = new VRPDRTSD(instanceName, nodesData, adjacenciesData, numberOfVehicles, vehicleCapacity);

        problem.buildGreedySolution();

        System.out.println("\nBefore local search = " + problem.getSolution());
        problem.getSolution().printAllInformations();
        //problem.getSolution().printIntegerRepresentationOfRoutes();
        problem.localSearch(2);
        System.out.println("\nAfter local search = " + problem.getSolution());
        problem.getSolution().printAllInformations();

        //problem.getSolution().printIntegerRepresentationOfRoutes();
        //problem.getSolution().getStaticMapWithAllRoutes(problem.getData().getNodes(), adjacenciesData, nodesData);
    }

    @Test
    public void swapBestImprovementTest() {
//        System.out.println("------ Testing local search method ------");
//        String instanceName = "r010n12tw10";
//        String nodesData = "bh_n12s";
//        String adjacenciesData = "bh_adj_n12s";
//        int numberOfVehicles = 10;
//        int vehicleCapacity = 11;
//        VRPDRTSD problem = new VRPDRTSD(instanceName, nodesData, adjacenciesData, numberOfVehicles, vehicleCapacity);
//
//        problem.buildGreedySolution();
//
//        Solution solution = problem.getSolution();
//        ProblemData data = problem.getData();
//
//        Route route = new Route(solution.getRoute(0));
//        long evaluationFunctionBeforeMovement = solution.getEvaluationFunction();
//        for (int j = 1; j < route.getIntegerSequenceOfAttendedRequests().size() - 1; j++) {
//            for (int k = j + 1; k < route.getIntegerSequenceOfAttendedRequests().size(); k++) {
//                route.swapRequests(j, k, data);
//                solution.setRoute(0, route);
//                solution.calculateEvaluationFunction();
//                long evaluationFunctionAfterMovement = solution.getEvaluationFunction();
//                if (evaluationFunctionAfterMovement > evaluationFunctionBeforeMovement) {
//                    route.swapRequests(j, k, data);
//                }
//            }
//        }
//        assertEquals(272, route.getTotalTimeWindowAnticipation());
    }

    @Test
    public void addAndRemoveTimeTest() {
        System.out.println("------ Testing local search method ------");
        System.out.println("-------- Testing time alteration --------");
        String instanceName = "r010n12tw10";
        String nodesData = "bh_n12s";
        String adjacenciesData = "bh_adj_n12s";
        int numberOfVehicles = 10;
        int vehicleCapacity = 11;
        VRPDRTSD problem = new VRPDRTSD(instanceName, nodesData, adjacenciesData, numberOfVehicles, vehicleCapacity);

        //problem.buildGreedySolution();
        //System.out.println("\nBefore local search = " + problem.getSolution());
        //problem.getSolution().printIntegerRepresentationOfRoutes();
        //problem.localSearch(2);
        //problem.localSearch(5);
        //System.out.println("\nAfter local search = " + problem.getSolution());
    }

    @Test
    public void interRouteMovimentsTest() {
        System.out.println("------ Testing local search method ------");
        String instanceName = "r010n12tw10";
        String nodesData = "bh_n12s";
        String adjacenciesData = "bh_adj_n12s";
        int numberOfVehicles = 10;
        int vehicleCapacity = 11;
        VRPDRTSD problem = new VRPDRTSD(instanceName, nodesData, adjacenciesData, numberOfVehicles, vehicleCapacity);

        problem.buildGreedySolution();

//        System.out.println(problem.getSolution());
        problem.getSolution().printAllInformations();
        problem.localSearch(8);
        problem.localSearch(2);
        // problem.localSearch(4);
//        System.out.println(problem.getSolution());
        problem.getSolution().printAllInformations();
    }

    @Test
    public void buildRandomSolutionTest() throws IOException {
        System.out.println("------ Testing Random Solution ------");
        String instanceName = "r010n12tw10";
        String nodesData = "bh_n12s";
        String adjacenciesData = "bh_adj_n12s";
        int numberOfVehicles = 250;
        int vehicleCapacity = 1;

        VRPDRTSD problem = new VRPDRTSD(instanceName, nodesData, adjacenciesData, numberOfVehicles, vehicleCapacity);
//        problem.buildRandomSolution();
        //problem.buildSelfishSolution();
        //problem.getSolution().printAllInformations();
        //problem.getSolution().getStaticMapWithAllRoutes(problem.getData().getNodes(), adjacenciesData, nodesData);

    }

    @Test
    public void requestReallocationTest() throws IOException {
        System.out.println("------ Testing Request Reallocation ------");
        String instanceName = "r010n12tw10";
        String nodesData = "bh_n12s";
        String adjacenciesData = "bh_adj_n12s";
        int numberOfVehicles = 250;
        int vehicleCapacity = 11;

        VRPDRTSD problem = new VRPDRTSD(instanceName, nodesData, adjacenciesData, numberOfVehicles, vehicleCapacity);
        List<Integer> idSequence = new ArrayList<>();
        idSequence.add(0);
        idSequence.add(1);
        idSequence.add(2);
        idSequence.add(1);
        idSequence.add(2);
        idSequence.add(0);

        int requestId = 3;
        int l = 2;
        int m = 3;
        List<Integer> newIdSequence = new ArrayList<>();

        newIdSequence.addAll(idSequence.subList(0, l));
        newIdSequence.add(requestId);
        newIdSequence.addAll(idSequence.subList(l, m - 1));
        newIdSequence.add(requestId);
        newIdSequence.addAll(idSequence.subList(m - 1, idSequence.size()));

        problem.buildGreedySolution();
        System.out.println(problem.getSolution());
//        problem.localSearch(10);
//        System.out.println(problem.getSolution());
//        problem.localSearch(8);
//        System.out.println(problem.getSolution());
//        problem.localSearch(2);
//        System.out.println(problem.getSolution());
//        problem.localSearch(4);
//        System.out.println(problem.getSolution());
        problem.localSearch(11);
    }

    @Test
    public void requestFeasibilityTest() {
        System.out.println("------ Testing Request Feasibility ------");
        String instanceName = "r010n12tw10";
        String nodesData = "bh_n12s";
        String adjacenciesData = "bh_adj_n12s";
        int numberOfVehicles = 250;
        int vehicleCapacity = 11;

        VRPDRTSD problem = new VRPDRTSD(instanceName, nodesData, adjacenciesData, numberOfVehicles, vehicleCapacity);
        problem.buildGreedySolution();
        problem.getSolution().printAllInformations();
//        problem.localSearch(10);
//        problem.getSolution().printAllInformations();
//        problem.localSearch(4);
//        problem.getSolution().printAllInformations();
    }

    @Test
    public void addingRoutesTest() {
        System.out.println("------ Testing ADDROUTES movement ------");
        String instanceName = "r010n12tw10";
        String nodesData = "bh_n12s";
        String adjacenciesData = "bh_adj_n12s";
        int numberOfVehicles = 250;
        int vehicleCapacity = 11;

        VRPDRTSD problem = new VRPDRTSD(instanceName, nodesData, adjacenciesData, numberOfVehicles, vehicleCapacity);
//        problem.buildGreedySolution();
//        problem.getSolution().printAllInformations();
        //problem.localSearch(10);
        //problem.perturbation(1, 1);
        //problem.getSolution().printAllInformations();
        for (int i = 0; i < 10; i++) {
            problem.buildGreedySolution();
            //System.out.println(problem.getSolution());
            //problem.perturbation(3, 1);
//            problem.localSearch(8);
            //   System.out.println(problem.getSolution());
            //     problem.getSolution().printAllInformations();
        }

    }
    
    
    @Test
    public void newBuildGreedySolution(){
        System.out.println("------ Testing newBuildGreedySolution ------");
        String instanceName = "r010n12tw10";
        String nodesData = "bh_n12s";
        String adjacenciesData = "bh_adj_n12s";
        int numberOfVehicles = 250;
        int vehicleCapacity = 11;

        VRPDRTSD problem = new VRPDRTSD(instanceName, nodesData, adjacenciesData, numberOfVehicles, vehicleCapacity);
        problem.buildGreedySolution();
        problem.getSolution().printAllInformations();
        
    }
}
