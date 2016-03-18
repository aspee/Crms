/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crms;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.FileInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Orlon
 */
public class AddC extends javax.swing.JPanel {

    /**
     * Creates new form AddC
     */
    JFileChooser chooser;
    DefaultComboBoxModel mState, mCity, aState, aCity;
    PreparedStatement pst;
    static boolean editing = false;
    boolean imagePresent = false;
    Home parent;
    byte[] tmpimg;

    public AddC(javax.swing.JFrame parent) {

        mState = new DefaultComboBoxModel();
        mCity = new DefaultComboBoxModel();
        aState = new DefaultComboBoxModel();
        aCity = new DefaultComboBoxModel();
        allStates();
        arrestedState();
        initComponents();
        chooser = new JFileChooser();
        tdob.setMaxSelectableDate(new Date());
        tdob.setDate(null);
        this.parent = (Home) parent;
        CID.setText("#" + currentID());

        if (!Database.getRole().equals("Judge")) {
            jLabel6.setVisible(false);
        }
        if (Database.getRole().equals("Police")) {
            JPanel tJpanel = null;
            for (Component c : this.getComponents()) {
                if (c.getClass().toString().contains("javax.swing.JPanel")) {
                    tJpanel = (JPanel) c;
                    for (Component d : tJpanel.getComponents()) {
                        d.setEnabled(false);
                    }
                }
            }
            IMAGE.setEnabled(true);
            tAddress.setEnabled(false);
            tAdditional.setEnabled(false);
        }

        tCell.setTransferHandler(null);
        tEyes.setTransferHandler(null);
        tFacility.setTransferHandler(null);
        tFirst.setTransferHandler(null);
        tFoot.setTransferHandler(null);
        tInch.setTransferHandler(null);
        tLast.setTransferHandler(null);
        tMiddle.setTransferHandler(null);
        tSection.setTransferHandler(null);
        tWeight.setTransferHandler(null);
        tdob.setTransferHandler(null);
        tAdditional.setTransferHandler(null);
        tAddress.setTransferHandler(null);
        tArrestdate.setTransferHandler(null);

    }

