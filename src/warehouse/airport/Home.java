package warehouse.airport;

import warehouse.admin.BookingAgentsManagement;
import warehouse.admin.AircraftTypesManagement;
import warehouse.admin.AirportsManagement;
import warehouse.admin.AirlinesManagement;
import warehouse.admin.FlightScheduleManagement;
import warehouse.admin.TicketTypesManagement;
import warehouse.admin.PaymentStatusManagement;
import warehouse.admin.TravelClassesManagement;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import warehouse.admin.AirlineAircraftManagement;

public final class Home extends javax.swing.JFrame {

    Connection con = null;
    PreparedStatement stm = null;
    ResultSet rs = null;
    String sql = null;
    int xMouse, yMouse;

    static Color color1 = new Color(87, 101, 116);
    static Color color2 = new Color(224, 81, 81);
    public Color color3 = new Color(45, 52, 54);
    static Color color4 = new Color(126, 214, 223);
    static Color color5 = new Color(186, 220, 88);
    static Color color6 = new Color(225, 41, 63);
    static Color color7 = new Color(45, 52, 54);
    Thread t = null;

    public Home() {

        initComponents();
        this.setLocationRelativeTo(null);
        clock();
        panel_pieChart.setVisible(false);
        panel_barChart.setVisible(false);
        dashboard_panels();

        jLabel15.setText(showTotalBookings()[1] + " BDT");
        lbl_totalBookings.setText("Total Bookings: " + showTotalBookings()[0]);
        txt_adminWelcome.setText("Welcome " + LoginsAndCustomerBookings.adminID);

    }

    String[] showTotalBookings() {
        try {
            con = ConnectSQL.connect();
            sql = "SELECT sum(cost) as sum_total,\n"
                    + "COUNT(booking.booking_status_code) AS total_bookings\n"
                    + "FROM\n"
                    + "booking\n"
                    + "inner join  billing on billing.booking_status_code=booking.booking_status_code";
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            String s[] = new String[2];
            while (rs.next()) {
                s[0] = String.valueOf(rs.getInt("total_bookings"));
                s[1] = String.valueOf(rs.getDouble("sum_total"));
                return s;

            }

        } catch (SQLException e) {

        }
        return null;

    }

    private void dashboard_panels() {
        show_best_agent();
        show_most_popular_airline();
        DashboardStartingCharts();
    }

    private void resetPanel(JPanel x) {

        x.repaint();
        x.validate();
    }

