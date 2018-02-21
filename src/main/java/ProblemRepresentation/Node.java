package ProblemRepresentation;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Renan Santos Mendes T
 */
public class Node implements Cloneable {

    private Integer id;
    private Double longitude;
    private Double latitude;
    private String adress;
    private int loadIndex;

    public Node() {

    }

    public Node(Integer nodeId, Double longitude, Double latitude, String adress) {
        this.id = nodeId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.adress = adress;
        this.loadIndex = 0;
    }

    public Node(Integer id, Double longitude, Double latitude, String adress, int loadIndex) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.adress = adress;
        this.loadIndex = loadIndex;
    }

    public Integer getId() {
        return id;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public String getAdress() {
        return adress;
    }

    public int getLoadIndex() {
        return loadIndex;
    }

    public String getGeocodedInformationForRoutes() {
        return this.getLongitude() + "," + this.getLatitude();
    }

    public void setLoadIndex(Map<Node, List<Request>> requestsWichBoardsInNode, Map<Node, List<Request>> requestsWichLeavesInNode) {
        if (requestsWichBoardsInNode.get(this) != null && requestsWichLeavesInNode.get(this) != null) {
            this.loadIndex = requestsWichBoardsInNode.get(this).size() - requestsWichLeavesInNode.get(this).size();
        }
    }

    private void setId(Integer id) {
        this.id = id;
    }

    private void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    private void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    private void setAdress(String adress) {
        this.adress = adress;
    }

    private void setLoadIndex(int loadIndex) {
        this.loadIndex = loadIndex;
    }

    public void setNode(Node node) {
        this.setId(node.getId());
        this.setLongitude(node.getLongitude());
        this.setLatitude(node.getLatitude());
        this.setAdress(node.getAdress());
        this.setLoadIndex(node.getLoadIndex());
    }

    public String getLatLng() {
        return this.longitude + "," + this.latitude;
    }

    @Override
    public String toString() {
        return "Node(" + this.id + ") " + "Lat = " + this.latitude + " Long = "
                + this.longitude + " LoadIndex = " + this.loadIndex + " Adress = " + this.adress;
    }

    public String toStringForMapQuery() {
        String color;
        String label = null;
        if (this.id > 9) {
            if (this.id == 10) {
                label = "A";
            } else if (this.id == 11) {
                label = "B";
            } else if (this.id == 12) {
                label = "C";
            } else if (this.id == 13) {
                label = "D";
            } else if (this.id == 14) {
                label = "E";
            } else if (this.id == 15) {
                label = "F";
            }
        } else {
            label = this.id.toString();
        }

        if (this.id == 0) {
            color = "red";
            //label = "O";
        } else {
            color = "red";
            //label = "S";
        }
        return "&markers=color:" + color + "|label:" + label + "|" + this.longitude + "," + this.latitude;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        Node node = (Node) obj;
        return this.getId() == node.getId();
    }

    @Override
    public int hashCode() {
        String string = Integer.toString(this.getId());
        int hash = string.hashCode();
        return hash;
    }

    @Override
    public Object clone() {
        return new Node(id, longitude, latitude, adress, loadIndex);
    }
}