    public void setAll() {
        try {
            int oldid = Integer.parseInt(CID.getText().substring(1));
            if (editing) {
                Database.getStatement().execute("SET FOREIGN_KEY_CHECKS=0;");
                Database.getStatement().execute("delete from tblIPC where id=" + oldid);
                Database.getStatement().execute("delete from tblpunishment where id=" + oldid);
                byteImage(oldid);
                Database.getStatement().execute("delete from mtblCriminals where cid=" + oldid);
                Database.getStatement().execute("delete from crimelocation where cid=" + oldid);
                Database.getStatement().execute("SET FOREIGN_KEY_CHECKS=1;");
                pst = Database.getConnection().prepareStatement("insert into mtblCriminals(cid,image,image_size,ad,fname,mname,lname,dob,state,city,address,gender,mstatus,color,hair,bg,height,weight,eyes,facility,section,cell,ai) values(" + oldid + ",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                editing = false;
            } else {
                pst = Database.getConnection().prepareStatement("insert into mtblCriminals(cid,image,image_size,ad,fname,mname,lname,dob,state,city,address,gender,mstatus,color,hair,bg,height,weight,eyes,facility,section,cell,ai) values(null,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            }

            Date dateFromDateChooser = tdob.getDate();
            Date dateFromDateChooser1 = tArrestdate.getDate();
            String dateString = (String.format("%1$tY-%1$tm-%1$td", dateFromDateChooser));
            if (dateString.equals("null-null-null")) {
                dateString = null;
            }
            String dateString1 = String.format("%1$tY-%1$tm-%1$td", dateFromDateChooser1);
            storeImage();
            pst.setString(3, dateString1);
            pst.setString(4, tFirst.getText());
            pst.setString(5, tMiddle.getText());
            pst.setString(6, tLast.getText());
            pst.setString(7, dateString);
            pst.setString(8, mState.getSelectedItem().toString());
            pst.setString(9, mCity.getSelectedItem().toString());
            pst.setString(10, tAddress.getText());
            pst.setString(11, rMale.isSelected() ? "M" : rFemale.isSelected() ? "F" : "U");
            pst.setString(12, cMarital.getSelectedItem().toString());
            pst.setString(13, tColor.getSelectedItem().toString());
            pst.setString(14, cHair.getSelectedItem().toString());
            pst.setString(15, cBloodgroup.getSelectedItem().toString());
            double Height = Double.parseDouble(tFoot.getText() + "." + tInch.getText() + "0");
            if (Height == 0.0) {
                pst.setNull(16, Types.DOUBLE);
            } else {
                pst.setDouble(16, Height);
            }
            if (tWeight.getText().equals("")) {
                pst.setDouble(17, 0);
            } else {
                pst.setDouble(17, Double.parseDouble(tWeight.getText()));
            }
            pst.setString(18, tEyes.getText());
            pst.setString(19, tFacility.getText());
            pst.setString(20, tSection.getText());
            pst.setString(21, tCell.getText());
            pst.setString(22, tAdditional.getText());
            pst.executeUpdate();
            parent.ipc(oldid);
            parent.pdf(oldid);
            editing = false;

        } catch (SQLException ex) {
            Logger.getLogger(AddC.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (Database.getRole().equals("Police")) {
            tFacility.setEnabled(false);
            tSection.setEnabled(false);
            tCell.setEnabled(false);
            bSave1.setEnabled(false);
        } else {
            tArrestdate.setEnabled(true);
            clearAll();
            CID.setText("#" + currentID());
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        lMiddle = new javax.swing.JLabel();
        cMarital = new javax.swing.JComboBox();
        rFemale = new javax.swing.JRadioButton();
        rMale = new javax.swing.JRadioButton();
        lState = new javax.swing.JLabel();
        cState = new javax.swing.JComboBox(mState);
        tLast = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tAddress = new javax.swing.JTextArea();
        lName = new javax.swing.JLabel();
        ldob = new javax.swing.JLabel();
        lMarital = new javax.swing.JLabel();
        tFirst = new javax.swing.JTextField();
        lGender = new javax.swing.JLabel();
        tMiddle = new javax.swing.JTextField();
        lFirst = new javax.swing.JLabel();
        lLast = new javax.swing.JLabel();
        lAddress = new javax.swing.JLabel();
        lCity = new javax.swing.JLabel();
        cCity = new javax.swing.JComboBox(mCity);
        jRadioButton1 = new javax.swing.JRadioButton();
        tdob = new com.toedter.calendar.JDateChooser();
        jLabel6 = new crms.CButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tAdditional = new javax.swing.JTextArea();
        lAdditional = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lSection = new javax.swing.JLabel();
        tCell = new javax.swing.JTextField();
        lCell = new javax.swing.JLabel();
        lFacility = new javax.swing.JLabel();
        tSection = new javax.swing.JTextField();
        tFacility = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        bClear = new crms.CButton()
        ;
        bSave1 = new crms.CButton()
        ;
        jPanel6 = new javax.swing.JPanel();
        IMAGE = new javax.swing.JLabel();
        lArrest = new javax.swing.JLabel();
        CID = new javax.swing.JTextField();
        lId = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        bBrowse = new crms.CButton()
        ;
        bRemove = new crms.CButton()
        ;
        tArrestdate = new com.toedter.calendar.JDateChooser();
        jPanel4 = new javax.swing.JPanel();
        lColor = new javax.swing.JLabel();
        tWeight = new javax.swing.JTextField();
        lInch = new javax.swing.JLabel();
        tInch = new javax.swing.JTextField();
        lHeight = new javax.swing.JLabel();
        lFoot = new javax.swing.JLabel();
        lHair = new javax.swing.JLabel();
        tFoot = new javax.swing.JTextField();
        tEyes = new javax.swing.JTextField();
        cHair = new javax.swing.JComboBox();
        lWeight = new javax.swing.JLabel();
        lBloodgroup = new javax.swing.JLabel();
        lKg = new javax.swing.JLabel();
        cBloodgroup = new javax.swing.JComboBox();
        lEyes = new javax.swing.JLabel();
        tColor = new javax.swing.JComboBox();

        setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setOpaque(false);

        lMiddle.setFont(new java.awt.Font("HP Simplified Light", 0, 14)); // NOI18N
        lMiddle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lMiddle.setText("Middle");

        cMarital.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cMarital.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " ", "Single", "Married", "Widowed", "Separated", "Divorced" }));
        cMarital.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cMaritalActionPerformed(evt);
            }
        });
        cMarital.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cMaritalKeyTyped(evt);
            }
        });

        rFemale.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(rFemale);
        rFemale.setFont(new java.awt.Font("HP Simplified Light", 0, 12)); // NOI18N
        rFemale.setText("Female");

        rMale.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(rMale);
        rMale.setFont(new java.awt.Font("HP Simplified Light", 0, 12)); // NOI18N
        rMale.setText("Male");

        lState.setFont(new java.awt.Font("HP Simplified Light", 0, 14)); // NOI18N
        lState.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lState.setText("State ");

        cState.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cState.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cStateItemStateChanged(evt);
            }
        });
        cState.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cStateActionPerformed(evt);
            }
        });

        tLast.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tLastActionPerformed(evt);
            }
        });
        tLast.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tLastKeyTyped(evt);
            }
        });

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        tAddress.setColumns(20);
        tAddress.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tAddress.setLineWrap(true);
        tAddress.setRows(5);
        tAddress.setWrapStyleWord(true);
        tAddress.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tAddressKeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(tAddress);

        lName.setFont(new java.awt.Font("HP Simplified Light", 0, 14)); // NOI18N
        lName.setText("Name");

        ldob.setFont(new java.awt.Font("HP Simplified Light", 0, 14)); // NOI18N
        ldob.setText("Date of Birth");

        lMarital.setFont(new java.awt.Font("HP Simplified Light", 0, 14)); // NOI18N
        lMarital.setText("Marital Status ");

        tFirst.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tFirst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tFirstActionPerformed(evt);
            }
        });
        tFirst.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tFirstKeyTyped(evt);
            }
        });

        lGender.setFont(new java.awt.Font("HP Simplified Light", 0, 14)); // NOI18N
        lGender.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lGender.setText("Gender ");

        tMiddle.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tMiddle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tMiddleActionPerformed(evt);
            }
        });
        tMiddle.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tMiddleKeyTyped(evt);
            }
        });

        lFirst.setFont(new java.awt.Font("HP Simplified Light", 0, 14)); // NOI18N
        lFirst.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lFirst.setText("First");

        lLast.setFont(new java.awt.Font("HP Simplified Light", 0, 14)); // NOI18N
        lLast.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lLast.setText("Last");
        lLast.setToolTipText("");

        lAddress.setFont(new java.awt.Font("HP Simplified Light", 0, 14)); // NOI18N
        lAddress.setText("Address ");

        lCity.setFont(new java.awt.Font("HP Simplified Light", 0, 14)); // NOI18N
        lCity.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lCity.setText("City ");

        cCity.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cCity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cCityActionPerformed(evt);
            }
        });

        jRadioButton1.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setFont(new java.awt.Font("HP Simplified Light", 0, 12)); // NOI18N
        jRadioButton1.setSelected(true);
        jRadioButton1.setText("Unknown");

        tdob.setDateFormatString("d/MM/yyyy");
        tdob.setMaxSelectableDate(new java.util.Date(253370748675000L));
        tdob.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tdobMouseClicked(evt);
            }
        });

        jLabel6.setText("Crimes & Punishments");
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel6MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lAddress, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ldob, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lMarital, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(32, 32, 32)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lFirst, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tFirst, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cMarital, 0, 160, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(tdob, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(29, 29, 29)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lMiddle, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                            .addComponent(tMiddle))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(tLast, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lLast, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lGender, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)
                            .addComponent(lState, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lCity, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cState, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cCity, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(rMale)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(rFemale)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jRadioButton1)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap(11, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(lName))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lFirst)
                            .addComponent(lMiddle)
                            .addComponent(lLast))
                        .addGap(3, 3, 3)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tFirst, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tMiddle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tLast, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(24, 24, 24)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(ldob)
                        .addComponent(lState)
                        .addComponent(cState, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(tdob, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lCity)
                            .addComponent(cCity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rMale)
                            .addComponent(rFemale)
                            .addComponent(lGender)
                            .addComponent(jRadioButton1)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lAddress))
                .addGap(18, 30, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cMarital, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lMarital, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.setOpaque(false);

        tAdditional.setColumns(20);
        tAdditional.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tAdditional.setLineWrap(true);
        tAdditional.setRows(5);
        tAdditional.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tAdditionalKeyTyped(evt);
            }
        });
        jScrollPane2.setViewportView(tAdditional);

        lAdditional.setFont(new java.awt.Font("HP Simplified Light", 0, 14)); // NOI18N
        lAdditional.setText("Additional Information");

        jLabel2.setForeground(new java.awt.Color(255, 0, 0));
        jLabel2.setText("*");

        jLabel3.setForeground(new java.awt.Color(255, 0, 0));
        jLabel3.setText("*");

        jLabel4.setForeground(new java.awt.Color(255, 0, 0));
        jLabel4.setText("*");

        lSection.setFont(new java.awt.Font("HP Simplified Light", 0, 14)); // NOI18N
        lSection.setText("Section");

        tCell.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tCell.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tCellKeyTyped(evt);
            }
        });

        lCell.setFont(new java.awt.Font("HP Simplified Light", 0, 14)); // NOI18N
        lCell.setText("Cell");

        lFacility.setFont(new java.awt.Font("HP Simplified Light", 0, 14)); // NOI18N
        lFacility.setText("Facility");

        tSection.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tSection.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tSectionKeyTyped(evt);
            }
        });

        tFacility.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tFacility.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tFacilityActionPerformed(evt);
            }
        });
        tFacility.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tFacilityKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lFacility)
                    .addComponent(lSection, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lCell, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 56, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tCell, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                    .addComponent(tSection, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                    .addComponent(tFacility))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addGap(107, 107, 107)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lAdditional)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lAdditional)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(lFacility)
                        .addGap(24, 24, 24)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lSection)
                            .addComponent(tSection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lCell)
                            .addComponent(tCell, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tFacility, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel5.setOpaque(false);

        bClear.setText("Clear");
        bClear.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        bClear.setPreferredSize(new java.awt.Dimension(83, 25));
        bClear.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bClearMouseClicked(evt);
            }
        });

        bSave1.setText("Save");
        bSave1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        bSave1.setPreferredSize(new java.awt.Dimension(83, 25));
        bSave1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bSave1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(bSave1, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(bClear, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bClear, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bSave1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel6.setOpaque(false);

        IMAGE.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/jinsignia.alpha.png"))); // NOI18N

        lArrest.setFont(new java.awt.Font("HP Simplified Light", 0, 14)); // NOI18N
        lArrest.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lArrest.setText("Arrest Date");

        CID.setEditable(false);
        CID.setFont(new java.awt.Font("HP Simplified Light", 0, 14)); // NOI18N
        CID.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        CID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CIDActionPerformed(evt);
            }
        });

        lId.setFont(new java.awt.Font("HP Simplified Light", 0, 14)); // NOI18N
        lId.setText("ID");
        lId.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        jLabel1.setForeground(new java.awt.Color(255, 0, 0));
        jLabel1.setText("*");

        bBrowse.setText("Browse");
        bBrowse.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        bBrowse.setPreferredSize(new java.awt.Dimension(83, 25));
        bBrowse.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bBrowseMouseClicked(evt);
            }
        });

        bRemove.setText("Remove");
        bRemove.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        bRemove.setPreferredSize(new java.awt.Dimension(83, 25));
        bRemove.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bRemoveMouseClicked(evt);
            }
        });

        tArrestdate.setDateFormatString("d/MM/yyyy");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(lId)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(CID, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(IMAGE, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel6Layout.createSequentialGroup()
                                            .addComponent(lArrest, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(0, 0, Short.MAX_VALUE))
                                        .addComponent(tArrestdate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                                    .addComponent(bBrowse, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(43, 43, 43)
                                    .addComponent(bRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lId))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(IMAGE, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bBrowse, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(lArrest)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
                    .addComponent(tArrestdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel4.setOpaque(false);

        lColor.setFont(new java.awt.Font("HP Simplified Light", 0, 14)); // NOI18N
        lColor.setText("Color");

        tWeight.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tWeight.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tWeightKeyTyped(evt);
            }
        });

        lInch.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lInch.setText("inch");

        tInch.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tInch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tInchKeyTyped(evt);
            }
        });

        lHeight.setFont(new java.awt.Font("HP Simplified Light", 0, 14)); // NOI18N
        lHeight.setText("Height");

        lFoot.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lFoot.setText("ft.");

        lHair.setFont(new java.awt.Font("HP Simplified Light", 0, 14)); // NOI18N
        lHair.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lHair.setText("Hair");

        tFoot.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tFoot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tFootActionPerformed(evt);
            }
        });
        tFoot.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tFootKeyTyped(evt);
            }
        });

        tEyes.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tEyes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tEyesKeyTyped(evt);
            }
        });

        cHair.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cHair.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " ", "Straight", "Curly" }));

        lWeight.setFont(new java.awt.Font("HP Simplified Light", 0, 14)); // NOI18N
        lWeight.setText("Weight");

        lBloodgroup.setFont(new java.awt.Font("HP Simplified Light", 0, 14)); // NOI18N
        lBloodgroup.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lBloodgroup.setText("Blood Group ");

        lKg.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lKg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lKg.setText("Kg.");

        cBloodgroup.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cBloodgroup.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " ", "A +ve", "A -ve", "B +ve", "B -ve", "O +ve", "O -ve", "AB +ve", "AB -ve" }));
        cBloodgroup.setToolTipText("");
        cBloodgroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cBloodgroupActionPerformed(evt);
            }
        });

        lEyes.setFont(new java.awt.Font("HP Simplified Light", 0, 14)); // NOI18N
        lEyes.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lEyes.setText("Eyes ");

        tColor.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tColor.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " ", "Light", "Fair", "Medium", "Olive", "Brown", "Black" }));
        tColor.setToolTipText("");
        tColor.setDoubleBuffered(true);
        tColor.setOpaque(false);
        tColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tColorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lHeight)
                    .addComponent(lColor))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(tFoot, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lFoot)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tInch, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lInch))
                    .addComponent(tColor, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(lHair, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cHair, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lBloodgroup, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(lWeight)
                        .addGap(18, 18, 18)
                        .addComponent(tWeight, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lKg, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lEyes, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cBloodgroup, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tEyes, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lColor)
                        .addComponent(tColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lBloodgroup)
                        .addComponent(cBloodgroup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cHair, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lHair)))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lHeight)
                        .addComponent(tFoot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lFoot)
                        .addComponent(tInch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lInch))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(tEyes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lEyes)
                        .addComponent(lKg))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lWeight)
                        .addComponent(tWeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(29, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(48, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void CIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CIDActionPerformed

    private void tFootActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tFootActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tFootActionPerformed

    private void cBloodgroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cBloodgroupActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cBloodgroupActionPerformed

    private void tFootKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tFootKeyTyped
        // TODO add your handling code here:
        char caracter = evt.getKeyChar();
        if (((caracter < '0') || (caracter > '9')) || tFoot.getText().length() >= 1 && (caracter != '\b')) {
            evt.consume();
        }
    }//GEN-LAST:event_tFootKeyTyped

    private void tInchKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tInchKeyTyped
        // TODO add your handling code here:
        char caracter = evt.getKeyChar();
        if (((caracter < '0') || (caracter > '9')) || tInch.getText().length() >= 2 && (caracter != '\b')) {
            evt.consume();
        } else {
            if (!"".equals(tInch.getText())) {
                if (Integer.parseInt(tInch.getText() + "" + caracter) > 11) {
                    evt.consume();
                }
            }
        }

    }//GEN-LAST:event_tInchKeyTyped

    private void tWeightKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tWeightKeyTyped
        // TODO add your handling code here:
        char caracter = evt.getKeyChar();
        if (((caracter < '0') || (caracter > '9')) || tWeight.getText().length() >= 3 && (caracter != '\b')) {
            evt.consume();
        }
    }//GEN-LAST:event_tWeightKeyTyped

    private void tColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tColorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tColorActionPerformed

    private void bBrowseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bBrowseMouseClicked
        if (bBrowse.isEnabled()) {

            chooser.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "JPG & PNG Images", "jpg", "png");
            chooser.setFileFilter(filter);
            int returnVal = chooser.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                IMAGE.setIcon(new ImageIcon(((new ImageIcon("" + chooser.getSelectedFile())).getImage()).getScaledInstance(192, 192, java.awt.Image.SCALE_SMOOTH)));
                imagePresent = true;
            }
        }
    }//GEN-LAST:event_bBrowseMouseClicked

    private void bRemoveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bRemoveMouseClicked
        if (bRemove.isEnabled()) {
            IMAGE.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/jinsignia.alpha.png")));
            imagePresent = false;
        }
    }//GEN-LAST:event_bRemoveMouseClicked

    private void bClearMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bClearMouseClicked
        if (bClear.isEnabled()) {
            clearAll();
            tArrestdate.setEnabled(true);
        }
    }//GEN-LAST:event_bClearMouseClicked

    private void bSave1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bSave1MouseClicked
        if (bSave1.isEnabled()) {

            if (verify()) {
                setAll();
                Saved();
            }
        }
    }//GEN-LAST:event_bSave1MouseClicked

    private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseClicked
        if (jLabel6.isEnabled()) {
            Home h = (Home) parent;
            h.showCrimes();
        }
    }//GEN-LAST:event_jLabel6MouseClicked

    private void cCityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cCityActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cCityActionPerformed

    private void tMiddleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tMiddleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tMiddleActionPerformed

    private void tFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tFirstActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tFirstActionPerformed

    private void tLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tLastActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tLastActionPerformed

    private void cStateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cStateActionPerformed

    }//GEN-LAST:event_cStateActionPerformed

    private void cStateItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cStateItemStateChanged
        // TODO add your handling code here:
        setcState();
    }//GEN-LAST:event_cStateItemStateChanged

    private void cMaritalKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cMaritalKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_cMaritalKeyTyped

    private void cMaritalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cMaritalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cMaritalActionPerformed

    private void tCellKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tCellKeyTyped
        // TODO add your handling code here:
        char caracter = evt.getKeyChar();
        if (((caracter < '0') || (caracter > '9')) || tCell.getText().length() >= 11 && (caracter != '\b')) {
            evt.consume();
        }
    }//GEN-LAST:event_tCellKeyTyped

    private void tFacilityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tFacilityActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tFacilityActionPerformed

    private void tdobMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tdobMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tdobMouseClicked

    private void tFirstKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tFirstKeyTyped
        char caracter = evt.getKeyChar();
        if (tFirst.getText().length() >= 20 || !(caracter < '0' || caracter > '9')) {
            evt.consume();
        }
    }//GEN-LAST:event_tFirstKeyTyped

    private void tMiddleKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tMiddleKeyTyped
        char caracter = evt.getKeyChar();
        if (tMiddle.getText().length() >= 20 || !(caracter < '0' || caracter > '9')) {
            evt.consume();
        }
    }//GEN-LAST:event_tMiddleKeyTyped

    private void tLastKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tLastKeyTyped
        char caracter = evt.getKeyChar();
        if (tLast.getText().length() >= 20 || !(caracter < '0' || caracter > '9')) {
            evt.consume();
        }
    }//GEN-LAST:event_tLastKeyTyped

    private void tAddressKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tAddressKeyTyped
        char caracter = evt.getKeyChar();
        if (tAddress.getText().length() >= 100) {
            evt.consume();
        }
    }//GEN-LAST:event_tAddressKeyTyped

    private void tFacilityKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tFacilityKeyTyped
        char caracter = evt.getKeyChar();
        if (tFacility.getText().length() >= 20) {
            evt.consume();
        }
    }//GEN-LAST:event_tFacilityKeyTyped

    private void tSectionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tSectionKeyTyped
        char caracter = evt.getKeyChar();
        if (tSection.getText().length() >= 20) {
            evt.consume();
        }
    }//GEN-LAST:event_tSectionKeyTyped

    private void tAdditionalKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tAdditionalKeyTyped
        char caracter = evt.getKeyChar();
        if (tAdditional.getText().length() >= 500) {
            evt.consume();
        }
    }//GEN-LAST:event_tAdditionalKeyTyped

    private void tEyesKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tEyesKeyTyped
        char caracter = evt.getKeyChar();
        if (tEyes.getText().length() >= 20) {
            evt.consume();
        }
    }//GEN-LAST:event_tEyesKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField CID;
    private javax.swing.JLabel IMAGE;
    private javax.swing.JLabel bBrowse;
    private javax.swing.JLabel bClear;
    private javax.swing.JLabel bRemove;
    private javax.swing.JLabel bSave1;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox cBloodgroup;
    private javax.swing.JComboBox cCity;
    private javax.swing.JComboBox cHair;
    private javax.swing.JComboBox cMarital;
    private javax.swing.JComboBox cState;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lAdditional;
    private javax.swing.JLabel lAddress;
    private javax.swing.JLabel lArrest;
    private javax.swing.JLabel lBloodgroup;
    private javax.swing.JLabel lCell;
    private javax.swing.JLabel lCity;
    private javax.swing.JLabel lColor;
    private javax.swing.JLabel lEyes;
    private javax.swing.JLabel lFacility;
    private javax.swing.JLabel lFirst;
    private javax.swing.JLabel lFoot;
    private javax.swing.JLabel lGender;
    private javax.swing.JLabel lHair;
    private javax.swing.JLabel lHeight;
    private javax.swing.JLabel lId;
    private javax.swing.JLabel lInch;
    private javax.swing.JLabel lKg;
    private javax.swing.JLabel lLast;
    private javax.swing.JLabel lMarital;
    private javax.swing.JLabel lMiddle;
    private javax.swing.JLabel lName;
    private javax.swing.JLabel lSection;
    private javax.swing.JLabel lState;
    private javax.swing.JLabel lWeight;
    private javax.swing.JLabel ldob;
    private javax.swing.JRadioButton rFemale;
    private javax.swing.JRadioButton rMale;
    private javax.swing.JTextArea tAdditional;
    private javax.swing.JTextArea tAddress;
    private com.toedter.calendar.JDateChooser tArrestdate;
    private javax.swing.JTextField tCell;
    private javax.swing.JComboBox tColor;
    private javax.swing.JTextField tEyes;
    private javax.swing.JTextField tFacility;
    private javax.swing.JTextField tFirst;
    private javax.swing.JTextField tFoot;
    private javax.swing.JTextField tInch;
    private javax.swing.JTextField tLast;
    private javax.swing.JTextField tMiddle;
    private javax.swing.JTextField tSection;
    private javax.swing.JTextField tWeight;
    private com.toedter.calendar.JDateChooser tdob;
    // End of variables declaration//GEN-END:variables

    private void storeImage() {
        File file;
        String s = "null";
        byte[] absent = s.getBytes();
        String filePath = null;
        try {
            if (imagePresent == false) {
                pst.setBytes(1, absent);
                pst.setInt(2, 1);
            } else if (imagePresent == true) {
                file = chooser.getSelectedFile();

                if (file != null) {
                    filePath = file.getPath();
                }
                if (filePath != null) {
                    FileInputStream fileInputStream = new FileInputStream(filePath);
                    byte b[] = new byte[fileInputStream.available()];
                    fileInputStream.read(b);
                    fileInputStream.close();
                    pst.setBytes(1, b);
                    pst.setInt(2, 1);
                } else {
                    pst.setBytes(1, tmpimg);
                    pst.setInt(2, 2);

                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private int currentID() {
        //To change body of generated methods, choose Tools | Templates.
        int currentid = 0;
        try {
            ResultSet rs = Database.getStatement().executeQuery("SELECT `AUTO_INCREMENT` FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'crms' AND TABLE_NAME = 'mtblCriminals';");
            rs.next();
            currentid = rs.getInt(1);

        } catch (SQLException ex) {
            Logger.getLogger(AddC.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return (currentid);
    }

    private void clearAll() {
        JTextField tfield = null;
        JPanel tJpanel = null;
        for (Component c : this.getComponents()) {
            if (c.getClass().toString().contains("javax.swing.JPanel")) {
                tJpanel = (JPanel) c;
                for (Component d : tJpanel.getComponents()) {
                    if (d.getClass().toString().contains("javax.swing.JTextField")) {
                        tfield = (JTextField) d;
                        tfield.setText("");
                    }
                }
            }
        }
        IMAGE.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/jinsignia.alpha.png")));
        imagePresent = false;
        tAddress.setText("");
        tAdditional.setText("");
        tdob.setDate(null);
        jRadioButton1.setSelected(true);
        tColor.setSelectedItem("");
        tArrestdate.setDate(null);
        mState.setSelectedItem("");
        mCity.setSelectedItem("");
        aState.setSelectedItem("");
        aCity.setSelectedItem("");
        cMarital.setSelectedIndex(0);
        cHair.setSelectedIndex(0);
        cBloodgroup.setSelectedIndex(0);
        CID.setText("#" + currentID());
        parent.ipcClear();

    }

    private Boolean verify() {

        String s = "";
        Boolean a = true;
        if (tdob.getDate() != null && tdob.getDate().after(new Date())) {
            s += "Dob cannot be in the future\n";
            a = a & false;
        }
        if (tArrestdate.getDate() == null) {
            s += "Arrest Date Required\n";
            a = a & false;
        } else {
            if (!editing) {
                if (tArrestdate.getDate().after(new Date())) {
                    s += "Arrest Date cannot be in future\n";
                    a = a & false;
                }
            }
        }
        if ("".equals(tSection.getText())) {
            s += "Section Required\n";
            a = a & false;
        }
        if ("".equals(tFacility.getText())) {
            s += "Facility Required\n";
            a = a & false;
        }
        if ("".equals(tCell.getText())) {
            s += "Cell Required\n";
            a = a & false;
        } else {
            try {
                ResultSet rs = Database.getStatement().executeQuery("select cid from mtblcriminals where cid<>" + CID.getText().substring(1) + " "
                        + "and cell=" + tCell.getText() + " "
                        + "and facility='" + tFacility.getText() + "' "
                        + "and section='" + tSection.getText() + "'");
                if (rs.next()) {
                    String tempid = rs.getString(1);
                    rs = Database.getStatement().executeQuery("select tdate from tblPunishment where id=" + tempid + " and tdate>curdate()");
                    if (rs.next()) {
                        s += "Cell Occupied\n";
                        a = a & false;
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(AddC.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (!a) {
            JOptionPane.showMessageDialog(null, s, "Error!", JOptionPane.ERROR_MESSAGE);
        }
        return a;

    }

    private void allStates() {
        try {

            //cState.removeAllItems();
            ResultSet rs2 = Database.getStatement().executeQuery("select name from location where parent_id=100");
            //rs2.next();
            mState.addElement("");
            mCity.addElement("");
            while (rs2.next()) {
                mState.addElement("" + rs2.getString(1));

            }
        } catch (SQLException ex) {
            Logger.getLogger(AddC.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void Saved() {

        final JDialog dlg = new dSave(null, true);
        dlg.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dlg.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                super.componentShown(e);
                final Timer t = new Timer(3000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dlg.dispose();
                    }
                });
                t.start();
            }
        });
        dlg.setVisible(true);

    }

    void editCriminal(int cid) {
        clearAll();
        CID.setText("#" + cid);
        tArrestdate.setEnabled(false);
        try {

            ResultSet edit = Database.getStatement().executeQuery("select * from mtblCriminals where cid=" + cid);
            if (edit.next()) {
                this.editing = true;
                if (!(new String(edit.getBytes("image"))).equals("null")) {
                    imagePresent = true;
                    IMAGE.setIcon(new ImageIcon(((new ImageIcon(edit.getBytes("image"))).getImage()).getScaledInstance(192, 192, java.awt.Image.SCALE_SMOOTH)));
                } else {
                    IMAGE.setIcon(new ImageIcon(((new ImageIcon(getClass().getResource("/res/alert.png"))).getImage()).getScaledInstance(192, 192, java.awt.Image.SCALE_SMOOTH)));

                }
                String tempad = null;
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                if ((tempad = edit.getString("ad")) != null) {
                    tArrestdate.setDate(formatter.parse(tempad));
                } else {
                    tArrestdate.setDate(null);
                }
                tFirst.setText(edit.getString("fname"));
                tMiddle.setText(edit.getString("mname"));
                tLast.setText(edit.getString("lname"));
                String tempDate = null;

                if ((tempDate = edit.getString("dob")) != null) {
                    tdob.setDate(formatter.parse(tempDate));
                } else {
                    tdob.setDate(null);
                }
                cState.setSelectedItem("" + edit.getString("state"));
                setcState();
            }
            ResultSet edit1 = Database.getStatement().executeQuery("select * from mtblCriminals where cid=" + cid);
            if (edit1.next());
            {
                cCity.setSelectedItem("" + edit1.getString(10));
                tAddress.setText("" + edit1.getString("address"));
                cCity.setSelectedItem("" + edit1.getString("city"));
                tAdditional.setText(edit1.getString("ai"));
                String gender = edit1.getString("gender");
                if (gender.equals("M")) {
                    rMale.setSelected(true);
                } else if (gender.equals("F")) {
                    rFemale.setSelected(true);
                } else {
                    jRadioButton1.setSelected(true);
                }
                cMarital.setSelectedItem("" + edit1.getString("mstatus"));
                tColor.setSelectedItem(edit1.getString("color"));
                cHair.setSelectedItem(edit1.getString("hair"));
                cBloodgroup.setSelectedItem("" + edit1.getString("bg"));
                if (!edit1.getString("weight").equals("0")) {
                    tWeight.setText("" + edit1.getString("weight"));
                }
                String height = "" + edit1.getDouble("height");
                if (height.equals("0.0")) {
                    tFoot.setText("");
                    tInch.setText("");
                } else {
                    int dec = height.indexOf(".");
                    tFoot.setText("" + height.substring(0, dec));
                    tInch.setText("" + height.substring(dec + 1, height.length()));

                }
                tEyes.setText(edit1.getString("eyes"));
                tFacility.setText(edit1.getString("facility"));
                tSection.setText(edit1.getString("section"));
                tCell.setText(edit1.getString("cell"));
                tAdditional.setText(edit1.getString("ai"));

            }
        } catch (SQLException ex) {
            Logger.getLogger(AddC.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(AddC.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void setcState() {
        try {
            // TODO add your handling code here:
            //cCity.removeAllItems();
            //  mState.removeElement("");
            mCity.removeAllElements();

            ResultSet rs = Database.getStatement().executeQuery("select location_id from location where name='" + mState.getSelectedItem() + "'");
            int lid = 100;
            while (rs.next()) {
                lid = rs.getInt(1);
            }
            ResultSet rs1 = Database.getStatement().executeQuery("select name from location where parent_id=" + lid);
            Boolean empty = true;
            while (rs1.next()) {
                mCity.addElement(rs1.getString(1));
                empty = false;
            }
            if (empty == true) {
                mCity.addElement("");

            }

        } catch (SQLException ex) {
            Logger.getLogger(AddC.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void arrestedState() {
        try {

            //cState.removeAllItems();
            ResultSet rs2 = Database.getStatement().executeQuery("select name from location where parent_id=100");
            //rs2.next();
            aState.addElement("");
            aCity.addElement("");
            while (rs2.next()) {
                aState.addElement("" + rs2.getString(1));

            }
        } catch (SQLException ex) {
            Logger.getLogger(AddC.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void setaCity() {
        try {
            // TODO add your handling code here:
            //cCity.removeAllItems();
            //  mState.removeElement("");
            aCity.removeAllElements();
            ResultSet rs = Database.getStatement().executeQuery("select location_id from location where name='" + aState.getSelectedItem() + "'");
            int lid = 100;
            while (rs.next()) {
                lid = rs.getInt(1);
            }
            ResultSet rs1 = Database.getStatement().executeQuery("select name from location where parent_id=" + lid);
            Boolean empty = true;
            while (rs1.next()) {
                aCity.addElement(rs1.getString(1));
                empty = false;
            }
            if (empty == true) {
                aCity.addElement("");

            }

        } catch (SQLException ex) {
            Logger.getLogger(AddC.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void ifpolice() {
        if (Database.getRole().equals("Police")) {
            JPanel tJpanel = null;
            for (Component c : this.getComponents()) {
                if (c.getClass().toString().contains("javax.swing.JPanel")) {
                    tJpanel = (JPanel) c;
                    for (Component d : tJpanel.getComponents()) {
                        d.setEnabled(false);
                    }
                }
            }
            IMAGE.setEnabled(true);
            tAddress.setEnabled(false);
            tAdditional.setEnabled(false);
            tFacility.setEnabled(true);
            tCell.setEnabled(true);
            tSection.setEnabled(true);
            bSave1.setEnabled(true);
        }
    }

    private void byteImage(int oldid) {
        try {
            ResultSet ima = Database.getStatement().executeQuery("select image  from mtblCriminals where cid=" + oldid);
            if (ima.next()) {
                tmpimg = ima.getBytes(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AddC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
