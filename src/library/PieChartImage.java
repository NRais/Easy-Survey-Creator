/*
 * Here comes the text of your license
 *  * Copyright 2018 Nathan Rais 
 *  * 
 *  *      This file is part of The Easy Survey Creator.
 *  *
 *  *   The Easy Survey Creator by Nathan Rais is free software 
 *  *   but is licensed under the terms of the Creative Commons 
 *  *   Attribution NoDerivatives license (CC BY-ND). Under the CC BY-ND license 
 *  *   you may redistribute this as long as you give attribution and do not 
 *  *   modify any part of this software in any way.
 *  *
 *  *   The Easy Survey Creator is distributed in the hope that it will be useful,
 *  *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  *   CC BY-ND license for more details.
 *  *
 *  *   You should have received a copy of the CC BY-ND license along with 
 *  *   The Easy Survey Creator. If not, see <https://creativecommons.org/licenses/by-nd/4.0/>.
 * 
 */
package library;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Paint;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.AttributedString;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.util.Rotation;
import org.jfree.data.general.PieDataset;

/**
 *
 * @author Nuser
 */
public class PieChartImage {
    
    private final Image chart;
    
    public PieChartImage(final String title, final PieDataset data) {

        final PieDataset dataset = data;
        chart = createChart(title, dataset);
        
    }
    
    public Image getChart() {
        return chart;
    }
    
    /**
     * Creates a sample chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return A chart.
     */
    private Image createChart(String titleString, final PieDataset dataset) {
        
        final JFreeChart chart = ChartFactory.createPieChart3D(
            titleString,  // chart title
            dataset,                // data
            true,                   // include legend
            true,
            false
        );
        
        Font titleFont = new Font("Veranda", Font.BOLD, 20); 
        Font textFont = new Font("Veranda", Font.PLAIN, 18); 
        Font legendFont = new Font("SansSerif", Font.PLAIN, 20); 
        
        chart.getTitle().setFont(titleFont);
        
        
        Paint white = new Color(255,255,255); // Translucent black

        final PiePlot3D plot = (PiePlot3D) chart.getPlot();
        chart.getLegend().setItemFont(legendFont);
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}"));
        //plot.setLabelGenerator(new CustomLabelGenerator());
        plot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator("{2}"));
        //plot.setLegendItemShape(new Rectangle(30,30));
        plot.setBackgroundPaint(white);
        plot.setLabelFont(textFont);
        plot.setLabelBackgroundPaint(white);
        plot.setStartAngle(290);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(0.5f);
        plot.setNoDataMessage("No data to display");
        
        /// create a byte[]
        BufferedImage objBufferedImage = chart.createBufferedImage(600,800);
        ByteArrayOutputStream bas = new ByteArrayOutputStream();
                try {
                    ImageIO.write(objBufferedImage, "png", bas);
                } catch (IOException e) {
                    e.printStackTrace();
                }

        byte[] byteArray = bas.toByteArray();
        
        /// create the image
        InputStream in = new ByteArrayInputStream(byteArray);
        BufferedImage image = null;
        try {
            image = ImageIO.read(in);
            //File outputfile = new File("image.png");
            //ImageIO.write(image, "png", outputfile);
        } catch (IOException ex) {
            Logger.getLogger(PieChartImage.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return image;
        
    }
        
    /**
     * A custom label generator (returns null for one item as a test).
     */
    static class CustomLabelGenerator implements PieSectionLabelGenerator {
        
        /**
         * Generates a label for a pie section.
         * 
         * @param dataset  the dataset (<code>null</code> not permitted).
         * @param key  the section key (<code>null</code> not permitted).
         * 
         * @return the label (possibly <code>null</code>).
         */
        public String generateSectionLabel(final PieDataset dataset, final Comparable key) {
            String result = null;    
            if (dataset != null) {
                if (!key.equals("PHP")) {
                    result = key.toString();   
                }
            }
            return result;
        }

        @Override
        public AttributedString generateAttributedSectionLabel(PieDataset dataset, Comparable key) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
   
    }

}
