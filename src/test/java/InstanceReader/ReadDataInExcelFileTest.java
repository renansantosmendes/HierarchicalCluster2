/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InstanceReader;

import java.io.IOException;
import jxl.read.biff.BiffException;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author renansantos
 */
public class ReadDataInExcelFileTest {

    @Test
    public void testExcelReader() throws IOException, BiffException {
        
        int requestsNumber = 10;
        
        Instance instance = new Instance();
        instance.setNumberOfRequests(requestsNumber)
                .setRequestTimeWindows(10)
                .setInstanceSize("s")
                .setNumberOfNodes(12)
                .setNumberOfVehicles(250)
                .setVehicleCapacity(4);
        
        ReadDataInExcelFile reader = new ReadDataInExcelFile("/home/renansantos/Área de Trabalho/Excel Instances/",instance);
//        reader.getListOfNodes().forEach(System.out::println);
//        reader.getRequests(reader.getListOfNodes()).forEach(System.out::println);
        reader.getDistanceBetweenNodes(12);
        Assert.assertEquals(12, reader.getListOfNodes().size());
        
        Assert.assertEquals(requestsNumber,reader.getRequests(reader.getListOfNodes()).size());
        
    }

}
