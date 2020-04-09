package warehouse.airport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

public final class Warehouse extends javax.swing.JFrame {

    Connection con = null;
    PreparedStatement stm = null;
    ResultSet rs = null;
    String sql = null;
    int xMouse, yMouse;

    static String s = "SELECT *,\n"
            + "    fact_table.fact_id,\n"
            + "    b_date.booking_date,\n"
            + "    agent_name,\n"
            + "    flight_schedules.flight_number,\n"
            + "    flight_schedules.origin_airport_code,\n"
            + "    flight_schedules.destination_airport_code,\n"
            + "    fact_table.total_cost,\n"
            + "    ref_payment_status.is_paid,\n"
            + "    ref_booking_status.is_booked,\n"
            + "    ref_reservation_status.is_reserved,\n"
            + "    ref_airlines.airline_name,\n"
            + "    ref_aircraft_types.aircraft_type_code,\n"
            + "SUM(total_cost) OVER () AS sum_total,\n"
            + "count(fact_id) over() as c_total\n"
            + "FROM\n"
            + "    fact_table\n"
            + "        INNER JOIN\n"
            + "    ref_payment_status ON fact_table.payment_status_code = ref_payment_status.payment_status_code\n"
            + "        INNER JOIN\n"
            + "    ref_booking_status ON fact_table.booking_status_code = ref_booking_status.booking_status_code\n"
            + "        INNER JOIN\n"
            + "    ref_reservation_status ON fact_table.reservation_status_code = ref_reservation_status.reservation_status_code\n"
            + "        INNER JOIN\n"
            + "    booking_agents ON fact_table.agent_id = booking_agents.agent_id\n"
            + "        INNER JOIN\n"
            + "    flight_schedules ON fact_table.flight_number = flight_schedules.flight_number\n"
            + "        INNER JOIN\n"
            + "    ref_airlines ON fact_table.ref_airline_code = ref_airlines.ref_airline_code\n"
            + "        INNER JOIN\n"
            + "    ref_aircraft_types ON fact_table.aircraft_type_code = ref_aircraft_types.aircraft_type_code\n"
            + "        INNER JOIN\n"
            + "    b_date ON fact_table.booking_date = b_date.booking_date";

    public Warehouse() {
        initComponents();
        this.setLocationRelativeTo(null);
        ShowTable();
        total_count();
        total_earning();
        jcombo_add_agent();
        jcombo_add_b_date();
        add_airline();

    }

