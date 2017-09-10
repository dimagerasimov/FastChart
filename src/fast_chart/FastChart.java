/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fast_chart;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.util.ArrayList;

/**
 *
 * @author UMKA
 */
public class FastChart extends JPanel implements MouseMotionListener, MouseWheelListener {
    public FastChart() {
        super();
        
        final int TITLE_FONT_SIZE = 16;
        final int NOTHING_TO_SHOW_FONT_SIZE = 14;
        final int AXIS_FONT_SIZE = 12;
        final int DESCRIPTION_FONT_SIZE = 15;

        titleFont = new Font(Font.DIALOG, Font.BOLD, TITLE_FONT_SIZE);
        nothingToShowFont = new Font(Font.DIALOG, Font.BOLD, NOTHING_TO_SHOW_FONT_SIZE);
        axisFont = new Font(Font.DIALOG, Font.PLAIN, AXIS_FONT_SIZE);
        descriptionFont = new Font(Font.DIALOG, Font.PLAIN, DESCRIPTION_FONT_SIZE);

        TextLayout tl;
        tl = new TextLayout("Aa", titleFont, new FontRenderContext(null, true, true));
        titleFontWidthPx =  (float)tl.getBounds().getWidth() / 2.0f;
        titleFontHeightPx = (float)tl.getBounds().getHeight();

        tl = new TextLayout(NOTHING_TO_SHOW_MSG, nothingToShowFont, new FontRenderContext(null, true, true));
        nothingToShowFontWidthPx =  (float)tl.getBounds().getWidth() / NOTHING_TO_SHOW_MSG.length();
        nothingToShowFontHeightPx = (float)tl.getBounds().getHeight();

        tl = new TextLayout("0", axisFont, new FontRenderContext(null, true, true));
        axisFontWidthPx = (float)tl.getBounds().getWidth();
        axisFontHeightPx = (float)tl.getBounds().getHeight();

        tl = new TextLayout("Aa", descriptionFont, new FontRenderContext(null, true, true));
        descriptionFontWidthPx = (float)tl.getBounds().getWidth() / 2.0f;
        descriptionFontHeightPx = (float)tl.getBounds().getHeight();

        clear();
    }
 
    public FastChart(Font titleFont, Font nothingToShowFont, Font axisFont, Font descriptionFont) {
        super();

        this.titleFont = titleFont;
        this.nothingToShowFont = nothingToShowFont;
        this.axisFont = axisFont;
        this.descriptionFont = descriptionFont;

        TextLayout tl;
        tl = new TextLayout("Aa", titleFont, new FontRenderContext(null, true, true));
        titleFontWidthPx =  (float)tl.getBounds().getWidth() / 2.0f;
        titleFontHeightPx = (float)tl.getBounds().getHeight();

        tl = new TextLayout("Aa", nothingToShowFont, new FontRenderContext(null, true, true));
        nothingToShowFontWidthPx =  (float)tl.getBounds().getWidth() / 2.0f;
        nothingToShowFontHeightPx = (float)tl.getBounds().getHeight();
        
        tl = new TextLayout("0", axisFont, new FontRenderContext(null, true, true));
        axisFontWidthPx = (float)tl.getBounds().getWidth();
        axisFontHeightPx = (float)tl.getBounds().getHeight();

        tl = new TextLayout("Aa", descriptionFont, new FontRenderContext(null, true, true));
        descriptionFontWidthPx = (float)tl.getBounds().getWidth() / 2.0f;
        descriptionFontHeightPx = (float)tl.getBounds().getHeight();

        clear();
    }

    public ChartLimits getLimits() {
        return new ChartLimits(currentLimits);
    }

    public void setLimits(ChartLimits chartLimits) {
        originalLimits = new ChartLimits(chartLimits);
        currentLimits = new ChartLimits(originalLimits);
        repaint();
    }

    public boolean setFormatValueAxisX(String format) {
        if(format == null) {
            return false;
        }
        axisFormatValueX = format;
        return true;
    }
    
    public boolean setFormatValueAxisY(String format) {
        if(format == null) {
            return false;
        }
        axisFormatValueY = format;
        return true;
    }
    
    public void setAreaFlag(boolean flag) {
        areaFlag = flag;
    }
    
    public boolean setTitle(String title) {
        if(title == null) {
            return false;
        }
        this.title = title;
        return true;
    }
    
    public boolean setSelectedPointColor(Color color) {
        if(color == null) {
            return false;
        } 
        selectedPointColor = color;
        return true;
    }

    public boolean setGraphicColor(int numberOfChart, Color color) {
        if(color == null || graphicColors == null ||
                numberOfChart < 0 || numberOfChart >= graphicColors.size()) {
            return false;
        } 
        graphicColors.set(numberOfChart, color);
        return true;
    }
    
