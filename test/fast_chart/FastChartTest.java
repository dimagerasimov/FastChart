/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fast_chart;

import java.awt.Color;
import javax.swing.JFrame;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author master
 */
public class FastChartTest {
    
    public FastChartTest() {
    }

    private static void showChart(FastChart myChart) throws InterruptedException {
        JFrame frame = new JFrame();
        frame.setSize(500, 500);
        frame.add(myChart);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        Thread.sleep(10000);
        
        assertTrue(true);
    }
    
    /**
     * Test line chart, of class FastChart.
     */
    @Test
    public void testLineChart() {
        System.out.println("Test of line chart");
        
        final int size = 100;
        double x, step;
                
        XY[] points1 = new XY[size + 1];
        step = (2 * Math.PI) / size;
        x = -Math.PI;
        for(int i = 0; i < size + 1; i++) {
            points1[i] = new XY(x, Math.sin(x * 3));
            x += step;
        }
        
        XY[] points2 = new XY[size + 1];
        step = (2 * Math.PI) / size;
        x = -Math.PI;
        for(int i = 0; i < size + 1; i++) {
            points2[i] = new XY(x, -3.0 + Math.cos(x));
            x += step;
        }
        
        try {
            FastChart myChart = new FastChart();
            myChart.sync(points1, points2);

            myChart.setTitle("Line chart test");
            myChart.setAreaFlag(false);

            myChart.setColor(0, Color.getHSBColor(0.0f, 0.8f, 0.6f));
            myChart.setColor(1, Color.getHSBColor(0.5f, 0.5f, 0.5f));

            myChart.setDescription(0, "Sin(3x)");
            myChart.setDescription(1, "-3+Cos(x)");

            myChart.setVisible(true);
            showChart(myChart);
        } catch(Exception ex) {
            fail("The test failed.");
        }
    }   
    
    /**
     * Test area chart, of class FastChart.
     */
    @Test
    public void testAreaChart() {
        System.out.println("Test of area chart");
        
        final int size = 100;
        double x, step;
                
        XY[] points1 = new XY[size + 1];
        step = (2 * Math.PI) / size;
        x = -Math.PI;
        for(int i = 0; i < size + 1; i++) {
            points1[i] = new XY(x, Math.sin(x * 3));
            x += step;
        }
        
        XY[] points2 = new XY[size + 1];
        step = (2 * Math.PI) / size;
        x = -Math.PI;
        for(int i = 0; i < size + 1; i++) {
            points2[i] = new XY(x, -3.0 + Math.cos(x));
            x += step;
        }
        
        try {
            FastChart myChart = new FastChart();
            myChart.sync(points1, points2);

            myChart.setTitle("Area chart test");
            myChart.setAreaFlag(true);

            myChart.setColor(0, Color.getHSBColor(0.0f, 0.8f, 0.6f));
            myChart.setColor(1, Color.getHSBColor(0.5f, 0.5f, 0.5f));

            myChart.setDescription(0, "Sin(3x)");
            myChart.setDescription(1, "-3+Cos(x)");

            myChart.setVisible(true);
            showChart(myChart);
        } catch(Exception ex) {
            fail("The test failed.");
        }
    }    
}
