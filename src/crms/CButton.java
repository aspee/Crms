/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crms;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.Border;

/**
 *
 * @author Orlon
 */
public class CButton extends JLabel implements MouseListener {

    public CButton() {
        setBackground(new java.awt.Color(66, 133, 244));
        setFont(new java.awt.Font("Tahoma", 0, 14));
        setForeground(new java.awt.Color(255, 255, 255));
        setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        setOpaque(true);
        this.addMouseListener(this);
        
    }

    @Override
    public void mouseClicked(MouseEvent me) {

    }

    @Override
    public void mousePressed(MouseEvent me) {
        setBackground(Color.WHITE);
        setForeground(Color.BLACK);
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        setBackground(new Color(66, 133, 244));
        setForeground(Color.WHITE);
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        Border one = BorderFactory.createEtchedBorder();
        Border two = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(one,two));
    }

    @Override
    public void mouseExited(MouseEvent me) {
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        setBorder(null);
    }

}