    public boolean setDescription(int numberOfChart, String description) {
        if(description == null || descriptions == null ||
                numberOfChart < 0 || numberOfChart >= descriptions.size()) {
            return false;
        } 
        descriptions.set(numberOfChart, description);
        return true;
    }
    
    public boolean sync(ArrayList<XY<Float>>... points) {         
        if(points == null || points.length < 1) {
            clear();
            originalLimits = new ChartLimits(DEFAULT_CHART_LIMITS);
            currentLimits = new ChartLimits(originalLimits);
            return false;
        }
        originalLimits = new ChartLimits(DEFAULT_CHART_LIMITS);
        scaler = 1.0f;
        selectedPointColor = Color.blue;
        graphicColors = new ArrayList();
        graphics = new ArrayList();
        descriptions = new ArrayList();
        for (ArrayList<XY<Float>> arrayXY : points) {
            graphicColors.add(Color.getHSBColor((float)Math.random(),
                    0.3f + (float)Math.random() * 0.5f, 0.3f + (float)Math.random() * 0.5f));
            graphics.add(arrayXY);
            descriptions.add("Chart" + (descriptions.size() + 1));
            
            XY<Float> tmp;
            for (int j = 0; j < arrayXY.size(); j++) {
                tmp = arrayXY.get(j);
                if(tmp.x < originalLimits.GetMinimumX()) {
                    originalLimits.SetMinimumX(tmp.x);
                }
                if(tmp.x > originalLimits.GetMaximumX()) {
                    originalLimits.SetMaximumX(tmp.x);
                }
                if(tmp.y < originalLimits.GetMinimumY()) {
                    originalLimits.SetMinimumY(tmp.y);
                }
                if(tmp.y > originalLimits.GetMaximumY()) {
                    originalLimits.SetMaximumY(tmp.y);
                }
            }
        }
        currentLimits = new ChartLimits(originalLimits);
        if(!bMouseListenerExisted) {
            addMouseMotionListener(this);
            addMouseWheelListener(this);
            bMouseListenerExisted = true;
        }
        return true;
    }

    public final void clear() {
        axisFormatValueX = "%.3f";
        axisFormatValueY = "%.3f";
        
        areaFlag = false;
        title = "Fast Chart";   
        originalLimits = currentLimits = null;
        
        selectedPointColor = null;
        graphicColors = null;
        graphics = null;
        descriptions = null;
    }
    
    private float convertXToScreenPx(int width, float x) {
        return (x - currentLimits.minX) / (currentLimits.maxX - currentLimits.minX) * width;
    }
    
    private float convertYToScreenPx(int height, float y) {
        return (1.0f - (y - currentLimits.minY) / (currentLimits.maxY - currentLimits.minY)) * height;
    }
    
    private float convertScreenPxToX(int width, float x) {
        return currentLimits.minX + x / width * (currentLimits.maxX - currentLimits.minX);
    }
    
    private float convertScreenPxToY(int height, float y) {
        return currentLimits.maxY - y / height * (currentLimits.maxY - currentLimits.minY);
    }
    
    private void plot(Graphics g, ChartRectangle rectangle) {
        final int widthOfPlot = rectangle.GetWidth() - (rectangle.GetPaddingLeft() + rectangle.GetPaddingRight());
        final int heightOfPlot = rectangle.GetHeight() - (rectangle.GetPaddingBottom() + rectangle.GetPaddingTop());
        
        final int size = graphics.size();
        for(int i = 0; i < size; i++) {
            Color color = graphicColors.get(i);
            ArrayList<XY<Float>> points = graphics.get(i);
            if(points.size() < 2)
            {
                //We can't draw what doesn't exist
                continue;
            }
            int[] xs = new int[points.size()]; 
            for(int j = 0; j < xs.length; j++) {
                xs[j] = rectangle.GetPaddingLeft() + (int)convertXToScreenPx(widthOfPlot, points.get(j).x);
            }
            int[] ys = new int[points.size()];
            for(int j = 0; j < ys.length; j++) {
                ys[j] = rectangle.GetPaddingTop() + (int)convertYToScreenPx(heightOfPlot, points.get(j).y);
            }
            g.setColor(color);
            g.drawPolyline(xs, ys, xs.length);
        }
    }
    
