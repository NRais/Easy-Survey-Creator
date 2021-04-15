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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.AttributedString;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.util.Rotation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.RefineryUtilities;

/**
 *
 * @author Nuser
 */
public class GraphChartImage {
    
        public final Image chart;
    
        public GraphChartImage( String chartTitle , CategoryDataset c) {
            
          JFreeChart barChart = ChartFactory.createBarChart(
             chartTitle,           
             "Category",            
             "Score",            
             c,          
             PlotOrientation.VERTICAL,           
             true, true, false);
          
          
            
            Font legendFont = new Font("SansSerif", Font.BOLD, 25); 
            Font labelFont = new Font("SansSerif", Font.PLAIN, 20); 
            Font textFont = new Font("Veranda", Font.PLAIN, 25); 

            ChartPanel chartPanel = new ChartPanel( barChart );        
            chartPanel.setPreferredSize(new java.awt.Dimension( 560 , 367 ) );  
          
            barChart.getLegend().setItemFont(textFont);
            
            
            CategoryPlot chartPlot = barChart.getCategoryPlot();
            ValueAxis yAxis = chartPlot.getRangeAxis();
            CategoryAxis xAxis = chartPlot.getDomainAxis();
            xAxis.setLabel("");
            yAxis.setTickLabelFont(legendFont);
            yAxis.setLabelFont(labelFont);
            yAxis.setLabel("Number of Responses");
            yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
            
          
            //TURN IT INTO A IMAGE
          
            /// create a byte[]
            BufferedImage objBufferedImage = barChart.createBufferedImage(600,800);
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
            // store our chart image
            chart = image;
        }

       private CategoryDataset createDataset( ) {
          final String fiat = "FIAT";        
          final String audi = "AUDI";        
          final String ford = "FORD";        
          final String speed = "Speed";        
          final String millage = "Millage";        
          final String userrating = "User Rating";        
          final String safety = "safety";        
          final DefaultCategoryDataset dataset = new DefaultCategoryDataset( );  

          dataset.addValue( 1.0 , fiat , speed );        
          dataset.addValue( 3.0 , fiat , userrating );        
          dataset.addValue( 5.0 , fiat , millage ); 
          dataset.addValue( 5.0 , fiat , safety );           

          dataset.addValue( 5.0 , audi , speed );        
          dataset.addValue( 6.0 , audi , userrating );       
          dataset.addValue( 10.0 , audi , millage );        
          dataset.addValue( 4.0 , audi , safety );

          dataset.addValue( 4.0 , ford , speed );        
          dataset.addValue( 2.0 , ford , userrating );        
          dataset.addValue( 3.0 , ford , millage );        
          dataset.addValue( 6.0 , ford , safety );               

          return dataset; 
       }

}
