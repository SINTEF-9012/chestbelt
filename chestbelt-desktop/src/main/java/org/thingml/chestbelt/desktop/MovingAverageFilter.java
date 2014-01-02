/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.thingml.chestbelt.desktop;

import java.util.LinkedList;
import java.util.Queue;

/**
 * From ingydotnet@GitHub:
 * Task/Averages-Simple-moving-average/Java/averages-simple-moving-average.java
 * @author oyvsta
 */
public class MovingAverageFilter {
    private final Queue<Double> samples = new LinkedList<Double>();
    private final int size;
    private double total;
    
    public MovingAverageFilter(int size){
        this.size = size;
    }
    
    public void add(double x) {
        total += x;
        samples.add(x);
        if(samples.size() > size) {
            total -= samples.remove();
        }
    }
    
    
    public double getAverage() {
        if(samples.isEmpty()) return 0;
        return total/samples.size();
    }
}
