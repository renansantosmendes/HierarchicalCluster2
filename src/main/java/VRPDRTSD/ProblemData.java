/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VRPDRTSD;

import InstanceReaderWithMySQL.AdjacenciesDAO;
import InstanceReaderWithMySQL.NodeDAO;
import InstanceReaderWithMySQL.NumberOfNodesDAO;
import InstanceReaderWithMySQL.RequestDAO;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author renansantos
 */
public class ProblemData {

    private int numberOfNodes;
    private List<Node> nodes;
    private List<Request> requests;
    private List<Request> instanceRequests = new ArrayList<>();
    private Duration[][] duration;
    private long[][] distance;
    private String instanceName;
    private String nodesInstanceName;
    private String adjacenciesInstanceName;
    private LocalDateTime currentTime = LocalDateTime.of(2017, 1, 1, 0, 0, 0);
    private Node currentNode = new Node(0, 40.7143528, -74.0059731, "Av. do Contorno, 340 - Santa Efigênia, Belo Horizonte - MG, 30110-017");
    private Request lastPassengerAddedToRoute;

    public ProblemData(String instanceName, String nodesInstanceName, String adjacenciesInstanceName) {
        this.instanceName = instanceName;
        this.nodesInstanceName = nodesInstanceName;
        this.adjacenciesInstanceName = adjacenciesInstanceName;
        this.readInstance();
    }

    public int getNumberOfNodes() {
        return numberOfNodes;
    }

    public void setNumberOfNodes(int numberOfNodes) {
        this.numberOfNodes = numberOfNodes;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }

    public List<Request> getInstanceRequests() {
        return instanceRequests;
    }

    public void setInstanceRequests(List<Request> instanceRequests) {
        this.instanceRequests = instanceRequests;
    }

    public Duration[][] getDuration() {
        return duration;
    }

    public void setDuration(Duration[][] duration) {
        this.duration = duration;
    }

    public long[][] getDistance() {
        return distance;
    }

    public void setDistance(long[][] distance) {
        this.distance = distance;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getNodesInstanceName() {
        return nodesInstanceName;
    }

    public void setNodesInstanceName(String nodesInstanceName) {
        this.nodesInstanceName = nodesInstanceName;
    }

    public String getAdjacenciesInstanceName() {
        return adjacenciesInstanceName;
    }

    public void setAdjacenciesInstanceName(String adjacenciesInstanceName) {
        this.adjacenciesInstanceName = adjacenciesInstanceName;
    }

    public LocalDateTime getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(LocalDateTime currentTime) {
        this.currentTime = currentTime;
    }

    public Node getCurrentNode() {
        return currentNode;
    }

    public void setCurrentNode(Node currentNode) {
        this.currentNode = currentNode;
    }

    public Request getLastPassengerAddedToRoute() {
        return lastPassengerAddedToRoute;
    }

    public void setLastPassengerAddedToRoute(Request lastPassengerAddedToRoute) {
        this.lastPassengerAddedToRoute = lastPassengerAddedToRoute;
    }

    public void readInstance() {
        this.numberOfNodes = new NumberOfNodesDAO().getNumberOfNodes(this.nodesInstanceName);
        this.nodes = new NodeDAO(this.nodesInstanceName).getListOfNodes();
        this.requests = new RequestDAO(this.instanceName).getListOfRequestUsingNodesList(nodes);
        this.duration = new AdjacenciesDAO(this.adjacenciesInstanceName, this.nodesInstanceName).getDurationBetweenNodes(this.numberOfNodes);
        this.distance = new AdjacenciesDAO(this.adjacenciesInstanceName, this.nodesInstanceName).getDistanceBetweenNodes(this.numberOfNodes);
        this.instanceRequests.addAll(this.requests);
    }

}
