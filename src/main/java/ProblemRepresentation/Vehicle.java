/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProblemRepresentation;

/**
 *
 * @author renansantos
 */
public class Vehicle {

    private int id;
    private String licensePlate;
    private int capacity;

    public Vehicle() {

    }

    public Vehicle(int id, String licensePlate, int capacity) {
        this.id = id;
        this.licensePlate = licensePlate;
        this.capacity = capacity;
    }

    public Vehicle(Vehicle vehicle) {
        this.setId(vehicle.getId());
        this.setLicensePlate(vehicle.getLicensePlate());
        this.setCapacity(vehicle.getCapacity());
    }

    public int getId() {
        return id;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setVehicle(Vehicle vehicle) {
        this.setId(vehicle.getId());
        this.setLicensePlate(vehicle.getLicensePlate());
        this.setCapacity(vehicle.getCapacity());
    }

    public String toString() {
        return "Vehicle = " + this.id + "\t License Plate = " + this.licensePlate + "\t Capacity = " + this.capacity;
    }

}
