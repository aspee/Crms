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
public class LoginButton extends JLabel implements MouseListener {

    public LoginButton() {
        setBackground(new java.awt.Color(255, 255, 255));
        setFont(new java.awt.Font("Tahoma", 0, 14));
        setForeground(new java.awt.Color(0, 0, 0));
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

        setForeground(new Color(72, 106, 179));
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        setBackground(new java.awt.Color(255, 255, 255));
        setForeground(new java.awt.Color(72, 106, 179));
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        //       setBackground(new Color(72,106,179));
//        Border one = BorderFactory.createMatteBorder(2,2, 2, 2, new Color(240,240,240));
//        Border two = BorderFactory.createMatteBorder(2, 2, 2, 2, new Color(240,240,240));
//        setBorder(BorderFactory.createCompoundBorder(one, two));
        setBackground(Color.BLACK);
        setForeground(Color.WHITE);
    }

    @Override
    public void mouseExited(MouseEvent me) {
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        setForeground(new Color(72, 106, 179));
        setBackground(Color.WHITE);
        setBorder(null);
    }

}
