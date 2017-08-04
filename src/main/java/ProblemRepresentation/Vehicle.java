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

    public Vehicle(int id, String licensePlate, int capacity) {
        this.id = id;
        this.licensePlate = licensePlate;
        this.capacity = capacity;
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
    
    public String toString(){
        return "Vehicle = "+ this.id +"\t License Plate = " + this.licensePlate + "\t Capacity = " + this.capacity;
    }
    
}
