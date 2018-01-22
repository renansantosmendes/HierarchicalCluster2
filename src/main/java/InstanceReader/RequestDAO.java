/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InstanceReader;

import ProblemRepresentation.Node;
import ProblemRepresentation.Request;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author renansantos
 */
public class RequestDAO {

    private Connection connection;
    private String instanceName;

    public RequestDAO(String instanceName) {
        this.connection = new ConnectionFactory().getConnection();
        this.instanceName = instanceName;
    }

    public void addRequestIntoDataBase(Request request) {
        String sql = "insert into requests "
                + "(id, passengerOrigin, passengerDestination, requestDay, pickUpTime, "
                + "deliveryTimeWindowLower, deliveryTimeWindowUpper)"
                + " values (?,?,?,?,?,?,?)";

        try {
            PreparedStatement statement = this.connection.prepareStatement(sql);

            statement.setString(1, request.getId().toString());
            statement.setString(2, request.getOrigin().getId().toString());
            statement.setString(3, request.getDestination().getId().toString());
            statement.setString(4, request.getDayRequestWasMade().toString());
            statement.setString(5, request.getPickUpTime().toLocalTime().toString());
            statement.setString(6, request.getDeliveryTimeWindowLower().toLocalTime().toString());
            statement.setString(7, request.getDeliveryTimeWindowUpper().toLocalTime().toString());

            statement.execute();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Request> getListOfRequest() {
        try {

            List<Request> listOfRequests = new ArrayList<Request>();
            PreparedStatement statement = this.connection.prepareStatement("select * from requests inner join originNodes on "
                    + "requests.passengerOrigin = originNodes.originNodeId inner join destinationNodes on "
                    + "requests.passengerDestination = destinationNodes.destinationNodeId;");

            ResultSet resultSetForRequests = statement.executeQuery();

            while (resultSetForRequests.next()) {

                int requestId = resultSetForRequests.getInt("requestId");
                int passengerOriginId = resultSetForRequests.getInt("originNodeId");
                double originLatitude = resultSetForRequests.getDouble("originLatitude");
                double originLongitude = resultSetForRequests.getDouble("originLongitude");
                int passengerDestinationId = resultSetForRequests.getInt("destinationNodeId");
                double destinationLatitude = resultSetForRequests.getDouble("destinationLatitude");
                double destinationLongitude = resultSetForRequests.getDouble("destinationLongitude");

                Node passengerOrigin = new Node(passengerOriginId, originLatitude, originLongitude, null);
                Node passengerDestination = new Node(passengerDestinationId, destinationLatitude, destinationLongitude, null);

                LocalDate requestDay = resultSetForRequests.getDate("requestDay").toLocalDate();

                LocalDateTime requestDayConverted = LocalDateTime.of(requestDay, LocalTime.MIN);
                LocalDateTime pickUpTime = LocalDateTime.of(requestDay, resultSetForRequests.getTime("pickUpTime").toLocalTime());
                LocalDateTime deliveryTimeWindowLower = LocalDateTime.of(requestDay, resultSetForRequests.getTime("deliveryTimeWindowLower").toLocalTime());
                LocalDateTime deliveryTimeWindowUpper = LocalDateTime.of(requestDay, resultSetForRequests.getTime("deliveryTimeWindowUpper").toLocalTime());

                Request request = new Request(requestId, passengerOrigin, passengerDestination, requestDayConverted, pickUpTime, deliveryTimeWindowLower, deliveryTimeWindowUpper);

                listOfRequests.add(request);
            }
            resultSetForRequests.close();
            statement.close();
            return listOfRequests;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public List<Request> getListOfRequestUsingNodesList(List<Node> nodes) {
        try {

            List<Request> listOfRequests = new ArrayList<Request>();

            String sql = "select * from " + instanceName + ";";
            PreparedStatement statement = this.connection.prepareStatement(sql);
            //statement.setString(1, instanceName);

            ResultSet resultSetForRequests = statement.executeQuery();

            while (resultSetForRequests.next()) {

                int requestId = resultSetForRequests.getInt("id");
                int passengerOriginId = resultSetForRequests.getInt("passengerOrigin");
                int passengerDestinationId = resultSetForRequests.getInt("passengerDestination");
                LocalDate requestDay = resultSetForRequests.getDate("requestDay").toLocalDate();

                LocalDateTime requestDayConverted = LocalDateTime.of(requestDay, LocalTime.MIN);
                LocalDateTime pickUpTime = LocalDateTime.of(requestDay, resultSetForRequests.getTime("pickUpTime").toLocalTime());
                LocalDateTime deliveryTimeWindowLower = LocalDateTime.of(requestDay, resultSetForRequests.getTime("deliveryTimeWindowLower").toLocalTime());
                LocalDateTime deliveryTimeWindowUpper = LocalDateTime.of(requestDay, resultSetForRequests.getTime("deliveryTimeWindowUpper").toLocalTime());

                Request request = new Request(requestId, nodes.stream().filter(u -> u.getId() == passengerOriginId).collect(Collectors.toList()).get(0),
                        nodes.stream().filter(u -> u.getId() == passengerDestinationId).collect(Collectors.toList()).get(0), requestDayConverted,
                        pickUpTime, deliveryTimeWindowLower, deliveryTimeWindowUpper);

                listOfRequests.add(request);
            }
            resultSetForRequests.close();
            statement.close();
            return listOfRequests;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
