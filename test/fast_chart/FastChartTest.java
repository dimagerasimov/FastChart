/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fast_chart;

import fast_chart.FastChart.ChartLimits;
import java.awt.Color;
import java.util.ArrayList;
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
     * Test empty chart, of class FastChart.
     */
    @Test
    public void testEmptyChart() {
        System.out.println("Test of empty chart");
        
        try {
            FastChart myChart = new FastChart();
            myChart.sync();

            myChart.setTitle("Empty chart test");
            myChart.setAreaFlag(false);

            // Check for invalid operation
            myChart.setGraphicColor(0, Color.getHSBColor(0.0f, 0.8f, 0.6f));
            myChart.setDescription(1, "-3+Cos(x)");
            ///////////////////////////////
            
            myChart.setVisible(true);
            showChart(myChart);
        } catch(Exception ex) {
            fail("The test failed.");
        }
    }   
    
    /**
     * Test line chart, of class FastChart.
     */
    @Test
    public void testLineChart() {
        System.out.println("Test of line chart");
        
        final int size = 100;
        float x, step;
                
        ArrayList<XY<Float>> points1 = new ArrayList<>(size + 1);
        step = (float)(2.0f * Math.PI) / size;
        x = -(float)Math.PI;
        for(int i = 0; i < size + 1; i++) {
            points1.add(new XY<>(x, (float)Math.sin(x * 3.0f)));
            x += step;
        }
        
        ArrayList<XY<Float>> points2 = new ArrayList<>(size + 1);
        step = (float)(2.0f * Math.PI) / size;
        x = -(float)Math.PI;
        for(int i = 0; i < size + 1; i++) {
            points2.add(new XY<>(x, -3.0f + (float)Math.cos(x)));
            x += step;
        }
        
        try {
            FastChart myChart = new FastChart();
            myChart.sync(points1, points2);

            myChart.setTitle("Line chart test");
            myChart.setAreaFlag(false);

            myChart.setGraphicColor(0, Color.getHSBColor(0.0f, 0.8f, 0.6f));
            myChart.setGraphicColor(1, Color.getHSBColor(0.5f, 0.5f, 0.5f));

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
        float x, step;
                
        ArrayList<XY<Float>> points1 = new ArrayList<>(size + 1);
        step = (float)(2.0f * Math.PI) / size;
        x = -(float)Math.PI;
        for(int i = 0; i < size + 1; i++) {
            points1.add(new XY<>(x, (float)Math.sin(x * 3.0f)));
            x += step;
        }
        
        ArrayList<XY<Float>> points2 = new ArrayList<>(size + 1);
        step = (float)(2.0f * Math.PI) / size;
        x = -(float)Math.PI;
        for(int i = 0; i < size + 1; i++) {
            points2.add(new XY<>(x, -3.0f + (float)Math.cos(x)));
            x += step;
        }
        
        try {
            FastChart myChart = new FastChart();
            myChart.sync(points1, points2);

            myChart.setTitle("Area chart test");
            myChart.setAreaFlag(true);

            myChart.setGraphicColor(0, Color.getHSBColor(0.0f, 0.8f, 0.6f));
            myChart.setGraphicColor(1, Color.getHSBColor(0.5f, 0.5f, 0.5f));

            myChart.setDescription(0, "Sin(3x)");
            myChart.setDescription(1, "-3+Cos(x)");

            myChart.setVisible(true);
            showChart(myChart);
        } catch(Exception ex) {
            fail("The test failed.");
        }
    }
    
    /**
     * Test selected point, of class FastChart.
     */
    @Test
    public void testSelectedPointAlgorithm() {
        System.out.println("Test of selected point algorithm");
        
        final int size = 20;
        float x, step;
                
        ArrayList<XY<Float>> points1 = new ArrayList<>(size + 1);
        step = (float)(2.0f * Math.PI) / size;
        x = -(float)Math.PI;
        for(int i = 0; i < size + 1; i++) {
            points1.add(new XY<>(x, (float)Math.sin(x * 3.0f)));
            x += step;
        }
        
        ArrayList<XY<Float>> points2 = new ArrayList<>(size + 1);
        step = (float)(2.0f * Math.PI) / size;
        x = -(float)Math.PI;
        for(int i = 0; i < size + 1; i++) {
            points2.add(new XY<>(x, -1.0f + (float)Math.cos(2 * x)));
            x += step;
        }
        
        try {
            FastChart myChart = new FastChart();
            myChart.sync(points1, points2);

            myChart.setTitle("Selected point algorithm");
            myChart.setAreaFlag(false);

            myChart.setSelectedPointColor(Color.YELLOW);
            myChart.setGraphicColor(0, Color.getHSBColor(0.0f, 0.8f, 0.6f));
            myChart.setGraphicColor(1, Color.getHSBColor(0.5f, 0.5f, 0.5f));

            myChart.setDescription(0, "Sin(3x)");
            myChart.setDescription(1, "-1+Cos(2x)");

            myChart.setVisible(true);
            showChart(myChart);
        } catch(Exception ex) {
            fail("The test failed.");
        }
    }  

    /**
     * Test set limits, of class FastChart.
     */
    @Test
    public void testSetLimits() {
        System.out.println("Test of setting of chart limits");
        
        final int size = 20;
        float x, step;
                
        ArrayList<XY<Float>> points = new ArrayList<>(size + 1);
        float rectLength = 5.0f;
        step = rectLength / (size - 1);
        points.add(new XY<>(0.0f, 0.0f));
        x = 0.0f;
        for(int i = 0; i < size; i++) {
            points.add(new XY<>(x, 1.0f));
            x += step;
        }
        points.add(new XY<>(rectLength, 0.0f));

        try {
            FastChart myChart = new FastChart();
            myChart.sync(points);

            myChart.setTitle("Set limits test");
            myChart.setAreaFlag(true);

            ChartLimits limits = myChart.getLimits();
            limits.SetMaximumX(limits.GetMaximumX() + 0.1f * rectLength);
            limits.SetMinimumX(limits.GetMinimumX() - 0.1f * rectLength);
            limits.SetMaximumY(limits.GetMaximumY() + 0.5f * rectLength);
            myChart.setLimits(limits);

            myChart.setSelectedPointColor(Color.ORANGE);
            myChart.setGraphicColor(0, Color.getHSBColor(0.0f, 0.8f, 0.6f));
 
            myChart.setDescription(0, "rectangle");

            myChart.setVisible(true);
            showChart(myChart);
        } catch(Exception ex) {
            fail("The test failed.");
        }
    }    
}