    public void show_best_agent() {

        try {
            con = ConnectSQL.connect();
            sql = "SELECT top 1 agent_name,\n"
                    + "COUNT(agent_name) AS booking_processed\n"
                    + "FROM Booking\n"
                    + "INNER JOIN booking_agents ON Booking.bookingagent = booking_agents.agent_id\n"
                    + "GROUP BY agent_name";
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                lbl_bestBookingAgentName.setText(rs.getString("agent_name"));
                lbl_totalContributionOfBestAgent.setText("Processed: " + String.valueOf(rs.getInt("booking_processed")));
            }

        } catch (SQLException e) {

        }

    }

    public void show_most_popular_airline() {
        try {
            con = ConnectSQL.connect();
            sql = "SELECT top 1 sum(cost) as earned,airline_name\n"
                    + "FROM booking \n"
                    + "inner join flights on Booking.flight_no=flights.flight_no\n"
                    + "iNNER JOIN airline_aircraft on flights.airline_aircraft_code=airline_aircraft.airline_aircraft_code\n"
                    + "inner join airline on airline_aircraft.airline_code=airline.airline_code\n"
                    + "inner join billing on billing.booking_status_code=Booking.booking_status_code\n"
                    + "group by airline_name";
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                jLabel20.setText(rs.getString("airline_name"));
                jLabel21.setText(String.valueOf(rs.getDouble("earned") + " Taka"));
            }

        } catch (SQLException e) {
            System.err.println(e);

        }
    }

    public void showPie(String sql_query, String count, String name, String title) {

        try {
            con = ConnectSQL.connect();
            sql = sql_query;
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            DefaultPieDataset dataset = new DefaultPieDataset();
            while (rs.next()) {

                dataset.setValue(rs.getString(name), rs.getInt(count));

                JFreeChart chart = ChartFactory.createPieChart(
                        title,
                        dataset,
                        false,
                        false,
                        false);

                ChartPanel CPane = new ChartPanel(chart);
                chart.getTitle().setPaint(Color.white);
                chart.getTitle().setFont(new Font("SansSerif", Font.PLAIN, 23));
                chart.setBackgroundPaint(color5);
                Plot plot = chart.getPlot();
                plot.setBackgroundPaint(color5);
                panel_pieChart.removeAll();
                panel_pieChart.add(CPane, BorderLayout.CENTER);
                panel_pieChart.repaint();
                panel_pieChart.validate();
                panel_pieChart.setVisible(true);
            }

        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    public void showBar(String sql_query, String income, String name, String title, String horizontal, String vertical, String index) {
        try {
            con = ConnectSQL.connect();
            sql = sql_query;
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            while (rs.next()) {

                dataset.setValue(rs.getDouble(income), index, rs.getString(name));
            }
            JFreeChart barChart = ChartFactory.createBarChart(
                    title,
                    horizontal,
                    vertical,
                    dataset,
                    PlotOrientation.VERTICAL,
                    false, false, false);

            ChartPanel CPane = new ChartPanel(barChart);

            barChart.setBackgroundPaint(color4);
            Plot plot = barChart.getPlot();
            plot.setBackgroundPaint(color4);
            barChart.getTitle().setPaint(Color.white);
            barChart.getTitle().setFont(new Font("SansSerif", Font.PLAIN, 23));
            panel_barChart.removeAll();
            panel_barChart.add(CPane, BorderLayout.CENTER);
            panel_barChart.repaint();
            panel_barChart.validate();
            panel_barChart.setVisible(true);
        } catch (SQLException e) {
            System.out.println(e);

        }
    }

    private void DashboardStartingCharts() {
        String s = "SELECT bookingdate,sum(cost) as sum_total,\n"
                + "COUNT(bookingdate) AS c_booking_date\n"
                + "FROM booking\n"
                + "inner join  billing on billing.booking_status_code=booking.booking_status_code\n"
                + "gROUP BY bookingdate";
        String ib = "sum_total";
        String anb = "bookingdate";
        String tb = "BDT";
        String cb = "Income";
        String ab = "Date";
        String db = "taka";
        showBar(s, ib, anb, cb, ab, tb, db);
        String ip = "c_booking_date";
        String anp = "bookingdate";
        String cp = "Bookings";
        showPie(s, ip, anp, cp);

    }

    /*
    * clock() is used to show current date and time.
    * lbl_time shows current time 
    * lbl_date shows date
     */
    public void clock() {
        Thread tr = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    Date date = Calendar.getInstance().getTime();
                    DateFormat f1 = new SimpleDateFormat("EEEE, dd MMMM yyyy");
                    DateFormat f2 = new SimpleDateFormat("hh:mm:ss a");
                    String today = f1.format(date);
                    String time = f2.format(date);

                    lbl_time.setText(time);
                    lbl_date.setText(today);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                    }
                }
            }
        });
        tr.start();
    }

    /*
    this method has no work in this project
    created to complete jUnit test
     */
    public String NullAgents() {

        try {
            con = ConnectSQL.connect();
            sql = "SELECT \n"
                    + "ref_reservation_status.is_reserved, \n"
                    + "count(fact_id) over() as c_not_reserved \n"
                    + "FROM\n"
                    + " fact_table\n"
                    + " INNER JOIN \n"
                    + " ref_reservation_status \n"
                    + " ON \n"
                    + " fact_table.reservation_status_code = ref_reservation_status.reservation_status_code"
                    + " where agent_id is null";
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                return rs.getString("agent_id");
            }
        } catch (SQLException e) {

        }
        return null;

    }

    private void SwitchPanel(JPanel jp1) {
        layeredPanel_Warehouse.removeAll();
        layeredPanel_Warehouse.add(jp1);
        layeredPanel_Warehouse.repaint();
        layeredPanel_Warehouse.validate();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        manage_data_bpannel = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        dashboard_bpanel = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        warehouse_bpanel = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        txt_adminWelcome = new javax.swing.JLabel();
        logut_admin = new javax.swing.JLabel();
        layeredPanel_Warehouse = new javax.swing.JLayeredPane();
        dashboard_panel = new javax.swing.JPanel();
        lbl_time = new javax.swing.JLabel();
        total_earning_panel = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        lbl_totalBookings = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        m_popular_aircraft_panel = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        lbl_date = new javax.swing.JLabel();
        b_booking_agent_panel = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        lbl_totalContributionOfBestAgent = new javax.swing.JLabel();
        lbl_bestBookingAgentName = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        panel_pieChart = new javax.swing.JPanel();
        panel_barChart = new javax.swing.JPanel();
        admin_panel = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jPanel22 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jPanel23 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jPanel24 = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setIconImages(null);
        setUndecorated(true);
        getContentPane().setLayout(null);

        jPanel3.setBackground(new java.awt.Color(45, 52, 54));
        jPanel3.setForeground(new java.awt.Color(242, 82, 82));

        jLabel6.setBackground(new java.awt.Color(45, 52, 54));
        jLabel6.setFont(new java.awt.Font("Yu Gothic", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(242, 82, 82));
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/warehouse/resourses/home.png"))); // NOI18N
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
            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 940, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        getContentPane().add(jPanel3);
        jPanel3.setBounds(0, 0, 940, 30);

        jPanel4.setBackground(new java.awt.Color(45, 52, 54));
        jPanel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel4MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel4MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanel4MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel4MousePressed(evt);
            }
        });

        jLabel2.setBackground(new java.awt.Color(102, 102, 102));
        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("X");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel4);
        jPanel4.setBounds(940, 0, 30, 30);

        jPanel1.setBackground(new java.awt.Color(45, 52, 54));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        manage_data_bpannel.setBackground(new java.awt.Color(242, 82, 82));
        manage_data_bpannel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                manage_data_bpannelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                manage_data_bpannelMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                manage_data_bpannelMousePressed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Manage Data");

        javax.swing.GroupLayout manage_data_bpannelLayout = new javax.swing.GroupLayout(manage_data_bpannel);
        manage_data_bpannel.setLayout(manage_data_bpannelLayout);
        manage_data_bpannelLayout.setHorizontalGroup(
            manage_data_bpannelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, manage_data_bpannelLayout.createSequentialGroup()
                .addContainerGap(106, Short.MAX_VALUE)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        manage_data_bpannelLayout.setVerticalGroup(
            manage_data_bpannelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(manage_data_bpannelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(manage_data_bpannel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 180, 250, -1));

        dashboard_bpanel.setBackground(new java.awt.Color(242, 82, 82));
        dashboard_bpanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                dashboard_bpanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                dashboard_bpanelMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                dashboard_bpanelMousePressed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Dashboard");

        javax.swing.GroupLayout dashboard_bpanelLayout = new javax.swing.GroupLayout(dashboard_bpanel);
        dashboard_bpanel.setLayout(dashboard_bpanelLayout);
        dashboard_bpanelLayout.setHorizontalGroup(
            dashboard_bpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dashboard_bpanelLayout.createSequentialGroup()
                .addContainerGap(102, Short.MAX_VALUE)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        dashboard_bpanelLayout.setVerticalGroup(
            dashboard_bpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dashboard_bpanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(dashboard_bpanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 110, 250, -1));

        warehouse_bpanel.setBackground(new java.awt.Color(242, 82, 82));
        warehouse_bpanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                warehouse_bpanelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                warehouse_bpanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                warehouse_bpanelMouseExited(evt);
            }
        });
        warehouse_bpanel.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                warehouse_bpanelPropertyChange(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Warehouse");

        javax.swing.GroupLayout warehouse_bpanelLayout = new javax.swing.GroupLayout(warehouse_bpanel);
        warehouse_bpanel.setLayout(warehouse_bpanelLayout);
        warehouse_bpanelLayout.setHorizontalGroup(
            warehouse_bpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, warehouse_bpanelLayout.createSequentialGroup()
                .addContainerGap(102, Short.MAX_VALUE)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        warehouse_bpanelLayout.setVerticalGroup(
            warehouse_bpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(warehouse_bpanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(warehouse_bpanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 250, 250, -1));

        txt_adminWelcome.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        txt_adminWelcome.setForeground(new java.awt.Color(255, 255, 255));
        txt_adminWelcome.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(txt_adminWelcome, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 200, 40));

        logut_admin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/warehouse/resourses/logout.png"))); // NOI18N
        logut_admin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                logut_adminMousePressed(evt);
            }
        });
        jPanel1.add(logut_admin, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 420, 40, 50));

        getContentPane().add(jPanel1);
        jPanel1.setBounds(0, 30, 250, 510);

        layeredPanel_Warehouse.setBackground(new java.awt.Color(255, 153, 153));
        layeredPanel_Warehouse.setOpaque(true);
        layeredPanel_Warehouse.setLayout(new java.awt.CardLayout());

        dashboard_panel.setBackground(new java.awt.Color(204, 204, 204));
        dashboard_panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_time.setBackground(new java.awt.Color(255, 102, 102));
        lbl_time.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lbl_time.setForeground(new java.awt.Color(255, 255, 255));
        lbl_time.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        dashboard_panel.add(lbl_time, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 50, 140, 33));

        total_earning_panel.setBackground(new java.awt.Color(46, 213, 115));
        total_earning_panel.setPreferredSize(new java.awt.Dimension(340, 120));
        total_earning_panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                total_earning_panelMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                total_earning_panelMousePressed(evt);
            }
        });
        total_earning_panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Total Earning");
        total_earning_panel.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, -1, -1));

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        total_earning_panel.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 44, 210, 26));

        lbl_totalBookings.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lbl_totalBookings.setForeground(new java.awt.Color(255, 255, 255));
        total_earning_panel.add(lbl_totalBookings, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 76, 210, 26));

        jLabel46.setIcon(new javax.swing.ImageIcon(getClass().getResource("/warehouse/resourses/coins (1).png"))); // NOI18N
        total_earning_panel.add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 0, 100, -1));

        dashboard_panel.add(total_earning_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, -1, -1));

        m_popular_aircraft_panel.setBackground(new java.awt.Color(83, 82, 237));
        m_popular_aircraft_panel.setPreferredSize(new java.awt.Dimension(340, 120));
        m_popular_aircraft_panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                m_popular_aircraft_panelMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                m_popular_aircraft_panelMousePressed(evt);
            }
        });
        m_popular_aircraft_panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Most Popular Airline");
        m_popular_aircraft_panel.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, -1, -1));

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        m_popular_aircraft_panel.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 44, 162, 26));

        jLabel21.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        m_popular_aircraft_panel.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 76, 162, 26));

        jLabel45.setIcon(new javax.swing.ImageIcon(getClass().getResource("/warehouse/resourses/airplane (1).png"))); // NOI18N
        m_popular_aircraft_panel.add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 0, -1, 130));

        dashboard_panel.add(m_popular_aircraft_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 140, -1, -1));

        lbl_date.setBackground(new java.awt.Color(204, 255, 102));
        lbl_date.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lbl_date.setForeground(new java.awt.Color(255, 255, 255));
        lbl_date.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        dashboard_panel.add(lbl_date, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 10, 230, 35));

        b_booking_agent_panel.setBackground(new java.awt.Color(255, 127, 80));
        b_booking_agent_panel.setPreferredSize(new java.awt.Dimension(350, 120));
        b_booking_agent_panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                b_booking_agent_panelMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                b_booking_agent_panelMousePressed(evt);
            }
        });
        b_booking_agent_panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Best Booking Agent");
        b_booking_agent_panel.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, -1, -1));

        lbl_totalContributionOfBestAgent.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lbl_totalContributionOfBestAgent.setForeground(new java.awt.Color(255, 255, 255));
        b_booking_agent_panel.add(lbl_totalContributionOfBestAgent, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 77, 156, 27));

        lbl_bestBookingAgentName.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lbl_bestBookingAgentName.setForeground(new java.awt.Color(255, 255, 255));
        b_booking_agent_panel.add(lbl_bestBookingAgentName, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 44, 156, 27));

        jLabel47.setIcon(new javax.swing.ImageIcon(getClass().getResource("/warehouse/resourses/seller (1).png"))); // NOI18N
        b_booking_agent_panel.add(jLabel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 0, -1, -1));

        dashboard_panel.add(b_booking_agent_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 140, -1, -1));

        panel_pieChart.setBackground(new java.awt.Color(255, 255, 255));
        panel_pieChart.setOpaque(false);
        panel_pieChart.setLayout(new java.awt.BorderLayout());
        dashboard_panel.add(panel_pieChart, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 270, 340, 240));

        panel_barChart.setBackground(new java.awt.Color(255, 255, 255));
        panel_barChart.setOpaque(false);
        panel_barChart.setLayout(new java.awt.BorderLayout());
        dashboard_panel.add(panel_barChart, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 270, 350, 240));

        layeredPanel_Warehouse.add(dashboard_panel, "card2");

        admin_panel.setBackground(new java.awt.Color(255, 255, 255));
        admin_panel.setLayout(null);

        jPanel11.setBackground(new java.awt.Color(48, 51, 107));
        jPanel11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel11MousePressed(evt);
            }
        });

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/warehouse/resourses/travel.png"))); // NOI18N

        jLabel10.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Airport");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel3)
                .addGap(26, 26, 26)
                .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(jLabel10)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        admin_panel.add(jPanel11);
        jPanel11.setBounds(30, 170, 200, 80);

        jPanel17.setBackground(new java.awt.Color(48, 51, 107));
        jPanel17.setPreferredSize(new java.awt.Dimension(200, 86));
        jPanel17.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel17MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel17MousePressed(evt);
            }
        });

        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/warehouse/resourses/airplaneAP.png"))); // NOI18N

        jLabel22.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("Aircraft");

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel18)
                .addGap(18, 18, 18)
                .addComponent(jLabel22)
                .addContainerGap(53, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel18))
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(jLabel22)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        admin_panel.add(jPanel17);
        jPanel17.setBounds(250, 30, 200, 86);

        jPanel19.setBackground(new java.awt.Color(0, 51, 51));
        jPanel19.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel19MousePressed(evt);
            }
        });

        jLabel25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/warehouse/resourses/turkish-airlines.png"))); // NOI18N

        jLabel26.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(255, 255, 255));
        jLabel26.setText("Airline");

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel25)
                .addGap(30, 30, 30)
                .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel25)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel26)
                .addGap(33, 33, 33))
        );

        admin_panel.add(jPanel19);
        jPanel19.setBounds(250, 170, 200, 80);

        jPanel21.setBackground(new java.awt.Color(255, 71, 87));
        jPanel21.setMinimumSize(new java.awt.Dimension(200, 86));
        jPanel21.setPreferredSize(new java.awt.Dimension(200, 86));
        jPanel21.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel21MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel21MousePressed(evt);
            }
        });

        jLabel27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/warehouse/resourses/payment.png"))); // NOI18N

        jLabel28.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 255, 255));
        jLabel28.setText("Payment Status");

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel27)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel28)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel27))
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(jLabel28)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        admin_panel.add(jPanel21);
        jPanel21.setBounds(30, 370, 200, 80);

        jPanel22.setBackground(new java.awt.Color(48, 51, 107));
        jPanel22.setPreferredSize(new java.awt.Dimension(200, 86));
        jPanel22.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel22MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel22MousePressed(evt);
            }
        });

        jLabel29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/warehouse/resourses/calendarAP.png"))); // NOI18N

        jLabel30.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(255, 255, 255));
        jLabel30.setText("Flight Schedule");

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel29)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel30)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel29))
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(jLabel30)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        admin_panel.add(jPanel22);
        jPanel22.setBounds(30, 30, 200, 86);

        jPanel20.setBackground(new java.awt.Color(255, 0, 153));
        jPanel20.setPreferredSize(new java.awt.Dimension(200, 76));
        jPanel20.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel20MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel20MousePressed(evt);
            }
        });

        jLabel31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/warehouse/resourses/plane-tickets.png"))); // NOI18N

        jLabel32.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(255, 255, 255));
        jLabel32.setText("Ticket Types");

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel31)
                .addGap(18, 18, 18)
                .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel31)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel20Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel32)
                .addGap(31, 31, 31))
        );

        admin_panel.add(jPanel20);
        jPanel20.setBounds(30, 270, 200, 80);

        jPanel23.setBackground(new java.awt.Color(255, 71, 87));
        jPanel23.setMinimumSize(new java.awt.Dimension(200, 76));
        jPanel23.setPreferredSize(new java.awt.Dimension(200, 76));
        jPanel23.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel23MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel23MousePressed(evt);
            }
        });

        jLabel33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/warehouse/resourses/avatar.png"))); // NOI18N

        jLabel34.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(255, 255, 255));
        jLabel34.setText("Booking Agent");

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel33)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel34)
                .addContainerGap(13, Short.MAX_VALUE))
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel33)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel34)
                .addGap(32, 32, 32))
        );

        admin_panel.add(jPanel23);
        jPanel23.setBounds(480, 270, 200, 80);

        jPanel24.setBackground(new java.awt.Color(0, 204, 255));
        jPanel24.setMinimumSize(new java.awt.Dimension(200, 86));
        jPanel24.setPreferredSize(new java.awt.Dimension(200, 76));
        jPanel24.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel24MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel24MousePressed(evt);
            }
        });

        jLabel35.setIcon(new javax.swing.ImageIcon(getClass().getResource("/warehouse/resourses/seatap.png"))); // NOI18N

        jLabel36.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(255, 255, 255));
        jLabel36.setText("Travel Class");

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel35)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel36)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(14, 14, 14))
        );

        admin_panel.add(jPanel24);
        jPanel24.setBounds(250, 270, 200, 80);

        jPanel18.setBackground(new java.awt.Color(153, 255, 153));
        jPanel18.setPreferredSize(new java.awt.Dimension(200, 76));
        jPanel18.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel18MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel18MousePressed(evt);
            }
        });

        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/warehouse/resourses/airplaneAP.png"))); // NOI18N

        jLabel23.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setText("Airline-Aircraft");
        jLabel23.setToolTipText("");

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel23)
                .addContainerGap(11, Short.MAX_VALUE))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel19))
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jLabel23)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        admin_panel.add(jPanel18);
        jPanel18.setBounds(480, 170, 200, 80);

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel9.setText("Frequently Used");
        jLabel9.setToolTipText("Frequently Used");
        admin_panel.add(jLabel9);
        jLabel9.setBounds(30, 10, 110, 17);

        layeredPanel_Warehouse.add(admin_panel, "card3");

        getContentPane().add(layeredPanel_Warehouse);
        layeredPanel_Warehouse.setBounds(250, 30, 720, 510);

        setSize(new java.awt.Dimension(969, 540));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void manage_data_bpannelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_manage_data_bpannelMouseEntered
        manage_data_bpannel.setBackground(color1);
    }//GEN-LAST:event_manage_data_bpannelMouseEntered

    private void manage_data_bpannelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_manage_data_bpannelMouseExited
        manage_data_bpannel.setBackground(color2);

    }//GEN-LAST:event_manage_data_bpannelMouseExited

    private void jLabel6MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MousePressed
        xMouse = evt.getX();
        yMouse = evt.getY();
    }//GEN-LAST:event_jLabel6MousePressed

    private void jLabel6MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseDragged
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation(x - xMouse, y - yMouse);
    }//GEN-LAST:event_jLabel6MouseDragged

    private void dashboard_bpanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dashboard_bpanelMouseEntered
        dashboard_bpanel.setBackground(color1);
    }//GEN-LAST:event_dashboard_bpanelMouseEntered

    private void dashboard_bpanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dashboard_bpanelMouseExited
        dashboard_bpanel.setBackground(color2);
    }//GEN-LAST:event_dashboard_bpanelMouseExited

    private void warehouse_bpanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_warehouse_bpanelMouseClicked
        new Warehouse().setVisible(true);
    }//GEN-LAST:event_warehouse_bpanelMouseClicked

    private void warehouse_bpanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_warehouse_bpanelMouseEntered
        warehouse_bpanel.setBackground(color1);
    }//GEN-LAST:event_warehouse_bpanelMouseEntered

    private void warehouse_bpanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_warehouse_bpanelMouseExited
        warehouse_bpanel.setBackground(color2);
    }//GEN-LAST:event_warehouse_bpanelMouseExited

    private void warehouse_bpanelPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_warehouse_bpanelPropertyChange

    }//GEN-LAST:event_warehouse_bpanelPropertyChange

    private void b_booking_agent_panelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_b_booking_agent_panelMouseClicked


    }//GEN-LAST:event_b_booking_agent_panelMouseClicked

    private void total_earning_panelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_total_earning_panelMouseClicked


    }//GEN-LAST:event_total_earning_panelMouseClicked

    private void m_popular_aircraft_panelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_m_popular_aircraft_panelMouseClicked

    }//GEN-LAST:event_m_popular_aircraft_panelMouseClicked

    private void jPanel17MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel17MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel17MouseClicked

    private void jPanel21MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel21MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel21MouseClicked

    private void jPanel22MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel22MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel22MouseClicked

    private void jPanel20MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel20MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel20MouseClicked

    private void jPanel23MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel23MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel23MouseClicked

    private void jPanel24MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel24MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel24MouseClicked

    private void manage_data_bpannelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_manage_data_bpannelMousePressed
        SwitchPanel(admin_panel);

    }//GEN-LAST:event_manage_data_bpannelMousePressed

    private void jPanel19MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel19MousePressed
        new AirlinesManagement().setVisible(true);        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel19MousePressed

    private void dashboard_bpanelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dashboard_bpanelMousePressed
        SwitchPanel(dashboard_panel);
    }//GEN-LAST:event_dashboard_bpanelMousePressed

    private void jPanel11MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel11MousePressed
        new AirportsManagement().setVisible(true);
    }//GEN-LAST:event_jPanel11MousePressed

    private void jPanel20MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel20MousePressed
        new TicketTypesManagement().setVisible(true);
    }//GEN-LAST:event_jPanel20MousePressed

    private void jPanel24MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel24MousePressed
        new TravelClassesManagement().setVisible(true);
    }//GEN-LAST:event_jPanel24MousePressed

    private void total_earning_panelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_total_earning_panelMousePressed
        DashboardStartingCharts();
    }//GEN-LAST:event_total_earning_panelMousePressed

    private void b_booking_agent_panelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_b_booking_agent_panelMousePressed
        sql = "SELECT agent_name,\n" +
