/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InstanceReader;

import ProblemRepresentation.Node;
import ProblemRepresentation.Request;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

/**
 *
 * @author renansantos
 */
public class ReadDataInExcelFile {

    private String filePath;
    private String requestsData;
    private String nodesData;
    private String adjacenciesData;
    private String requestsFile = "instances.xls";
    private String nodesFile = "nodes.xls";
    private String adjacenciesFile = "adjacencies.xls";
    private int numberOfNodes = 0;

    public ReadDataInExcelFile(String filePath) {
        this.filePath = filePath;
    }

    public ReadDataInExcelFile(String filePath, Instance instance) {
        this.filePath = filePath;
        instance.buildInstaceNames();
        this.requestsData = instance.getInstanceName();
        this.nodesData = instance.getNodesData();
        this.adjacenciesData = instance.getAdjacenciesData();
    }

    public ReadDataInExcelFile(String filePath, String requestsData, String nodesData, String adjacenciesData) {
        this.filePath = filePath;
        this.requestsData = requestsData;
        this.nodesData = nodesData;
        this.adjacenciesData = adjacenciesData;
    }

    public void saveData(List<Request> requests) {
        requests.forEach(r -> System.out.println(r.getStringToFile()));
    }

    public List<Request> getRequests() throws IOException, BiffException {
        WorkbookSettings conf = new WorkbookSettings();
        conf.setEncoding("ISO-8859-1");
        Workbook workbook = Workbook.getWorkbook(new File(this.filePath + this.requestsFile), conf);
        Sheet sheet = workbook.getSheet(requestsData);
        int rows = sheet.getRows();
        int columns = sheet.getColumns();

        List<Request> requests = new ArrayList<>();

        for (int i = 1; i < rows; i++) {
            Cell id = sheet.getCell(0, i);
            Cell origin = sheet.getCell(1, i);
            Cell destination = sheet.getCell(2, i);
            Cell pickupTimeWindowLower = sheet.getCell(3, i);
            Cell pickupTimeWindowUpper = sheet.getCell(4, i);
            Cell deliveryTimeWindowLower = sheet.getCell(5, i);
            Cell deliveryTimeWindowUpper = sheet.getCell(6, i);

            Node passengerOrigin = new Node(Integer.parseInt(origin.getContents()), 0.0, 0.0, null);
            Node passengerDestination = new Node(Integer.parseInt(destination.getContents()), 0.0, 0.0, null);

//            Request request = new Request(Integer.parseInt(id.getContents()),
//                    passengerOrigin,
//                    passengerDestination,
//                    Integer.parseInt(pickupTimeWindowLower.getContents()),
//                    Integer.parseInt(deliveryTimeWindowLower.getContents()),
//                    Integer.parseInt(deliveryTimeWindowUpper.getContents()));
//
//            requests.add(request);
        }
        return requests;
    }

    public List<Request> getRequests(List<Node> nodes) throws IOException, BiffException {
        WorkbookSettings conf = new WorkbookSettings();
        conf.setEncoding("ISO-8859-1");
        Workbook workbook = Workbook.getWorkbook(new File(this.filePath + this.requestsFile), conf);
        Sheet sheet = workbook.getSheet(requestsData);
        int rows = sheet.getRows();
        int columns = sheet.getColumns();

        List<Request> requests = new ArrayList<>();

        for (int i = 1; i < rows; i++) {
            Cell id = sheet.getCell(0, i);
            Cell origin = sheet.getCell(1, i);
            Cell destination = sheet.getCell(2, i);
            Cell pickupTimeWindowLower = sheet.getCell(3, i);
            Cell pickupTimeWindowUpper = sheet.getCell(4, i);
            Cell deliveryTimeWindowLowerCell = sheet.getCell(5, i);
            Cell deliveryTimeWindowUpperCell = sheet.getCell(6, i);

            Node passengerOrigin = nodes.stream().filter(u -> u.getId() == Integer.parseInt(origin.getContents())).collect(Collectors.toList()).get(0);
            Node passengerDestination = nodes.stream().filter(u -> u.getId() == Integer.parseInt(destination.getContents())).collect(Collectors.toList()).get(0);

            LocalDate day = LocalDate.of(2017, 1, 1);
            LocalDateTime requestDay = LocalDateTime.of(day, LocalTime.MIN);
            LocalTime timeWindowLower = LocalTime.of(Integer.parseInt(deliveryTimeWindowLowerCell.getContents()) / 60,
                    Integer.parseInt(deliveryTimeWindowLowerCell.getContents()) % 60);
            LocalTime timeWindowUpper = LocalTime.of(Integer.parseInt(deliveryTimeWindowUpperCell.getContents()) / 60,
                    Integer.parseInt(deliveryTimeWindowUpperCell.getContents()) % 60);

            LocalDateTime deliveryTimeWindowLower = LocalDateTime.of(day, timeWindowLower);
            LocalDateTime deliveryTimeWindowUpper = LocalDateTime.of(day, timeWindowUpper);

            Request request = new Request(Integer.parseInt(id.getContents()), passengerOrigin, passengerDestination, requestDay,
                    requestDay, deliveryTimeWindowLower, deliveryTimeWindowUpper);

            requests.add(request);
        }
        return requests;
    }

    public Set<Integer> getSetOfNodes() throws IOException, BiffException {
        Set<Integer> nodesSet = new HashSet<>();

        WorkbookSettings conf = new WorkbookSettings();
        conf.setEncoding("ISO-8859-1");
        Workbook workbook = Workbook.getWorkbook(new File(this.filePath + this.nodesFile), conf);
        Sheet sheet = workbook.getSheet(nodesData);
        int rows = sheet.getRows();
        int columns = sheet.getColumns();

        for (int i = 1; i < rows; i++) {
            Cell id = sheet.getCell(0, i);
            nodesSet.add(Integer.parseInt(id.getContents()));
        }
        this.numberOfNodes = nodesSet.size();
        return nodesSet;
    }