    private void plotArea(Graphics g, ChartRectangle rectangle) {
        final int widthOfPlot = rectangle.GetWidth() - (rectangle.GetPaddingLeft() + rectangle.GetPaddingRight());
        final int heightOfPlot = rectangle.GetHeight() - (rectangle.GetPaddingBottom() + rectangle.GetPaddingTop());
        
        final int size = graphics.size();
        for(int i = 0; i < size; i++) {
            Color color = graphicColors.get(i);
            ArrayList<XY<Float>> points = graphics.get(i);
            if(points.size() < 2)
            {
                //We can't draw what doesn't exist
                continue;
            }
            int[] xs = new int[points.size() + 2]; 
            for(int j = 0; j < xs.length - 2; j++) {
                xs[j] = rectangle.GetPaddingLeft() + (int)convertXToScreenPx(widthOfPlot, points.get(j).x);
            }
            xs[xs.length - 2] = xs[xs.length - 3];
            xs[xs.length - 1] = xs[0];

            int[] ys = new int[points.size() + 2];
            for(int j = 0; j < ys.length - 2; j++) {
                ys[j] = rectangle.GetPaddingTop() + (int)convertYToScreenPx(heightOfPlot, points.get(j).y);
            }
            ys[ys.length - 2] = rectangle.GetPaddingTop() + heightOfPlot;
            ys[ys.length - 1] = rectangle.GetPaddingTop() + heightOfPlot;

            g.setColor(color);
            g.fillPolygon(xs, ys, xs.length);
        }
    }