    private void ShowTable() {
        try {
            con = ConnectSQL.connect();
            sql = s;
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                Object[] row = new Object[12];
                row[0] = rs.getDate("booking_date");
                row[1] = rs.getInt("fact_id");
                row[2] = rs.getString("agent_name");
                row[3] = rs.getString("flight_number");
                row[4] = rs.getString("origin_airport_code");
                row[5] = rs.getString("destination_airport_code");
                row[6] = rs.getFloat("total_cost");
                row[7] = rs.getString("is_paid");
                row[8] = rs.getString("is_booked");
                row[9] = rs.getString("is_reserved");
                row[10] = rs.getString("airline_name");
                row[11] = rs.getString("aircraft_type_code");

                model.addRow(row);
            }
        } catch (Exception e) {

        }

    }

    private void refreshTable() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        while (model.getRowCount() > 0) {
            model.setRowCount(0);
        }
    }

    public String total_count() {
        try {
            con = ConnectSQL.connect();
            sql = s;
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                String c = String.valueOf(rs.getInt("c_total"));
                jLabel5.setText(c);
                return c;

            }
        } catch (Exception e) {

        }
        return null;
    }

    public String total_earning() {
        try {
            con = ConnectSQL.connect();
            sql = Warehouse.s;
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {

                float x = (float) (Math.round(Float.valueOf(rs.getString("Sum_Total")) * 100.0) / 100.0);
                String te = String.valueOf(x);
                jLabel7.setText(te);
                return te;

            }
        } catch (SQLException | NumberFormatException e) {

        }
        return null;
    }

    private void jcombo_add_agent() {
        try {
            con = ConnectSQL.connect();
            sql = "Select agent_name from booking_agents";
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                jComboBox2.addItem(rs.getString("agent_name"));
            }
        } catch (Exception e) {

        }
    }

    private void jcombo_add_b_date() {
        try {
            con = ConnectSQL.connect();
            sql = "Select booking_date from b_date";
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                jComboBox3.addItem(rs.getDate("booking_date"));
            }
        } catch (Exception e) {

        }
    }

    private void add_airline() {
        try {
            con = ConnectSQL.connect();
            sql = "Select airline_name from ref_airlines";
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                jComboBox4.addItem(rs.getString("airline_name"));
            }
        } catch (SQLException e) {
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jComboBox3 = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        search = new javax.swing.JButton();
        jComboBox4 = new javax.swing.JComboBox();
        jLabel11 = new javax.swing.JLabel();
        reset = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setForeground(new java.awt.Color(242, 82, 82));

        jLabel6.setBackground(new java.awt.Color(45, 52, 54));
        jLabel6.setFont(new java.awt.Font("Yu Gothic", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(242, 82, 82));
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/warehouse/resourses/warehouse.png"))); // NOI18N
        jLabel6.setText("Airport Data Warehouse");
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
            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 1130, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jTable1.setBackground(new java.awt.Color(120, 224, 143));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Fact ID", "Agent", "Flight", "Origin", "Destination", "Cost", "Paid", "Booked", "Reserved", "Airline", "Aircraft"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 1160, 300));

        jPanel1.setBackground(new java.awt.Color(242, 82, 82));
        jPanel1.setLayout(null);
        jPanel1.add(jLabel1);
        jLabel1.setBounds(281, 0, 0, 0);

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "" }));
        jComboBox2.setBorder(null);
        jPanel1.add(jComboBox2);
        jComboBox2.setBounds(162, 93, 167, 28);

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "" }));
        jComboBox3.setBorder(null);
        jPanel1.add(jComboBox3);
        jComboBox3.setBounds(163, 47, 167, 28);

        jLabel9.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Agents");
        jPanel1.add(jLabel9);
        jLabel9.setBounds(48, 95, 45, 20);

        jLabel10.setBackground(new java.awt.Color(255, 255, 255));
        jLabel10.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Booking Date");
        jPanel1.add(jLabel10);
        jLabel10.setBounds(48, 49, 97, 20);

        search.setBackground(new java.awt.Color(44, 62, 80));
        search.setFont(new java.awt.Font("Yu Gothic UI", 0, 12)); // NOI18N
        search.setForeground(new java.awt.Color(255, 255, 255));
        search.setText("Search");
        search.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchActionPerformed(evt);
            }
        });
        jPanel1.add(search);
        search.setBounds(226, 158, 103, 32);

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "" }));
        jComboBox4.setBorder(null);
        jComboBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox4ActionPerformed(evt);
            }
        });
        jPanel1.add(jComboBox4);
        jComboBox4.setBounds(430, 50, 167, 28);

        jLabel11.setBackground(new java.awt.Color(255, 255, 255));
        jLabel11.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Airline");
        jPanel1.add(jLabel11);
        jLabel11.setBounds(375, 49, 61, 20);

        reset.setBackground(new java.awt.Color(44, 62, 80));
        reset.setFont(new java.awt.Font("Yu Gothic UI", 0, 12)); // NOI18N
        reset.setForeground(new java.awt.Color(255, 255, 255));
        reset.setText("Reset");
        reset.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        reset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetActionPerformed(evt);
            }
        });
        jPanel1.add(reset);
        reset.setBounds(360, 160, 103, 32);

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 330, 910, 230));

        jPanel2.setBackground(new java.awt.Color(44, 62, 80));
        jPanel2.setForeground(new java.awt.Color(255, 255, 255));

        jLabel3.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Bookings: ");

        jLabel4.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Total Earning:");

        jLabel5.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);

        jLabel7.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(36, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(71, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 330, 250, 230));

        jPanel6.setBackground(new java.awt.Color(225, 41, 63));
        jPanel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel6MouseClicked(evt);
            }
        });

        jLabel2.setBackground(new java.awt.Color(225, 41, 63));
        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("X");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(1130, 0, -1, -1));

        setSize(new java.awt.Dimension(1160, 557));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel6MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MousePressed
        xMouse = evt.getX();
        yMouse = evt.getY();
    }//GEN-LAST:event_jLabel6MousePressed

    private void jLabel6MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseDragged
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation(x - xMouse, y - yMouse);
    }//GEN-LAST:event_jLabel6MouseDragged

    private void searchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchActionPerformed

        con = ConnectSQL.connect();
        jLabel7.setText(null);
        jLabel5.setText(null);
        refreshTable();
        if (jComboBox3.getSelectedItem().equals("") && jComboBox2.getSelectedItem().equals("") && jComboBox4.getSelectedItem().equals("")) {
            try {
                sql = s;
                stm = con.prepareStatement(sql);
                rs = stm.executeQuery();
                while (rs.next()) {
                    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                    Object[] row = new Object[12];
                    row[0] = rs.getDate("booking_date");
                    row[1] = rs.getInt("fact_id");
                    row[2] = rs.getString("agent_name");
                    row[3] = rs.getString("flight_number");
                    row[4] = rs.getString("origin_airport_code");
                    row[5] = rs.getString("destination_airport_code");
                    row[6] = rs.getFloat("total_cost");
                    row[7] = rs.getString("is_paid");
                    row[8] = rs.getString("is_booked");
                    row[9] = rs.getString("is_reserved");
                    row[10] = rs.getString("airline_name");
                    row[11] = rs.getString("aircraft_type_code");
                    model.addRow(row);
                    float x = (float) (Math.round(Float.valueOf(rs.getString("sum_total")) * 100.0) / 100.0);
                    jLabel7.setText(String.valueOf(x));
                    jLabel5.setText(rs.getString("c_total"));
                }
            } catch (NumberFormatException | SQLException e) {

            }
//3selected
        } else if (!jComboBox3.getSelectedItem().equals("") && !jComboBox2.getSelectedItem().equals("") && !jComboBox4.getSelectedItem().equals("")) {
            try {
                sql = s + " where fact_table.booking_date='" + jComboBox3.getSelectedItem() + "' and agent_name='" + jComboBox2.getSelectedItem() + "' and airline_name='" + jComboBox4.getSelectedItem() + "'";
                stm = con.prepareStatement(sql);
                rs = stm.executeQuery();
                while (rs.next()) {
                    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                    Object[] row = new Object[12];
                    row[0] = rs.getDate("booking_date");
                    row[1] = rs.getInt("fact_id");
                    row[2] = rs.getString("agent_name");
                    row[3] = rs.getString("flight_number");
                    row[4] = rs.getString("origin_airport_code");
                    row[5] = rs.getString("destination_airport_code");
                    row[6] = rs.getFloat("total_cost");
                    row[7] = rs.getString("is_paid");
                    row[8] = rs.getString("is_booked");
                    row[9] = rs.getString("is_reserved");
                    row[10] = rs.getString("airline_name");
                    row[11] = rs.getString("aircraft_type_code");
                    model.addRow(row);
                    float x = (float) (Math.round(Float.valueOf(rs.getString("sum_total")) * 100.0) / 100.0);
                    jLabel7.setText(String.valueOf(x));
                    jLabel5.setText(rs.getString("c_total"));
                }
            } catch (SQLException | NumberFormatException e) {
                System.out.println(e);
            }

        } //j2 j3|| j4
        else if (!jComboBox3.getSelectedItem().equals("") && !jComboBox2.getSelectedItem().equals("") && jComboBox4.getSelectedItem().equals("")) {
            try {
                sql = s + " where fact_table.booking_date='" + jComboBox3.getSelectedItem() + "' and agent_name='" + jComboBox2.getSelectedItem() + "'";
                stm = con.prepareStatement(sql);
                rs = stm.executeQuery();
                while (rs.next()) {
                    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                    Object[] row = new Object[12];
                    row[0] = rs.getDate("booking_date");
                    row[1] = rs.getInt("fact_id");
                    row[2] = rs.getString("agent_name");
                    row[3] = rs.getString("flight_number");
                    row[4] = rs.getString("origin_airport_code");
                    row[5] = rs.getString("destination_airport_code");
                    row[6] = rs.getFloat("total_cost");
                    row[7] = rs.getString("is_paid");
                    row[8] = rs.getString("is_booked");
                    row[9] = rs.getString("is_reserved");
                    row[10] = rs.getString("airline_name");
                    row[11] = rs.getString("aircraft_type_code");
                    model.addRow(row);

                    float x = (float) (Math.round(Float.valueOf(rs.getString("sum_total")) * 100.0) / 100.0);
                    jLabel7.setText(String.valueOf(x));
                    jLabel5.setText(rs.getString("c_total"));

                }
            } catch (SQLException | NumberFormatException e) {

            }

            //1,3 ||2
        } else if (!jComboBox3.getSelectedItem().equals("") && jComboBox2.getSelectedItem().equals("") && !jComboBox4.getSelectedItem().equals("")) {
            try {
                sql = s + " where fact_table.booking_date='" + jComboBox3.getSelectedItem() + "' and airline_name='" + jComboBox4.getSelectedItem() + "'";
                stm = con.prepareStatement(sql);
                rs = stm.executeQuery();
                while (rs.next()) {
                    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                    Object[] row = new Object[12];
                    row[0] = rs.getDate("booking_date");
                    row[1] = rs.getInt("fact_id");
                    row[2] = rs.getString("agent_name");
                    row[3] = rs.getString("flight_number");
                    row[4] = rs.getString("origin_airport_code");
                    row[5] = rs.getString("destination_airport_code");
                    row[6] = rs.getFloat("total_cost");
                    row[7] = rs.getString("is_paid");
                    row[8] = rs.getString("is_booked");
                    row[9] = rs.getString("is_reserved");
                    row[10] = rs.getString("airline_name");
                    row[11] = rs.getString("aircraft_type_code");
                    model.addRow(row);
                    float x = (float) (Math.round(Float.valueOf(rs.getString("sum_total")) * 100.0) / 100.0);
                    jLabel7.setText(String.valueOf(x));
                    jLabel5.setText(rs.getString("c_total"));
                }
            } catch (SQLException | NumberFormatException e) {

            }

        } //2,3||1
        else if (jComboBox3.getSelectedItem().equals("") && !jComboBox2.getSelectedItem().equals("") && !jComboBox4.getSelectedItem().equals("")) {
            try {
                sql = s + " where agent_name='" + jComboBox2.getSelectedItem() + "' and airline_name='" + jComboBox4.getSelectedItem() + "'";
                stm = con.prepareStatement(sql);
                rs = stm.executeQuery();
                while (rs.next()) {
                    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                    Object[] row = new Object[12];
                    row[0] = rs.getDate("booking_date");
                    row[1] = rs.getInt("fact_id");
                    row[2] = rs.getString("agent_name");
                    row[3] = rs.getString("flight_number");
                    row[4] = rs.getString("origin_airport_code");
                    row[5] = rs.getString("destination_airport_code");
                    row[6] = rs.getFloat("total_cost");
                    row[7] = rs.getString("is_paid");
                    row[8] = rs.getString("is_booked");
                    row[9] = rs.getString("is_reserved");
                    row[10] = rs.getString("airline_name");
                    row[11] = rs.getString("aircraft_type_code");
                    model.addRow(row);
                    float x = (float) (Math.round(Float.valueOf(rs.getString("sum_total")) * 100.0) / 100.0);
                    jLabel7.setText(String.valueOf(x));
                    jLabel5.setText(rs.getString("c_total"));
                }
            } catch (SQLException | NumberFormatException e) {

            }

        } //1
        else if (jComboBox3.getSelectedItem()!="" && jComboBox2.getSelectedItem().equals("") && jComboBox4.getSelectedItem().equals("")) {
            try {
                sql = s + " where  fact_table.booking_date='" + jComboBox3.getSelectedItem() + "'";
                stm = con.prepareStatement(sql);
                rs = stm.executeQuery();
                while (rs.next()) {
                    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                    Object[] row = new Object[12];
                    row[0] = rs.getDate("booking_date");
                    row[1] = rs.getInt("fact_id");
                    row[2] = rs.getString("agent_name");
                    row[3] = rs.getString("flight_number");
                    row[4] = rs.getString("origin_airport_code");
                    row[5] = rs.getString("destination_airport_code");
                    row[6] = rs.getFloat("total_cost");
                    row[7] = rs.getString("is_paid");
                    row[8] = rs.getString("is_booked");
                    row[9] = rs.getString("is_reserved");
                    row[10] = rs.getString("airline_name");
                    row[11] = rs.getString("aircraft_type_code");
                    model.addRow(row);
                    float x = (float) (Math.round(Float.valueOf(rs.getString("sum_total")) * 100.0) / 100.0);
                    jLabel7.setText(String.valueOf(x));
                    jLabel5.setText(rs.getString("c_total"));
                }
            } catch (SQLException | NumberFormatException e) {

            }

        } //2
        else if (jComboBox3.getSelectedItem().equals("") && !jComboBox2.getSelectedItem().equals("") && jComboBox4.getSelectedItem().equals("")) {
            try {
                sql = s + " where agent_name='" + jComboBox2.getSelectedItem() + "'";
                stm = con.prepareStatement(sql);
                rs = stm.executeQuery();
                while (rs.next()) {
                    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                    Object[] row = new Object[12];
                    row[0] = rs.getDate("booking_date");
                    row[1] = rs.getInt("fact_id");
                    row[2] = rs.getString("agent_name");
                    row[3] = rs.getString("flight_number");
                    row[4] = rs.getString("origin_airport_code");
                    row[5] = rs.getString("destination_airport_code");
                    row[6] = rs.getFloat("total_cost");
                    row[7] = rs.getString("is_paid");
                    row[8] = rs.getString("is_booked");
                    row[9] = rs.getString("is_reserved");
                    row[10] = rs.getString("airline_name");
                    row[11] = rs.getString("aircraft_type_code");
                    model.addRow(row);
                    float x = (float) (Math.round(Float.valueOf(rs.getString("sum_total")) * 100.0) / 100.0);
                    jLabel7.setText(String.valueOf(x));
                    jLabel5.setText(rs.getString("c_total"));
                }
            } catch (SQLException | NumberFormatException e) {

            }

        } //3
        else if (jComboBox3.getSelectedItem().equals("") && jComboBox2.getSelectedItem().equals("") && !jComboBox4.getSelectedItem().equals("")) {
            try {
                sql = s + " where  airline_name='" + jComboBox4.getSelectedItem() + "'";
                stm = con.prepareStatement(sql);
                rs = stm.executeQuery();
                while (rs.next()) {
                    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                    Object[] row = new Object[12];
                    row[0] = rs.getDate("booking_date");
                    row[1] = rs.getInt("fact_id");
                    row[2] = rs.getString("agent_name");
                    row[3] = rs.getString("flight_number");
                    row[4] = rs.getString("origin_airport_code");
                    row[5] = rs.getString("destination_airport_code");
                    row[6] = rs.getFloat("total_cost");
                    row[7] = rs.getString("is_paid");
                    row[8] = rs.getString("is_booked");
                    row[9] = rs.getString("is_reserved");
                    row[10] = rs.getString("airline_name");
                    row[11] = rs.getString("aircraft_type_code");
                    model.addRow(row);
                    float x = (float) (Math.round(Float.valueOf(rs.getString("sum_total")) * 100.0) / 100.0);
                    jLabel7.setText(String.valueOf(x));
                    jLabel5.setText(rs.getString("c_total"));
                }
            } catch (SQLException | NumberFormatException e) {

            }

        }
    }//GEN-LAST:event_searchActionPerformed

    private void jPanel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel6MouseClicked
        this.dispose();
    }//GEN-LAST:event_jPanel6MouseClicked

    private void resetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetActionPerformed

        jComboBox2.setSelectedItem("");
        jComboBox3.setSelectedItem("");
        jComboBox4.setSelectedItem("");
        refreshTable();
        try {
            sql = s;
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                Object[] row = new Object[12];
                row[0] = rs.getDate("booking_date");
                row[1] = rs.getInt("fact_id");
                row[2] = rs.getString("agent_name");
                row[3] = rs.getString("flight_number");
                row[4] = rs.getString("origin_airport_code");
                row[5] = rs.getString("destination_airport_code");
                row[6] = rs.getFloat("total_cost");
                row[7] = rs.getString("is_paid");
                row[8] = rs.getString("is_booked");
                row[9] = rs.getString("is_reserved");
                row[10] = rs.getString("airline_name");
                row[11] = rs.getString("aircraft_type_code");
                model.addRow(row);
                float x = (float) (Math.round(Float.valueOf(rs.getString("sum_total")) * 100.0) / 100.0);
                jLabel7.setText(String.valueOf(x));
                jLabel5.setText(rs.getString("c_total"));
            }
        } catch (Exception e) {

        }

    }//GEN-LAST:event_resetActionPerformed

    private void jComboBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox4ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton reset;
    private javax.swing.JButton search;
    // End of variables declaration//GEN-END:variables
}
