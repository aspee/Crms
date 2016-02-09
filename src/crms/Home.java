/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crms;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.TextField;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.text.JTextComponent;

/**
 *
 * @author Orlon
 */
public class Home extends javax.swing.JFrame {

    /**
     * Creates new form Home
     */
    CardLayout cl;
    Crimes c;
    SearchC sc;
    AddC ac;

    public Home() {

        initComponents();

        setName();
        Permissions();
        Cards.add(new Settings(this), "Settings");
        sc = new SearchC(this);
        Cards.add(sc, "Search Criminal");
        ac = new AddC(this);
        Cards.add(ac, "Add Criminal");
        Cards.add(new AddU(), "Add User");
        c = new Crimes(this);
        Cards.add(c, "Crimes");
        cl = (CardLayout) (Cards.getLayout());
        addWindowListener(exitListener);
        getContentPane().setBackground(new Color(247, 247, 247));
    }

    public void setName() {
        try {
            Hello.setText("Hello, " + Database.getName());
        } catch (SQLException ex) {
            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void showCrimes() {
        cl.show(Cards, "Crimes");
    }

    public void showAddCriminal() {
        cl.show(Cards, "Add Criminal");
    }

    public void ipc(int id) {
        c.setIPC(id);
    }

    public void ipcClear() {
        c.removeAll();
    }
    WindowListener exitListener = new WindowAdapter() {

        @Override
        public void windowClosing(WindowEvent e) {
            int confirm = JOptionPane.showOptionDialog(Home.this,
                    "Are You Sure to Log Off?",
                    "Exit Confirmation", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new Login().setVisible(true);
            }
        }
    };

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        Settings = new javax.swing.JButton();
        bAddc = new javax.swing.JButton();
        bSearch = new javax.swing.JButton();
        bAddu = new javax.swing.JButton();
        Hello = new javax.swing.JLabel();
        Cards = new javax.swing.JPanel();
        jLabel3 = new crms.CButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setBackground(new java.awt.Color(66, 133, 244));
        setFocusTraversalPolicyProvider(true);
        setForeground(new java.awt.Color(66, 133, 244));
        setResizable(false);

        jToolBar1.setBackground(new java.awt.Color(51, 51, 51));
        jToolBar1.setFloatable(false);
        jToolBar1.setForeground(new java.awt.Color(51, 51, 51));
        jToolBar1.setRollover(true);

        Settings.setBackground(new java.awt.Color(51, 51, 51));
        Settings.setForeground(new java.awt.Color(51, 51, 51));
        Settings.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/Settings.png"))); // NOI18N
        Settings.setToolTipText("");
        Settings.setFocusable(false);
        Settings.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        Settings.setMargin(new java.awt.Insets(5, 5, 5, 5));
        Settings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SettingsActionPerformed(evt);
            }
        });
        jToolBar1.add(Settings);

        bAddc.setBackground(new java.awt.Color(51, 51, 51));
        bAddc.setForeground(new java.awt.Color(51, 51, 51));
        bAddc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/Criminal.png"))); // NOI18N
        bAddc.setToolTipText("");
        bAddc.setFocusable(false);
        bAddc.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        bAddc.setMargin(new java.awt.Insets(5, 5, 5, 5));
        bAddc.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        bAddc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAddcActionPerformed(evt);
            }
        });
        jToolBar1.add(bAddc);

        bSearch.setBackground(new java.awt.Color(51, 51, 51));
        bSearch.setForeground(new java.awt.Color(51, 51, 51));
        bSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/Search.png"))); // NOI18N
        bSearch.setToolTipText("");
        bSearch.setFocusable(false);
        bSearch.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        bSearch.setMargin(new java.awt.Insets(5, 5, 5, 5));
        bSearch.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        bSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bSearchActionPerformed(evt);
            }
        });
        jToolBar1.add(bSearch);

        bAddu.setBackground(new java.awt.Color(51, 51, 51));
        bAddu.setForeground(new java.awt.Color(51, 51, 51));
        bAddu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/Add.png"))); // NOI18N
        bAddu.setToolTipText("");
        bAddu.setFocusable(false);
        bAddu.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        bAddu.setMargin(new java.awt.Insets(5, 5, 5, 5));
        bAddu.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        bAddu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAdduActionPerformed(evt);
            }
        });
        jToolBar1.add(bAddu);

        Hello.setBackground(new java.awt.Color(247, 247, 247));
        Hello.setFont(new java.awt.Font("HP Simplified", 0, 16)); // NOI18N
        Hello.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        Hello.setText("Hello, Man");
        Hello.setOpaque(true);

        Cards.setLayout(new java.awt.CardLayout());

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Log Out");
        jLabel3.setOpaque(true);
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(Hello, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(38, 38, 38)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Cards, javax.swing.GroupLayout.PREFERRED_SIZE, 906, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Hello, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Cards, javax.swing.GroupLayout.PREFERRED_SIZE, 574, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        Cards.getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bAdduActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAdduActionPerformed
        // TODO add your handling code here:
        cl.show(Cards, "Add User");
    }//GEN-LAST:event_bAdduActionPerformed

    private void bAddcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAddcActionPerformed
        // TODO add your handling code here:
        cl.show(Cards, "Add Criminal");
    }//GEN-LAST:event_bAddcActionPerformed

    private void SettingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SettingsActionPerformed
        // TODO add your handling code here:
        cl.show(Cards, "Settings");
    }//GEN-LAST:event_SettingsActionPerformed

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        // TODO add your handling code here:
        int confirm = JOptionPane.showOptionDialog(this,
                "Are You Sure to Log Off?",
                "Exit Confirmation", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, null, null);
        if (confirm == JOptionPane.YES_OPTION) {
            //System.exit(0);
            dispose();
            new Login().setVisible(true);
        }
    }//GEN-LAST:event_jLabel3MouseClicked

    private void bSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSearchActionPerformed
        // TODO add your handling code here:
        cl.show(Cards, "Search Criminal");
        sc.fetchCriminals();
        sc.clearAll();
    }//GEN-LAST:event_bSearchActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Home().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Cards;
    private javax.swing.JLabel Hello;
    private javax.swing.JButton Settings;
    private javax.swing.JButton bAddc;
    private javax.swing.JButton bAddu;
    private javax.swing.JButton bSearch;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables

    private void Permissions() {
        String Role = Database.getRole();
        if (Role.equals("Admin")) {
            //No Admin Panel
            //jToolBar1.remove(bAddu);
            jToolBar1.remove(bAddc);
            jToolBar1.remove(bSearch);

        } else if (Role.equals("Jailer")) {
            jToolBar1.remove(bAddu);

        } else if (Role.equals("CBI") || Role.equals("Police") || Role.equals("Judge")) {
            jToolBar1.remove(bAddu);
            jToolBar1.remove(bAddc);
        }
        jToolBar1.revalidate();
        jToolBar1.repaint();
    }

    void pdf(int i) {
        c.setPunish(i);
    }

    void editCriminals(int parseInt) {
        ac.editCriminal(parseInt);
        c.editIpc(parseInt);
        c.editPunish(parseInt);
    }

}
