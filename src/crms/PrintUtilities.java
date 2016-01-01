/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crms;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;

/**
 *
 * @author Orlon
 */
public class PrintUtilities implements Printable {

    private Component componentToBePrinted;

    public static void printComponent(Component c) {
      
        new PrintUtilities(c).print();
        
    }

    public PrintUtilities(Component componentToBePrinted) {
        this.componentToBePrinted = componentToBePrinted;
    }

    public void print() {
        PrinterJob printJob = PrinterJob.getPrinterJob();
       Show h = (Show)SwingUtilities.getWindowAncestor(componentToBePrinted);
        printJob.setJobName(h.getJobName());
        printJob.setPrintable(this);
        if (printJob.printDialog()) {
            try {
                printJob.print();
            } catch (PrinterException pe) {
                System.out.println("Error printing: " + pe);
            }
        }
    }

    public int print(Graphics g, PageFormat pageFormat, int pageIndex) {

        if (pageIndex > 0) {
            return (NO_SUCH_PAGE);
        } else {
            Graphics2D g2d = (Graphics2D) g;
            g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
// scale to fill the page        
            double dw = pageFormat.getImageableWidth();
            double dh = pageFormat.getImageableHeight();
            Dimension screenSize = //new Dimension((int) pageFormat.getWidth(), (int) pageFormat.getHeight());
new Dimension(componentToBePrinted.getHeight(), componentToBePrinted.getWidth());//Took me one day.
        //Toolkit.getDefaultToolkit().getScreenSize();

            double xScale = dw / screenSize.width;
            double yScale = dh / screenSize.height;
            double scale = Math.min(xScale, yScale);

// center the chart on the page
            double tx = 0.0;
            double ty = 0.0;
            if (xScale > scale) {
                tx = 0.5 * (xScale - scale) * screenSize.width;
            } else {
                ty = 0.5 * (yScale - scale) * screenSize.height;
            }
            g2d.translate(tx+50, ty);//for no reason?
            g2d.scale(scale, scale);
            
            
            disableDoubleBuffering(componentToBePrinted);
            componentToBePrinted.paint(g2d);
            enableDoubleBuffering(componentToBePrinted);
            return Printable.PAGE_EXISTS;
        }
    }

    /**
     * The speed and quality of printing suffers dramatically if any of the
     * containers have double buffering turned on. So this turns if off
     * globally.
     *
     * @see enableDoubleBuffering
     */
    public static void disableDoubleBuffering(Component c) {
        RepaintManager currentManager = RepaintManager.currentManager(c);
        currentManager.setDoubleBufferingEnabled(false);
    }

    /**
     * Re-enables double buffering globally.
     */
    public static void enableDoubleBuffering(Component c) {
        RepaintManager currentManager = RepaintManager.currentManager(c);
        currentManager.setDoubleBufferingEnabled(true);
    }
}
