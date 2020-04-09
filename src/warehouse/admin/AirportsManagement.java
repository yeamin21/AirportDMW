package warehouse.admin;

import java.awt.Color;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import warehouse.airport.Home;
import warehouse.airport.ConnectSQL;

public class AirportsManagement extends javax.swing.JFrame {

    ArrayList<Airports> airports = new ArrayList<>();
    Connection con = null;
    PreparedStatement stm = null;
    String sql = null;
    int xMouse = 0;
    int yMouse = 0;
    Color color3 = new Color(45, 52, 54);

    public AirportsManagement() {
        try {
            initComponents();
            this.setLocationRelativeTo(null);
            ShowAirports();
            JTableHeader theader = table_airports.getTableHeader();
            theader.setBackground(color3);
            theader.setForeground(Color.white);
        } catch (Exception e) {

        }
    }

    private void ShowAirports() {
        refreshTable();
        airports.clear();
        try {
            con = ConnectSQL.connect();
            sql = "select * from airport";
            stm = con.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Airports airport = new Airports(rs.getString("airport_code"), rs.getString("airport_name"), rs.getString("airport_location"));
                airports.add(airport);
            }
            DefaultTableModel model = (DefaultTableModel) table_airports.getModel();
            for (Airports airport : airports) {
                Object[] row = new Object[3];
                row[0] = airport.getAirport_Code();
                row[1] = airport.getAirport_Name();
                row[2] = airport.getAirport_Location();
                model.addRow(row);
            }

        } catch (SQLException e) {

        }
    }

    private void ShowAirports(String sql_query) {
        refreshTable();
        airports.clear();
        try {
            con = ConnectSQL.connect();
            sql = sql_query;
            stm = con.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Airports airport = new Airports(rs.getString("airport_code"), rs.getString("airport_name"), rs.getString("airport_location"));
                airports.add(airport);
            }
            DefaultTableModel model = (DefaultTableModel) table_airports.getModel();
            for (Airports airport : airports) {
                Object[] row = new Object[3];
                row[0] = airport.getAirport_Code();
                row[1] = airport.getAirport_Name();
                row[2] = airport.getAirport_Location();
                model.addRow(row);
            }

        } catch (SQLException e) {

        }
    }

    private void refreshTable() {
        DefaultTableModel model = (DefaultTableModel) table_airports.getModel();
        while (model.getRowCount() > 0) {
            model.setRowCount(0);
        }
    }

    private void clear() {
        txt_airportCode.setText("");
        txt_airportName.setText("");
        txt_airportLocation.setText("");

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txt_airportCode = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txt_airportName = new javax.swing.JTextField();
        txt_airportLocation = new javax.swing.JTextField();
        button_addAirport = new javax.swing.JButton();
        button_updateAirport = new javax.swing.JButton();
        button_deleteAirport = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        table_airports = new javax.swing.JTable();
        txt_searchAirport = new javax.swing.JTextField();
        button_searchAirport = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setUndecorated(true);
        getContentPane().setLayout(null);

        jPanel1.setBackground(new java.awt.Color(242, 82, 82));
        jPanel1.setForeground(new java.awt.Color(242, 82, 82));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Airport Code");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(36, 73, -1, 28));

        txt_airportCode.setBackground(new java.awt.Color(242, 82, 82));
        txt_airportCode.setForeground(new java.awt.Color(255, 255, 255));
        txt_airportCode.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(255, 255, 255)));
        txt_airportCode.setCaretColor(new java.awt.Color(255, 255, 255));
        txt_airportCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_airportCodeActionPerformed(evt);
            }
        });
        jPanel1.add(txt_airportCode, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 120, 318, 28));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Airport Name");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(36, 165, -1, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Airport Location");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(36, 249, -1, 29));

        txt_airportName.setBackground(new java.awt.Color(242, 82, 82));
        txt_airportName.setForeground(new java.awt.Color(255, 255, 255));
        txt_airportName.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(255, 255, 255)));
        txt_airportName.setCaretColor(new java.awt.Color(255, 255, 255));
        jPanel1.add(txt_airportName, new org.netbeans.lib.awtextra.AbsoluteConstraints(36, 210, 318, 28));

        txt_airportLocation.setBackground(new java.awt.Color(242, 82, 82));
        txt_airportLocation.setForeground(new java.awt.Color(255, 255, 255));
        txt_airportLocation.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(255, 255, 255)));
        txt_airportLocation.setCaretColor(new java.awt.Color(255, 255, 255));
        jPanel1.add(txt_airportLocation, new org.netbeans.lib.awtextra.AbsoluteConstraints(36, 289, 318, 28));

        button_addAirport.setBackground(new java.awt.Color(45, 52, 54));
        button_addAirport.setForeground(new java.awt.Color(255, 255, 255));
        button_addAirport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/warehouse/resourses/icons8-plus-20.png"))); // NOI18N
        button_addAirport.setText("ADD");
        button_addAirport.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        button_addAirport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_addAirportActionPerformed(evt);
            }
        });
        jPanel1.add(button_addAirport, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 420, 98, 29));

        button_updateAirport.setBackground(new java.awt.Color(45, 52, 54));
        button_updateAirport.setForeground(new java.awt.Color(255, 255, 255));
        button_updateAirport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/warehouse/resourses/icons8-restart-20.png"))); // NOI18N
        button_updateAirport.setText("UPDATE");
        button_updateAirport.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        button_updateAirport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_updateAirportActionPerformed(evt);
            }
        });
        jPanel1.add(button_updateAirport, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 420, 98, 29));

        button_deleteAirport.setBackground(new java.awt.Color(45, 52, 54));
        button_deleteAirport.setForeground(new java.awt.Color(255, 255, 255));
        button_deleteAirport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/warehouse/resourses/icons8-cancel-20.png"))); // NOI18N
        button_deleteAirport.setText("DELETE");
        button_deleteAirport.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        button_deleteAirport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_deleteAirportActionPerformed(evt);
            }
        });
        jPanel1.add(button_deleteAirport, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 420, 98, 29));

        table_airports.setBackground(new java.awt.Color(0, 99, 177));
        table_airports.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        table_airports.setForeground(new java.awt.Color(255, 255, 255));
        table_airports.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Airport Code", "Airport Name", "Airport Location"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        table_airports.setToolTipText("");
        table_airports.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
        table_airports.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        table_airports.setFocusable(false);
        table_airports.setGridColor(new java.awt.Color(255, 255, 255));
        jScrollPane1.setViewportView(table_airports);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 50, 590, 480));

        txt_searchAirport.setBackground(new java.awt.Color(242, 82, 82));
        txt_searchAirport.setForeground(new java.awt.Color(255, 255, 255));
        txt_searchAirport.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(255, 255, 255)));
        txt_searchAirport.setCaretColor(new java.awt.Color(255, 255, 255));
        txt_searchAirport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_searchAirportActionPerformed(evt);
            }
        });
        jPanel1.add(txt_searchAirport, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 10, 220, 28));

        button_searchAirport.setBackground(new java.awt.Color(45, 52, 54));
        button_searchAirport.setForeground(new java.awt.Color(255, 255, 255));
        button_searchAirport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/warehouse/resourses/search (1).png"))); // NOI18N
        button_searchAirport.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        button_searchAirport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_searchAirportActionPerformed(evt);
            }
        });
        jPanel1.add(button_searchAirport, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 10, 40, 30));

        getContentPane().add(jPanel1);
        jPanel1.setBounds(0, 30, 970, 530);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setForeground(new java.awt.Color(242, 82, 82));

        jLabel6.setBackground(new java.awt.Color(45, 52, 54));
        jLabel6.setFont(new java.awt.Font("Yu Gothic", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(242, 82, 82));
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/warehouse/resourses/airport (1).png"))); // NOI18N
        jLabel6.setText("Manage Airports");
        jLabel6.setOpaque(true);
        jLabel6.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jLabel6MouseDragged(evt);
            }
        });
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel6MousePressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 940, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        getContentPane().add(jPanel3);
        jPanel3.setBounds(0, 0, 940, 30);

        jPanel4.setBackground(new java.awt.Color(225, 41, 63));
        jPanel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel4MouseClicked(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("X");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel4);
        jPanel4.setBounds(940, 0, 30, 30);

        setSize(new java.awt.Dimension(970, 560));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void button_addAirportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_addAirportActionPerformed
        if (!txt_airportCode.getText().isEmpty() && !txt_airportName.getText().isEmpty() && !txt_airportLocation.getText().isEmpty()) {
            try {
                con = ConnectSQL.connect();
                sql = "INSERT INTO AIRPORT (airport_code, airport_name, airport_location)VALUES(?,?,?)";
                stm = con.prepareStatement(sql);

                stm.setString(1, txt_airportCode.getText());
                stm.setString(2, txt_airportName.getText());
                stm.setString(3, txt_airportLocation.getText());
                stm.execute();
                refreshTable();
                ShowAirports();
                JOptionPane.showMessageDialog(null, "Added " + txt_airportName.getText(), "Success", JOptionPane.INFORMATION_MESSAGE);
                clear();

            } catch (NumberFormatException | SQLException e) {
                   System.out.println(e);
            }
        }
    }//GEN-LAST:event_button_addAirportActionPerformed

    private void button_updateAirportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_updateAirportActionPerformed

        if (!txt_airportCode.getText().isEmpty()) {
            try {
                con = ConnectSQL.connect();
                sql = "UPDATE airport SET airport_name='" + txt_airportName.getText() + "',airport_location='" + txt_airportLocation.getText() + "' WHERE airport_code='" + txt_airportCode.getText() + "'";
                stm = con.prepareStatement(sql);
                stm.execute();

                JOptionPane.showMessageDialog(null, "Updated " + txt_airportCode.getText(), "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshTable();
                ShowAirports();
                clear();
            } catch (NumberFormatException | SQLException e) {

            }
        }

    }//GEN-LAST:event_button_updateAirportActionPerformed

    private void button_deleteAirportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_deleteAirportActionPerformed
        if (!txt_airportCode.getText().isEmpty()) {
            int P = JOptionPane.showConfirmDialog(null, " Are you sure want to delete ?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (P == 0) {
                try {
                    con = ConnectSQL.connect();
                    sql = ("DELETE FROM AIRPORT WHERE airport_code='" + txt_airportCode.getText() + "'");
                    stm = con.prepareStatement(sql);
                    stm.execute();
                    refreshTable();
                    ShowAirports();

                    JOptionPane.showMessageDialog(null, "Deleted Airport for Code: " + txt_airportCode.getText(), "Success", JOptionPane.INFORMATION_MESSAGE);
                    clear();
                } catch (SQLException | HeadlessException e) {

                }
            }

        }
    }//GEN-LAST:event_button_deleteAirportActionPerformed

    private void jLabel6MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseDragged
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation(x - xMouse, y - yMouse);        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel6MouseDragged

    private void jLabel6MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MousePressed
        xMouse = evt.getX();
        yMouse = evt.getY();
    }//GEN-LAST:event_jLabel6MousePressed

    private void txt_airportCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_airportCodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_airportCodeActionPerformed

    private void txt_searchAirportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_searchAirportActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_searchAirportActionPerformed

    private void button_searchAirportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_searchAirportActionPerformed
        sql = "SELECT * FROM  airport WHERE(airport_name LIKE '" + txt_searchAirport.getText() + "%' or airport_code like '" + txt_searchAirport.getText() + "%' or airport_location like '" + txt_searchAirport.getText() + "%')";
        ShowAirports(sql);
    }//GEN-LAST:event_button_searchAirportActionPerformed

    private void jPanel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel4MouseClicked
        this.dispose();
    }//GEN-LAST:event_jPanel4MouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton button_addAirport;
    private javax.swing.JButton button_deleteAirport;
    private javax.swing.JButton button_searchAirport;
    private javax.swing.JButton button_updateAirport;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable table_airports;
    private javax.swing.JTextField txt_airportCode;
    private javax.swing.JTextField txt_airportLocation;
    private javax.swing.JTextField txt_airportName;
    private javax.swing.JTextField txt_searchAirport;
    // End of variables declaration//GEN-END:variables
}