    private void FixBorders() {
        float tmpDiffX, tmpDiffY;
        if(currentLimits.GetMinimumX() < originalLimits.GetMinimumX()) {
            tmpDiffX = originalLimits.GetMinimumX() - currentLimits.GetMinimumX();
            currentLimits.Move(tmpDiffX, 0.0f);
        }
        else if(currentLimits.GetMaximumX() > originalLimits.GetMaximumX()) {
            tmpDiffX = currentLimits.GetMaximumX() - originalLimits.GetMaximumX();
            currentLimits.Move(-tmpDiffX, 0.0f);
        }
        if(currentLimits.GetMinimumY() < originalLimits.GetMinimumY()) {
            tmpDiffY = originalLimits.GetMinimumY() - currentLimits.GetMinimumY();
            currentLimits.Move(0.0f, tmpDiffY);
        }
        else if(currentLimits.GetMaximumY() > originalLimits.GetMaximumY()) {
            tmpDiffY = currentLimits.GetMaximumY() - originalLimits.GetMaximumY();
            currentLimits.Move(0.0f, -tmpDiffY);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        x -= chartRectangle.paddingLeft;
        y -= chartRectangle.paddingTop;
        int widthOfChart = chartRectangle.width - (chartRectangle.GetPaddingLeft() + chartRectangle.GetPaddingRight());
        int heightOfChart = chartRectangle.height - (chartRectangle.GetPaddingTop() + chartRectangle.GetPaddingBottom());
        if(bMouseDraggedEvent && x >= 0 && x < widthOfChart && y >= 0 && y < heightOfChart) {
            currentLimits.Move(convertScreenPxToX(widthOfChart, mouseX) - convertScreenPxToX(widthOfChart, x),
                    convertScreenPxToY(heightOfChart, mouseY) - convertScreenPxToY(heightOfChart, y));
            FixBorders();
            repaint(chartRectangle.GetPaddingLeft(), chartRectangle.GetPaddingTop(),
                chartRectangle.GetWidth() - (chartRectangle.GetPaddingLeft() + chartRectangle.GetPaddingRight()),
                chartRectangle.GetHeight() - (chartRectangle.GetPaddingTop() + chartRectangle.GetPaddingTop()));
        }
        else {
            bMouseDraggedEvent = true;
        }
        mouseX = x;
        mouseY = y;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        bMouseDraggedEvent = false;
        mouseX = e.getX();
        mouseY = e.getY();
        
        SelectedPointInfo newSelectedPoint = FindSelectedPoint(chartRectangle);
        if(newSelectedPoint != null && currentSelectedPoint == null ||
           newSelectedPoint == null && currentSelectedPoint != null ||
           newSelectedPoint != null && currentSelectedPoint != null
            && newSelectedPoint.point_number != currentSelectedPoint.point_number) {
            repaint(chartRectangle.GetPaddingLeft(), chartRectangle.GetPaddingTop(),
                chartRectangle.GetWidth() - (chartRectangle.GetPaddingLeft() + chartRectangle.GetPaddingRight()),
                chartRectangle.GetHeight() - (chartRectangle.GetPaddingTop() + chartRectangle.GetPaddingTop()));
        }
        currentSelectedPoint = newSelectedPoint;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        float scalerCoeff = 1.0f + e.getWheelRotation() * 0.1f;
        float newScaler = scaler * scalerCoeff;
        if(newScaler < 0.2f) {
            newScaler = 0.2f;
        }
        else if(newScaler < 1.0f) {
            int x = e.getX();
            int y = e.getY();
            x -= chartRectangle.paddingLeft;
            y -= chartRectangle.paddingTop;
            int widthOfChart = chartRectangle.width - (chartRectangle.GetPaddingLeft() + chartRectangle.GetPaddingRight());
            int heightOfChart = chartRectangle.height - (chartRectangle.GetPaddingTop() + chartRectangle.GetPaddingBottom());
            if(x >= 0 && x < widthOfChart && y >= 0 && y < heightOfChart) {
                currentLimits.Scale(scalerCoeff, (float)x / widthOfChart, (float)y / heightOfChart);
                FixBorders();
            }
            else {
                newScaler = scaler;
            }
        }
        else {
            newScaler = 1.0f;
            currentLimits = new ChartLimits(originalLimits);
        }
        if(newScaler != scaler) {
            repaint();
        }
        scaler = newScaler;
    }

    private static SelectedPointInfo SelectedPointSearchAlgorithm(
        ArrayList<XY<Float>> points, float mouseX, float mouseY, float maxDistance) {
        //Line search. Most likely the algorithm will be improved in future
        float distance;
        boolean bSelected = false;
        SelectedPointInfo selectedPoint = new SelectedPointInfo();
        for(int i = 0; i < points.size(); i++) {
            distance = (float)Math.sqrt(
                    (mouseX - points.get(i).x) * (mouseX - points.get(i).x)
                    + (mouseY - points.get(i).y) * (mouseY - points.get(i).y));
            if(bSelected) {
                if(distance > selectedPoint.distance) {
                    break;
                }
                else {
                    selectedPoint.point_number = i;
                    selectedPoint.distance = distance;
                }
            }
            else if(distance < maxDistance) {
                selectedPoint.point_number = i;
                selectedPoint.distance = distance;
                bSelected = true;
            }
        }
        if(!bSelected) {
            selectedPoint = null;
        }
        return selectedPoint;
    }
    
    private SelectedPointInfo FindSelectedPoint(ChartRectangle rectangle) {
        final int widthOfPlot = rectangle.GetWidth() - (rectangle.GetPaddingLeft() + rectangle.GetPaddingRight());
        final int heightOfPlot = rectangle.GetHeight() - (rectangle.GetPaddingBottom() + rectangle.GetPaddingTop());
        
        int mousePositionX = mouseX - rectangle.GetPaddingLeft();
        int mousePositionY = mouseY - rectangle.GetPaddingTop();
        if(mousePositionX < 0 || mousePositionX >= widthOfPlot || mousePositionY < 0 || mousePositionY >= heightOfPlot) {
            return null;
        }
        float graphCoordX = convertScreenPxToX(widthOfPlot, mousePositionX);
        float graphCoordY = convertScreenPxToY(heightOfPlot, mousePositionY);

        final int size = graphics.size();
 
        float maxDistance = (currentLimits.maxX - currentLimits.minX) < (currentLimits.maxY - currentLimits.minY)
            ? (currentLimits.maxX - currentLimits.minX) : (currentLimits.maxY - currentLimits.minY);
        maxDistance *= 0.05f;
        SelectedPointInfo[] selectedPoints = new SelectedPointInfo[size];
        for(int i = 0; i < size; i++) {
            ArrayList<XY<Float>> points = graphics.get(i);
            if(points.size() < 2)
            {
                //We can't draw what doesn't exist
                continue;
            }
            selectedPoints[i] = SelectedPointSearchAlgorithm(points, graphCoordX, graphCoordY, maxDistance);
            if(selectedPoints[i] != null) {
                selectedPoints[i].graphic_number = i;
            }
        }
        SelectedPointInfo retPoint = selectedPoints[0];
        for(int i = 1; i < size; i++) {
            if(retPoint == null && selectedPoints[i] != null) {
                retPoint = selectedPoints[i];
            }
            if(retPoint != null && selectedPoints[i] != null
                    && selectedPoints[i].distance < retPoint.distance) {
                retPoint = selectedPoints[i];
            }
        }
        return retPoint;
    }
       
    private void DrawSelectedPoint(Graphics g, ChartRectangle rectangle, SelectedPointInfo selectedPoint) {
        final int widthOfPlot = rectangle.GetWidth() - (rectangle.GetPaddingLeft() + rectangle.GetPaddingRight());
        final int heightOfPlot = rectangle.GetHeight() - (rectangle.GetPaddingBottom() + rectangle.GetPaddingTop());
        
        if(!graphics.isEmpty()) {
            int sizeOfOval = (widthOfPlot < heightOfPlot) ? widthOfPlot : heightOfPlot;
            sizeOfOval *= 0.025f;
            XY<Float> point = graphics.get(selectedPoint.graphic_number).get(selectedPoint.point_number);
            Color color = selectedPointColor;
            g.setColor(color);
            g.fillOval(rectangle.GetPaddingLeft() + (int)convertXToScreenPx(widthOfPlot, point.x) - sizeOfOval / 2,
                    rectangle.GetPaddingTop() + (int)convertYToScreenPx(heightOfPlot, point.y) - sizeOfOval / 2, sizeOfOval, sizeOfOval);
            g.setColor(Color.lightGray);
            g.drawOval(rectangle.GetPaddingLeft() + (int)convertXToScreenPx(widthOfPlot, point.x) - sizeOfOval / 2,
                    rectangle.GetPaddingTop() + (int)convertYToScreenPx(heightOfPlot, point.y) - sizeOfOval / 2, sizeOfOval, sizeOfOval);
        }
    }

    private void DrawLabelSelectedPoint(Graphics g, ChartRectangle rectangle, SelectedPointInfo selectedPoint) {
        final int widthOfPlot = rectangle.GetWidth() - (rectangle.GetPaddingLeft() + rectangle.GetPaddingRight());
        final int heightOfPlot = rectangle.GetHeight() - (rectangle.GetPaddingBottom() + rectangle.GetPaddingTop());
        
        if(!graphics.isEmpty()) {
            XY<Float> point = graphics.get(selectedPoint.graphic_number).get(selectedPoint.point_number);

            final int SELECTED_POINT_LABEL_FONT_SIZE = 12;
            final Font selectedPointLabelFont = new Font(Font.DIALOG, Font.PLAIN, SELECTED_POINT_LABEL_FONT_SIZE);

            final String label = String.format(axisFormatValueX, point.x)
                    + ", " + String.format(axisFormatValueY, point.y);
            final TextLayout tl = new TextLayout(label, selectedPointLabelFont, new FontRenderContext(null, true, true));
            final int widthOfText = (int)tl.getBounds().getWidth();
            final int heightOfText = (int)(tl.getBounds().getHeight());
            final int widthOfOneCharacter = widthOfText / label.length();
            final int widthOfLabel = (int)(widthOfText * 1.4f);
            final int heightOfLabel = (int)(heightOfText * 1.5f);
  
            int leftCornerX = (int)convertXToScreenPx(widthOfPlot, point.x) + (int)(widthOfLabel * 0.07f);
            int leftCornerY = (int)convertYToScreenPx(heightOfPlot, point.y) - (int)(heightOfLabel);
            if(leftCornerX + widthOfLabel > widthOfPlot) {
                leftCornerX -= widthOfLabel;
            }
            if(leftCornerY - heightOfLabel < 0.0f) {
                leftCornerY += heightOfLabel;
            }
            g.setColor(Color.WHITE);
            g.fillRoundRect(rectangle.GetPaddingLeft() + leftCornerX,
                    rectangle.GetPaddingTop() + leftCornerY, widthOfLabel, heightOfLabel,
                    widthOfLabel / 10, widthOfLabel / 10);
            g.setColor(Color.GRAY);
            g.drawRoundRect(rectangle.GetPaddingLeft() + leftCornerX,
                    rectangle.GetPaddingTop() + leftCornerY, widthOfLabel, heightOfLabel,
                    widthOfLabel / 10, widthOfLabel / 10);

            g.setFont(selectedPointLabelFont);
            g.setColor(Color.BLACK);
            g.drawString(label, rectangle.GetPaddingLeft() + leftCornerX
                    + (widthOfLabel - widthOfText - widthOfOneCharacter) / 2,
                    rectangle.GetPaddingTop() + leftCornerY + heightOfLabel
                    - (heightOfLabel - heightOfText) / 2);
        }
    }

    private void showTitle(Graphics g, ChartRectangle rectangle) {
        int x, y, lengthX, lengthY;
        x = rectangle.GetPaddingLeft();
        y = 0;
        lengthX = rectangle.GetWidth() - (rectangle.GetPaddingLeft() + rectangle.GetPaddingRight());
        lengthY = rectangle.GetPaddingTop();

        g.setColor(Color.BLACK);
        g.setFont(titleFont);
        g.drawString(title, (int)(x + (lengthX - getTitleWordWidth()) / 2.0f),
                (int)(y + (lengthY + titleFontHeightPx) / 2.0f));
    }
        
    private void showNothingToShowMsg(Graphics g, ChartRectangle rectangle) {
        int x, y, lengthX, lengthY;
        x = rectangle.GetPaddingLeft();
        y = (rectangle.GetHeight() - rectangle.GetPaddingTop()) / 2;
        lengthX = rectangle.GetWidth() - (rectangle.GetPaddingLeft() + rectangle.GetPaddingRight());
        lengthY = rectangle.GetPaddingTop();

        g.setColor(Color.RED);
        g.setFont(nothingToShowFont);
        g.drawString(NOTHING_TO_SHOW_MSG, (int)(x + (lengthX - getNothingToShowWordWidth()) / 2.0f),
                (int)(y + (lengthY + nothingToShowFontHeightPx) / 2.0f));
    }
    
    private void showDecription(Graphics g, ChartRectangle rectangle,
            float maxWidthInPx) {
        int x, y, lengthX, lengthY;
        x = rectangle.GetWidth() - rectangle.GetPaddingRight();
        y = rectangle.GetPaddingTop();
        lengthX = rectangle.GetPaddingRight();
        lengthY = rectangle.GetHeight() - (rectangle.GetPaddingBottom() + rectangle.GetPaddingTop());
                
        final int size = graphicColors.size();
        final int widthOfIconColor = (int)(1.5f * axisFontWidthPx);
        final int heightOfIconColor = widthOfIconColor;
        final int indent = (int)(widthOfIconColor * 0.5f);
        final float stepYInPx = heightOfIconColor * 1.5f;
        
        final float locationX = x + (lengthX - maxWidthInPx - widthOfIconColor) / 2.0f;
        float locationY = y + (lengthY - stepYInPx * size + stepYInPx - heightOfIconColor) / 2.0f;
        
        final int maxNumCharacters = (int)(maxWidthInPx / descriptionFontWidthPx);
        
        g.setFont(descriptionFont);
        for(int i = 0; i < size; i++) {
            g.setColor(graphicColors.get(i));
            g.fillRect((int)locationX, (int)locationY,
                    widthOfIconColor, heightOfIconColor);
            
            String itemDecription = descriptions.get(i);
            if(itemDecription.length() > maxNumCharacters) {
                itemDecription = itemDecription.substring(0, maxNumCharacters);
            }
            g.drawString(itemDecription, (int)locationX + widthOfIconColor + indent,
                    (int)locationY + heightOfIconColor);
            
            locationY += stepYInPx;
        }
    }
    
    private float getTitleWordWidth() {
        return title.length() * titleFontWidthPx;
    }

    private float getNothingToShowWordWidth() {
        return NOTHING_TO_SHOW_MSG.length() * nothingToShowFontWidthPx;
    }

    private String getWordAxisX(float value) {
        return String.format(axisFormatValueX, value);
    }
    
    private float getWordWidthAxisX(float value) {
        return getWordAxisX(value).length() * axisFontWidthPx;
    }
    
    private String getWordAxisY(float value) {
        return String.format(axisFormatValueY, value);
    }
    
    private float getWordWidthAxisY(float value) {
        return getWordAxisY(value).length() * axisFontWidthPx;
    }
    
    private void showAxises(Graphics g, ChartRectangle rectangle) {
        g.setColor(Color.BLACK);
        
        g.drawLine(rectangle.GetPaddingLeft(), rectangle.GetPaddingTop(),
                rectangle.GetWidth() - rectangle.GetPaddingRight(), rectangle.GetPaddingTop());
        g.drawLine(rectangle.GetWidth() - rectangle.GetPaddingRight(), rectangle.GetPaddingTop(),
                rectangle.GetWidth() - rectangle.GetPaddingRight(), rectangle.GetHeight() - rectangle.GetPaddingBottom());
        g.drawLine(rectangle.GetPaddingLeft(), rectangle.GetHeight() - rectangle.GetPaddingBottom(),
                rectangle.GetWidth() - rectangle.GetPaddingRight(), rectangle.GetHeight() - rectangle.GetPaddingBottom());
        g.drawLine(rectangle.GetPaddingLeft(), rectangle.GetHeight() - rectangle.GetPaddingBottom(),
                rectangle.GetPaddingLeft(), rectangle.GetPaddingTop());
        
        g.setColor(Color.BLACK);
        g.setFont(axisFont);
        
        // Coefficients scaling of labels
        final int NUM_AXIS_X_LABELS = (int)Math.ceil((rectangle.GetWidth()
                - (rectangle.GetPaddingLeft() + rectangle.GetPaddingRight()))
                / (Math.max(getWordWidthAxisX(currentLimits.minX), getWordWidthAxisX(currentLimits.maxX)) * 3));
        final int NUM_AXIS_Y_LABELS = (int)Math.ceil((rectangle.GetHeight()
                - (rectangle.GetPaddingBottom() + rectangle.GetPaddingTop())) / (axisFontHeightPx * 6));
        
        showAxisX(g, rectangle, NUM_AXIS_X_LABELS);
        showAxisY(g, rectangle, NUM_AXIS_Y_LABELS);
    }
    
    private void showAxisX(Graphics g, ChartRectangle rectangle, int numLabels) {
        final int widthOfPlot = rectangle.GetWidth() - (rectangle.GetPaddingLeft() + rectangle.GetPaddingRight());
        float step = (currentLimits.maxX - currentLimits.minX) / numLabels;
        final float stepInPx = (float)convertXToScreenPx(widthOfPlot, currentLimits.minX + step);
        step = Math.abs(step);
        
        final int labelLocationY = (int)(rectangle.GetHeight() - (0.5f * rectangle.GetPaddingBottom()) + axisFontHeightPx / 2.0f);
        float labelLocationX = rectangle.GetPaddingLeft();
        
        float sum = currentLimits.minX;
        if(!currentLimits.equals(DEFAULT_CHART_LIMITS)) {
            for(int i = 0; i < numLabels + 1; i++) {
                g.drawString(getWordAxisX(sum),
                        (int)(labelLocationX - getWordWidthAxisX(sum) / 2.0f), labelLocationY);
                sum += step;
                labelLocationX += stepInPx;
            }
        }
    }
    
    private void showAxisY(Graphics g, ChartRectangle rectangle, int numLabels) {
        final int heightOfPlot = rectangle.GetHeight() - (rectangle.GetPaddingBottom() + rectangle.GetPaddingTop());
        float step = (currentLimits.maxY - currentLimits.minY) / numLabels;
        final float stepInPx = (float)(heightOfPlot - convertYToScreenPx(heightOfPlot, currentLimits.minY + step));
        step = Math.abs(step);
        
        final int labelLocationX = (int)(rectangle.GetPaddingLeft() / 2.0f);
        float labelLocationY = rectangle.GetHeight() - rectangle.GetPaddingBottom() + axisFontHeightPx / 2.0f;
                
        float sum = currentLimits.minY;
        if(!currentLimits.equals(DEFAULT_CHART_LIMITS)) {
            for(int i = 0; i < numLabels + 1; i++) {
                g.drawString(getWordAxisY(sum),
                        (int)(labelLocationX - getWordWidthAxisY(sum) / 2.0f), (int)labelLocationY);
                sum += step;
                labelLocationY -= stepInPx;
            }
        }
    }
    
    @Override
    public void paint(Graphics g)
    {
        final int width = this.getWidth();
        final int height = this.getHeight();  
        
        int paddingLeft = (int)(0.05f * width);
        int paddingRight = (int)(0.2f * width);
        
        int paddingBottom = (int)(0.05f * height);
        int paddingTop = (int)(0.05f * height);
        
        int tmp;
        tmp = (int)(1.5f * Math.max(getWordWidthAxisY(currentLimits.minY), getWordWidthAxisY(currentLimits.maxY)));
        if(paddingLeft < tmp) {
            paddingLeft = tmp;
        }
        tmp = (int)(2.5f * Math.max(getWordWidthAxisY(currentLimits.minY), getWordWidthAxisY(currentLimits.maxY)));
        if(paddingRight < tmp) {
            paddingRight = tmp;
        }   
        tmp = (int)(2.0f * axisFontHeightPx);
        if(paddingBottom < tmp) {
            paddingBottom = tmp;
        }
        tmp = (int)(2.0f * titleFontHeightPx);
        if(paddingTop < tmp) {
            paddingTop = tmp;
        }
        
        ChartRectangle tmp_rect = new ChartRectangle(width, height, paddingLeft,
            paddingRight, paddingBottom, paddingTop);
        //Clear all drawing panel
        g.clearRect(0, 0, tmp_rect.GetWidth(), tmp_rect.GetHeight());
        if(graphics != null) {
            g.setClip(tmp_rect.GetPaddingLeft(), tmp_rect.GetPaddingTop(),
                tmp_rect.GetWidth() - (tmp_rect.GetPaddingLeft() + tmp_rect.GetPaddingRight()),
                tmp_rect.GetHeight() - (tmp_rect.GetPaddingTop() + tmp_rect.GetPaddingTop()));
            if(!areaFlag) {
                plot(g, tmp_rect);
            } else {
                plotArea(g, tmp_rect);
            }
            if(currentSelectedPoint != null) {
                DrawSelectedPoint(g, tmp_rect, currentSelectedPoint);
                DrawLabelSelectedPoint(g, tmp_rect, currentSelectedPoint);
            }
            g.setClip(0, 0, tmp_rect.GetWidth(), tmp_rect.GetHeight());
            showDecription(g, tmp_rect, tmp_rect.GetPaddingRight() * 0.75f);
        }
        showAxises(g, tmp_rect);
        showTitle(g, tmp_rect);
        if(currentLimits.equals(DEFAULT_CHART_LIMITS))
        {
            showNothingToShowMsg(g, tmp_rect);
        }
        chartRectangle = tmp_rect;
    }

    private static class SelectedPointInfo {
        public int point_number;
        public int graphic_number;
        public float distance;
    }

    public static class ChartLimits {
        private float minX;
        private float maxX;
        private float minY;
        private float maxY;
        
        public ChartLimits(float minX, float maxX, float minY, float maxY) {
            this.minX = minX;
            this.maxX = maxX;
            this.minY = minY;
            this.maxY = maxY;
        }
        public ChartLimits(ChartLimits obj) {
            this.minX = obj.minX;
            this.maxX = obj.maxX;
            this.minY = obj.minY;
            this.maxY = obj.maxY;
        }

        public float GetMinimumX() {
            return minX;
        }
        public float GetMaximumX() {
            return maxX;
        }
        public float GetMinimumY() {
            return minY;
        }
        public float GetMaximumY() {
            return maxY;
        }
        public void SetMinimumX(float minX) {
            this.minX = minX;
        }
        public void SetMaximumX(float maxX) {
            this.maxX = maxX;
        }
        public void SetMinimumY(float minY) {
            this.minY = minY;
        }
        public void SetMaximumY(float maxY) {
            this.maxY = maxY;
        }
        public boolean equals(ChartLimits object) {
            return (minX == object.minX) && (maxX == object.maxX)
                    && (minY == object.minY) && (maxY == object.maxY);
        }
        public void Scale(float scaler, float lrCoeff, float btCoeff) {
            float tmp, postScaler, factor;
            factor = (scaler < 1.0f) ? -1.0f : 1.0f;
            postScaler = Math.abs(scaler - 1.0f);
            tmp = (maxX - minX) * postScaler;
            minX -= factor * lrCoeff * tmp;
            maxX += factor * (1.0f - lrCoeff) * tmp;
            tmp = (maxY - minY) * postScaler;
            minY -= factor * (1.0f - btCoeff) * tmp;
            maxY += factor * btCoeff * tmp;
        }
        public void Move(float offsetX, float offsetY) {
            minX += offsetX;
            maxX += offsetX;
            minY += offsetY;
            maxY += offsetY;
        }
    };

    private static class ChartRectangle {
        private final int width;
        private final int height;
        private final int paddingLeft;
        private final int paddingRight;
        private final int paddingBottom;
        private final int paddingTop;
        
        public ChartRectangle(int width, int height, int paddingLeft, int paddingRight,
                int paddingBottom, int paddingTop) {
            this.width = width;
            this.height = height;
            this.paddingLeft = paddingLeft;
            this.paddingRight = paddingRight;
            this.paddingBottom = paddingBottom;
            this.paddingTop = paddingTop;
        }

        public int GetWidth() {
            return width;
        }
        public int GetHeight() {
            return height;
        }
        public int GetPaddingLeft() {
            return paddingLeft;
        }
        public int GetPaddingRight() {
            return paddingRight;
        }
        public int GetPaddingBottom() {
            return paddingBottom;
        }
        public int GetPaddingTop() {
            return paddingTop;
        }
    }

    private final float titleFontWidthPx;
    private final float titleFontHeightPx;
    private final float nothingToShowFontWidthPx;
    private final float nothingToShowFontHeightPx;
    private final float axisFontWidthPx;
    private final float axisFontHeightPx;
    private final float descriptionFontWidthPx;
    private final float descriptionFontHeightPx;

    private final Font titleFont;
    private final Font nothingToShowFont;
    private final Font axisFont;
    private final Font descriptionFont;

    private boolean areaFlag;    
    private static final String NOTHING_TO_SHOW_MSG = "nothing to show";
    private static final ChartLimits DEFAULT_CHART_LIMITS = new ChartLimits(
        Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY);
    private ChartLimits currentLimits;
    private ChartLimits originalLimits;

    private boolean bMouseDraggedEvent;
    private float scaler;
    private int mouseX, mouseY;
    private Color selectedPointColor;
    private SelectedPointInfo currentSelectedPoint;
            
    private String title;
    private String axisFormatValueX;
    private String axisFormatValueY;

    private boolean bMouseListenerExisted;
    private ArrayList<Color> graphicColors;
    private ArrayList<ArrayList<XY<Float>>> graphics;
    private ArrayList<String> descriptions;
    private ChartRectangle chartRectangle;
}