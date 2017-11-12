/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GoogleMapsApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author renansantos
 */
public class ColorGenerator {

    private static int colorCounter;
    private static List<String> listOfColors = new ArrayList<>();

    public ColorGenerator() {
        colorCounter = 0;
    }

    public String generatesRandomColor() {
        StringBuilder color = new StringBuilder();
        Random rnd = new Random();

        color.append("0x");
        for (int i = 0; i < 6; i++) {
            int number = rnd.nextInt(16);
            if (number == 10) {
                color.append("a");
            } else if (number == 11) {
                color.append("b");
            } else if (number == 12) {
                color.append("c");
            } else if (number == 13) {
                color.append("d");
            } else if (number == 14) {
                color.append("e");
            } else if (number == 15) {
                color.append("f");
            } else {
                color.append(number);
            }

        }
        listOfColors.add(color.toString());
        return color.toString();
    }

    public static String generatesColor() {
        loadColors();
        String color = listOfColors.get(colorCounter);
        colorCounter++;
        return color;
    }

    private static void loadColors() {
        listOfColors.add("0xFF0000");
        listOfColors.add("0x4169E1");
        listOfColors.add("0x228B22");
        listOfColors.add("0xD2691E");
        listOfColors.add("0x00FF7F");
        listOfColors.add("0x20B2AA");
        listOfColors.add("0x708090");
        
        listOfColors.add("0x9400D3");
        listOfColors.add("0xDC143C");
        listOfColors.add("0xFFD700");
        listOfColors.add("0xBC8F8F");
        listOfColors.add("0xD2691E");

    }

}