"COUNT(agent_name) AS booking_processed,\n" +
"sum(cost) as amount\n" +
"FROM Booking\n" +
"INNER JOIN booking_agents ON Booking.bookingagent = booking_agents.agent_id\n" +
"inner join billing on billing.booking_status_code=Booking.booking_status_code\n" +
"GROUP BY agent_name";
        String resultset_get_sum = "amount";
        String resultset_get = "agent_name";
        String vertical = "BDT";
        String bar_title = "Contribution";
        String horizontal = "Agent";
        String index = "taka";
        showBar(sql, resultset_get_sum, resultset_get, bar_title, horizontal, vertical, index);
        String count_agent = "booking_processed";
        String title_pie = "Bookings";
        showPie(sql, count_agent, resultset_get, title_pie);
    }//GEN-LAST:event_b_booking_agent_panelMousePressed

    private void m_popular_aircraft_panelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_m_popular_aircraft_panelMousePressed
        String s = "SELECT top 1 sum(cost) as earned,airline_name,count(airline_name) as c_airline_name\n"
                    + "FROM booking \n"
                    + "inner join flights on Booking.flight_no=flights.flight_no\n"
                    + "iNNER JOIN airline_aircraft on flights.airline_aircraft_code=airline_aircraft.airline_aircraft_code\n"
                    + "inner join airline on airline_aircraft.airline_code=airline.airline_code\n"
                    + "inner join billing on billing.booking_status_code=Booking.booking_status_code\n"
                    + "group by airline_name";
        String ib = "earned";
        String anb = "airline_name";
        String tb = "BDT";
        String cb = "Contribution";
        String ab = "Airline";
        String db = "taka";
        showBar(s, ib, anb, cb, ab, tb, db);

        String ip = "c_airline_name";
        String anp = "airline_name";
        String cp = "Bookings";
        showPie(s, ip, anp, cp);
    }//GEN-LAST:event_m_popular_aircraft_panelMousePressed

    private void jPanel23MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel23MousePressed
        new BookingAgentsManagement().setVisible(true);
    }//GEN-LAST:event_jPanel23MousePressed

    private void jPanel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel4MouseClicked

    }//GEN-LAST:event_jPanel4MouseClicked

    private void jPanel4MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel4MousePressed
        System.exit(0);
    }//GEN-LAST:event_jPanel4MousePressed

    private void jPanel22MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel22MousePressed
        new FlightScheduleManagement().setVisible(true);
    }//GEN-LAST:event_jPanel22MousePressed

    private void jPanel17MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel17MousePressed
        new AircraftTypesManagement().setVisible(true);
    }//GEN-LAST:event_jPanel17MousePressed

    private void jPanel21MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel21MousePressed
        new PaymentStatusManagement().setVisible(true);        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel21MousePressed

    private void jPanel4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel4MouseEntered
        jPanel4.setBackground(color6);
    }//GEN-LAST:event_jPanel4MouseEntered

    private void jPanel4MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel4MouseExited
        jPanel4.setBackground(color7);
    }//GEN-LAST:event_jPanel4MouseExited


    private void logut_adminMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logut_adminMousePressed
        this.dispose();
        LoginsAndCustomerBookings l = new LoginsAndCustomerBookings();
        l.SwitchPanel(LoginsAndCustomerBookings.panel_admin_login, "Admin Login");
        l.setVisible(true);
    }//GEN-LAST:event_logut_adminMousePressed

    private void jPanel18MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel18MouseClicked

    }//GEN-LAST:event_jPanel18MouseClicked

    private void jPanel18MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel18MousePressed
        new AirlineAircraftManagement().setVisible(true);
    }//GEN-LAST:event_jPanel18MousePressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel admin_panel;
    private javax.swing.JPanel b_booking_agent_panel;
    private javax.swing.JPanel dashboard_bpanel;
    private javax.swing.JPanel dashboard_panel;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JLayeredPane layeredPanel_Warehouse;
    private javax.swing.JLabel lbl_bestBookingAgentName;
    private javax.swing.JLabel lbl_date;
    private javax.swing.JLabel lbl_time;
    private javax.swing.JLabel lbl_totalBookings;
    private javax.swing.JLabel lbl_totalContributionOfBestAgent;
    private javax.swing.JLabel logut_admin;
    private javax.swing.JPanel m_popular_aircraft_panel;
    private javax.swing.JPanel manage_data_bpannel;
    private javax.swing.JPanel panel_barChart;
    private javax.swing.JPanel panel_pieChart;
    private javax.swing.JPanel total_earning_panel;
    private javax.swing.JLabel txt_adminWelcome;
    private javax.swing.JPanel warehouse_bpanel;
    // End of variables declaration//GEN-END:variables
}