    public List<Node> getListOfNodes() throws IOException, BiffException {
        List<Node> nodes = new ArrayList<>();

        WorkbookSettings conf = new WorkbookSettings();
        conf.setEncoding("ISO-8859-1");
        Workbook workbook = Workbook.getWorkbook(new File(this.filePath + this.nodesFile), conf);
        Sheet sheet = workbook.getSheet(nodesData);
        int rows = sheet.getRows();
        int columns = sheet.getColumns();

        for (int i = 1; i < rows; i++) {
            Cell id = sheet.getCell(0, i);
            Cell latitude = sheet.getCell(1, i);
            Cell longitude = sheet.getCell(2, i);
            Cell address = sheet.getCell(3, i);

            Node node = new Node(Integer.parseInt(id.getContents()), Double.parseDouble(latitude.getContents()),
                    Double.parseDouble(longitude.getContents()), address.getContents());
            nodes.add(node);
        }
        this.numberOfNodes = nodes.size();
        return nodes;
    }

    public List<List<Long>> getAdjacenciesListOfDistances() throws IOException, BiffException {

        initializaNumberOfNodesIfEqualsZero();

        WorkbookSettings conf = new WorkbookSettings();
        conf.setEncoding("ISO-8859-1");
        Workbook workbook = Workbook.getWorkbook(new File(this.filePath + this.adjacenciesFile), conf);
        Sheet sheet = workbook.getSheet(adjacenciesData);
        int rows = sheet.getRows();
        int columns = sheet.getColumns();

        List<List<Long>> distanceBetweenNodes = new LinkedList<>();
        initializeAdjacenciesWithZeros(distanceBetweenNodes);

        for (int i = 1; i < rows; i++) {
            Integer originNode = Integer.parseInt(sheet.getCell(0, i).getContents());//)resultSet.getInt("originNode");
            Integer destinationNode = Integer.parseInt(sheet.getCell(1, i).getContents());
            Long distanceTo = (long) Double.parseDouble(sheet.getCell(3, i).getContents());
            distanceBetweenNodes.get(originNode).set(destinationNode, distanceTo);

        }

        return distanceBetweenNodes;
    }

    private void initializeAdjacenciesWithZeros(List<List<Long>> adjacencie) {
        for (int i = 0; i < numberOfNodes; i++) {
            adjacencie.add(new LinkedList<Long>());
            for (int j = 0; j < numberOfNodes; j++) {
                long zero = 0;
                adjacencie.get(i).add(zero);
            }
        }
    }

    private void initializaNumberOfNodesIfEqualsZero() throws BiffException, IOException {
        if (this.numberOfNodes == 0) {
            getListOfNodes();
        }
    }

    public Duration[][] getDurationBetweenNodes(int numberOfNodes) throws IOException, BiffException {

        initializaNumberOfNodesIfEqualsZero();
        Duration[][] durationBetweenNodes = new Duration[numberOfNodes][numberOfNodes];

        WorkbookSettings conf = new WorkbookSettings();
        conf.setEncoding("ISO-8859-1");
        Workbook workbook = Workbook.getWorkbook(new File(this.filePath + this.adjacenciesFile), conf);
        Sheet sheet = workbook.getSheet(adjacenciesData);
        int rows = sheet.getRows();
        int columns = sheet.getColumns();

        List<List<Long>> timeBetweenNodes = new LinkedList<>();
        initializeAdjacenciesWithZeros(timeBetweenNodes);
        int k = 0, j = 0;
        for (int i = 1; i < rows; i++) {
            Integer originNode = Integer.parseInt(sheet.getCell(0, i).getContents());
            Integer destinationNode = Integer.parseInt(sheet.getCell(1, i).getContents());
            Long timeTo = (long) Double.parseDouble(sheet.getCell(2, i).getContents());
            Duration durationTo = Duration.ofSeconds(timeTo);
            durationBetweenNodes[k][j] = durationTo;
            j++;
            if (j == numberOfNodes) {
                k++;
                j = 0;
            }
        }

        return durationBetweenNodes;
    }

    public long[][] getDistanceBetweenNodes(int numberOfNodes) throws IOException, BiffException {

        initializaNumberOfNodesIfEqualsZero();
        long[][] distanceBetweenNodes = new long[numberOfNodes][numberOfNodes];

        WorkbookSettings conf = new WorkbookSettings();
        conf.setEncoding("ISO-8859-1");
        Workbook workbook = Workbook.getWorkbook(new File(this.filePath + this.adjacenciesFile), conf);
        Sheet sheet = workbook.getSheet(adjacenciesData);
        int rows = sheet.getRows();
        int columns = sheet.getColumns();

        List<List<Long>> timeBetweenNodes = new LinkedList<>();
        initializeAdjacenciesWithZeros(timeBetweenNodes);
        int k = 0, j = 0;
        for (int i = 1; i < rows; i++) {
            Integer originNode = Integer.parseInt(sheet.getCell(0, i).getContents());
            Integer destinationNode = Integer.parseInt(sheet.getCell(1, i).getContents());
            Long distance = (long) Double.parseDouble(sheet.getCell(3, i).getContents());
            distanceBetweenNodes[k][j] = distance;
            j++;
            if (j == numberOfNodes) {
                k++;
                j = 0;
            }
        }

        return distanceBetweenNodes;
    }

    public int getNumberOfNodes() throws IOException, BiffException {
        return this.getListOfNodes().size();
    }

}
