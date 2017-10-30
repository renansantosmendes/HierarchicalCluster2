/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProblemRepresentation;

import VRPDRTSD.VRPDRTSD;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author renansantos
 */
public class RouteTest {

    public RouteTest() {
    }

    @Test
    public void testBuildSequenceOfAttendedRequests() {
        String instanceName = "r010n12tw10";
        String nodesData = "bh_n12s";
        String adjacenciesData = "bh_adj_n12s";
        int numberOfVehicles = 10;
        int vehicleCapacity = 11;
        System.out.println("Testing VRPDRTSD class");
        VRPDRTSD problem = new VRPDRTSD(instanceName, nodesData, adjacenciesData, numberOfVehicles, vehicleCapacity);

        //problem.buildGreedySolution();
        Route route = new Route();
        List<Integer> sequence = new ArrayList<>();
        sequence.add(0);
        sequence.add(-vehicleCapacity);
        sequence.add(-426);
        sequence.add(3);
        sequence.add(-432);
        sequence.add(4);
        sequence.add(-432);
        sequence.add(3);
        sequence.add(-445);
        sequence.add(4);
        sequence.add(-445);
        sequence.add(0);
        route.setIntegerRouteRepresetation(sequence);
        route.buildSequenceOfAttendedRequests(problem.getData());

        List<Integer> sequenceOfAttendedRequests = sequence.stream().filter(u -> u.longValue() > 0)
                .collect(Collectors.toCollection(ArrayList::new));
        List<Request> sequenceOfRequests = new ArrayList<>();

        for (Integer id : sequenceOfAttendedRequests) {
            Request request = problem.getData().getRequests().stream().filter(u -> u.getId() == id).findAny().get();
            sequenceOfRequests.add(request);
        }

        //sequenceOfRequests.forEach(System.out::println);

        assertEquals(sequenceOfRequests, route.getSequenceOfAttendedRequests());
        //System.out.println(sequenceOfAttendedRequests);
    }

    @Test
    public void testBuildNodesSequence() {

        String instanceName = "r010n12tw10";
        String nodesData = "bh_n12s";
        String adjacenciesData = "bh_adj_n12s";
        int numberOfVehicles = 10;
        int vehicleCapacity = 11;
        System.out.println("Testing BUILD NODES SEQUENCE");
        VRPDRTSD problem = new VRPDRTSD(instanceName, nodesData, adjacenciesData, numberOfVehicles, vehicleCapacity);

        Route route = new Route();
        List<Integer> sequence = new ArrayList<>();
        sequence.add(0);
        sequence.add(-vehicleCapacity);
        sequence.add(-426);
        sequence.add(3);
        sequence.add(-432);
        sequence.add(4);
        sequence.add(-432);
        sequence.add(3);
        sequence.add(-445);
        sequence.add(4);
        sequence.add(-445);
        sequence.add(0);
        route.setIntegerRouteRepresetation(sequence);
        route.buildSequenceOfAttendedRequests(problem.getData());
        route.buildNodesSequence(problem.getData());
        System.out.println("Testing ----");
        //route.getSequenceOfAttendedRequests().forEach(System.out::println);
        //route.getNodesSequence().forEach(System.out::println);
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

        Route route = solution.getRoutes().get(routeNumber);
        System.out.println("\n\nTesting Route Swapping --- \n\nBefore Movement\n" + route);
        route.swapRequests(3, 4, problem.getData());
        
        System.out.println("After Movement\n" + route);
        assertEquals(7, route.getNodesSequence().size());
    }
    
    @Test
    public void minutesAddedAndRemovedTest(){
        String instanceName = "r010n12tw10";
        String nodesData = "bh_n12s";
        String adjacenciesData = "bh_adj_n12s";
        int numberOfVehicles = 10;
        int vehicleCapacity = 11;
        VRPDRTSD problem = new VRPDRTSD(instanceName, nodesData, adjacenciesData, numberOfVehicles, vehicleCapacity);
        problem.buildGreedySolution();

        Solution solution = problem.getSolution();
        int routeNumber = 1;

        Route route = solution.getRoutes().get(routeNumber);
        System.out.println("\n\nTesting Route Scheduling --- \n\nBefore Movement\n" + route);
        route.addMinutesInRoute(30, problem.getData());
        
        System.out.println("After Movement\n" + route);
        assertEquals(5, route.getNodesSequence().size());
    }
}
