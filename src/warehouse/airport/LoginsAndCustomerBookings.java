package warehouse.airport;

import java.awt.Color;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 *
 * @author yeami
 */
public final class LoginsAndCustomerBookings extends javax.swing.JFrame {

    Date date = Calendar.getInstance().getTime();

    Connection con;
    String sql;
    PreparedStatement stm;
    ResultSet rs;
    static String adminID;
    private String CustomerID;
    DateFormat f1 = new SimpleDateFormat("yyyy-MM-dd");
    int xMouse, yMouse;
    int reservation_status_code;
    String bookingAgent;
    DefaultTableModel flightSearchTable, CustomerBookingTable, unconfirmedBookingTable;

    public LoginsAndCustomerBookings() {
        initComponents();
        this.setLocationRelativeTo(null);
        comboAirport();
        ShowUnconfirmedBookings();
        combo_flight_number();
        combo_travel_class();
        combo_ticket_type();
        SwitchPanel(panel_admin_login, "Login As Admin");
       
        CustomerBookingTable = (DefaultTableModel) table_CustomerBookings.getModel();
        flightSearchTable = (DefaultTableModel) table_searched_flights.getModel();
        
        JTableHeader theader = table_searched_flights.getTableHeader();
        theader.setBackground(new Home().color3);
        theader.setForeground(Color.white);
    }

    public void ShowUnconfirmedBookings() {
        
        try {
            con = ConnectSQL.connect();
            sql = "select billing_id, billing.booking_status_code,bookingdate,flights.flight_no,customer_id from Booking\n" +
"                  inner join Flights on Flights.flight_no=Booking.flight_no\n" +
"                 inner join Billing on Billing.Booking_Status_code=Booking.Booking_Status_code where bookingagent is null";
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();

            while (rs.next()) {
        
                unconfirmedBookingTable = (DefaultTableModel) unconfirmed_bookings_table.getModel();
                Object[] row = new Object[7];
                row[0] = rs.getInt("booking_status_code");
                row[1] = rs.getDate("bookingdate");
                row[2] = rs.getString("flight_no");
                row[3] = rs.getString("customer_id");
                row[4] = rs.getInt("billing_id");
                unconfirmedBookingTable.addRow(row);
              

            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void loginDashboard() {

        try {
            con = ConnectSQL.connect();
            sql = "select * from admin where admin_id='" + jTextField1.getText() + "'and admin_pass='" + String.valueOf(jPasswordField1.getPassword()) + "'";
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            if (rs.next()) {
                LoginsAndCustomerBookings.adminID = (rs.getString("admin_id"));
                new Home().setVisible(true);
                this.dispose();

            } else {
                JOptionPane.showMessageDialog(null, "Incorrect Admin ID or Password", "ERROR", JOptionPane.WARNING_MESSAGE);
                jTextField1.setText(null);
                jPasswordField1.setText(null);
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    private void comboAirport() {
        //done
        try {
            con = ConnectSQL.connect();
            sql = "Select airport_name from airport";
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                combobox_origin_airport.addItem(rs.getString("airport_name"));
                combobox_destination_airport.addItem(rs.getString("airport_name"));
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private String OriginAirportCode() {
        try {
            con = ConnectSQL.connect();
            sql = "Select airport_code from airport where airport_name='" + combobox_origin_airport.getSelectedItem() + "'";
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                return rs.getString("airport_code");
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    private String DestinationAirportCode() {
        try {
            con = ConnectSQL.connect();
            sql = "Select airport_code from airport where airport_name='" + combobox_destination_airport.getSelectedItem() + "'";
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                String des_airport = rs.getString("airport_code");
                return des_airport;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    private void combo_flight_number() {
        try {
            con = ConnectSQL.connect();
            sql = "Select flight_no from flights where origin_airport_code='" + OriginAirportCode() + "' and destination_airport_code='" + DestinationAirportCode() + "'";
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                combobox_flight_no.addItem(rs.getString("flight_number"));
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void combo_ticket_type() {
        try {
            con = ConnectSQL.connect();
            sql = "Select ticket_type_code from ticket_type";
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                combobox_ticket_type.addItem(rs.getString("ticket_type_code"));
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void combo_travel_class() {
        try {
            con = ConnectSQL.connect();
            sql = "Select travel_class_code from travel_class";
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                combobox_travel_class.addItem(rs.getString("travel_class_code"));
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void refreshTable(DefaultTableModel m) {
        while (m.getRowCount() > 0) {
            m.setRowCount(0);
        }
    }

    private float base_price() {
        try {
            con = ConnectSQL.connect();
            sql = "select base_price from flights where flight_no='" + combobox_flight_no.getSelectedItem() + "'";
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                float bp = rs.getFloat("base_price");
                return bp;

            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return 0;

    }

    private float price_ticket() {
        try {
            con = ConnectSQL.connect();
            sql = "select price from ticket_type where ticket_type_code='" + combobox_ticket_type.getSelectedItem() + "'";
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                float mb = rs.getFloat("price");
                return mb;

            }

        } catch (Throwable e) {
            e.printStackTrace();
        }
        return 0;

    }

    private float ticket_varies() {
        try {
            con = ConnectSQL.connect();
            sql = "select price from travel_class where travel_class_code='" + combobox_travel_class.getSelectedItem() + "'";
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {
                float vp = rs.getFloat("price");
                return vp;

            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return 0;

    }

    private float total_cost() {
        float t = (base_price() * price_ticket()) + (base_price() * (ticket_varies() / 100));
        float total = (float) (Math.round(Float.valueOf(t) * 100.0) / 100.0);
        return total;
    }

    private String[] flight_values() {
        try {
            con = ConnectSQL.connect();
            sql = "Select * from flights where flight_no='" + combobox_flight_no.getSelectedItem() + "'";
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();

            while (rs.next()) {
                String fs[] = new String[4];
//                fs[0] = rs.getString("origin_airport_code");
//                fs[1] = rs.getString("airline_code");
//                fs[2] = rs.getString("aircraft_code");
                fs[3] = rs.getString("flight_no");
                return fs;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    private int last_booking_code() {

        con = ConnectSQL.connect();
        sql = "Insert into ref_booking_status(is_booked)values('yes')";
        try {
            stm = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stm.execute();
            rs = stm.getGeneratedKeys();
            while (rs.next()) {
                int i = rs.getInt(1);
                return i;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return 0;

    }

    private int last_reserved_code() {

        con = ConnectSQL.connect();
        sql = "Insert into reservation_status(is_reserved) values(NULL)";
        try {
            stm = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stm.execute();
            rs = stm.getGeneratedKeys();
            while (rs.next()) {
                int i = rs.getInt(1);
                return i;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int last_payment_code() {

        con = ConnectSQL.connect();
        sql = "Insert into payment_status(is_paid)values('no')";
        try {
            stm = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stm.execute();
            rs = stm.getGeneratedKeys();
            while (rs.next()) {
                int i = rs.getInt(1);
                return i;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void Tips() {
        Random r = new Random();
        int x = r.nextInt(3);
        switch (x) {
            case 0:

                lbl_tips.setText("Remember to update 'working day' everyday");
                break;
            case 1:

                lbl_tips.setText("Datas with Agent 'NONE's are yet to be processed");
                break;
            case 2:
                lbl_tips.setText("Admins can not manage themselves");
                break;
            default:
                break;
        }
    }

    private void CustomeBookingsAll() {
        try {
            con = ConnectSQL.connect();
            sql = "select booking.booking_status_code as booking_status_code,\n"
                    + "billing.cost as cost,\n"
                    + "payment.billing_id as billing_id, \n"
                    + "booking.flight_no as flight_no,\n"
                    + "booking.is_booked as is_booked,\n"
                    + "Flights.departure_date_time as departure_date_time,\n"
                    + "Flights.arrival_date_time as arrival_date_time,\n"
                    + "sum(payment.amount) as paid from booking\n"
                    + "inner join billing on Booking.booking_status_code=billing.booking_status_code\n"
                    + "inner join Flights on Booking.flight_no=Flights.flight_no \n"
                    + "left join payment on billing.billing_id=payment.billing_id \n"
                    + "where booking.customer_id='" + this.CustomerID + "'\n"
                    + "and is_booked='yes' \n"
                    + "group by payment.billing_id, booking.booking_status_code,billing.cost,booking.flight_no,booking.is_booked,flights.departure_date_time,Flights.arrival_date_time";
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {

                Object[] row = new Object[8];
                row[7] = rs.getInt("paid");
                row[1] = rs.getString("is_booked");
                row[2] = rs.getString("flight_no");
                row[6] = rs.getInt("billing_id");
                row[3] = rs.getTime("departure_date_time");
                row[4] = rs.getTime("arrival_date_time");
                row[5] = rs.getFloat("cost");
                row[0] = rs.getInt("booking_status_code");

                CustomerBookingTable.addRow(row);
            }
        } catch (Exception e) {
            System.out.println(e);

        }
    }

    void SwitchPanel(JPanel jp1, String jl1) {
        Tips();
        jLayeredPane1.removeAll();
        jLabel3.setText(jl1);
        jp1.repaint();
        jp1.validate();
        jLayeredPane1.add(jp1);
        jLayeredPane1.repaint();
        jLayeredPane1.revalidate();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel_titleBar = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        panel_customer_booking = new javax.swing.JPanel();
        kGradientPanel1 = new keeptoo.KGradientPanel();
        combobox_origin_airport = new javax.swing.JComboBox<>();
        combobox_destination_airport = new javax.swing.JComboBox<>();
        buttonc_FindFlights = new javax.swing.JButton();
        combobox_flight_no = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        table_searched_flights = new javax.swing.JTable();
        datechooser_desired_date = new com.toedter.calendar.JDateChooser();
        combobox_ticket_type = new javax.swing.JComboBox<>();
        combobox_travel_class = new javax.swing.JComboBox<>();
        buttonc_ShowCost = new javax.swing.JButton();
        buttonc_Book = new javax.swing.JButton();
        lbl_show_basePrice = new javax.swing.JLabel();
        lbl_base_price = new javax.swing.JLabel();
        lbl_ticket_type_multiplies = new javax.swing.JLabel();
        lbl_Show_ticketTypeMultiplies = new javax.swing.JLabel();
        lbl_show_travel_class_difference = new javax.swing.JLabel();
        lbl_travel_class_difference = new javax.swing.JLabel();
        lbl_show_total_cost = new javax.swing.JLabel();
        lbl_total_cost = new javax.swing.JLabel();
        lbl_desired_date = new javax.swing.JLabel();
        lbl_origin_airport = new javax.swing.JLabel();
        lbl_destination_airport = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        lbl_show_previous_bookings = new javax.swing.JButton();
        panel_admin_login = new javax.swing.JPanel();
        kGradientPanel2 = new keeptoo.KGradientPanel();
        jTextField1 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPasswordField1 = new javax.swing.JPasswordField();
        jLabel2 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        lbl_tips = new javax.swing.JLabel();
        panel_customer_signUp = new javax.swing.JPanel();
        kGradientPanel3 = new keeptoo.KGradientPanel();
        lvlC_Name = new javax.swing.JLabel();
        lvlC_Email = new javax.swing.JLabel();
        txtC_Name = new javax.swing.JTextField();
        lvlC_Pass = new javax.swing.JLabel();
        txtC_Email = new javax.swing.JTextField();
        passC_Pass = new javax.swing.JPasswordField();
        lvlC_Address = new javax.swing.JLabel();
        txtC_Address = new javax.swing.JTextField();
        lvlC_phone = new javax.swing.JLabel();
        txtC_Phone = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        btnC_SignUp = new javax.swing.JButton();
        jLabel31 = new javax.swing.JLabel();
        panel_customer_signIn = new javax.swing.JPanel();
        kGradientPanel5 = new keeptoo.KGradientPanel();
        lvlC_Email_signIn = new javax.swing.JLabel();
        lvlC_Pass_signIN = new javax.swing.JLabel();
        txtC_Email_signIn = new javax.swing.JTextField();
        passC_Pass_signIn = new javax.swing.JPasswordField();
        btnC_SignIn = new javax.swing.JButton();
        panel_customer_check = new javax.swing.JPanel();
        kGradientPanel6 = new keeptoo.KGradientPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        table_CustomerBookings = new javax.swing.JTable();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();
        txt_paymentID = new javax.swing.JTextField();
        txt_paymentMethod = new javax.swing.JTextField();
        panel_booking_agent_login = new javax.swing.JPanel();
        txtAgent_Id = new javax.swing.JTextField();
        passAgent_Pass = new javax.swing.JPasswordField();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        panel_booking_process = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        unconfirmed_bookings_table = new javax.swing.JTable();
        jLabel40 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        AgentsBookingID = new javax.swing.JTextField();
        btn_confirmBookingAgent = new javax.swing.JButton();
        panel_close = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel44 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel45 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(null);

        panel_titleBar.setBackground(new java.awt.Color(51, 51, 51));
        panel_titleBar.setForeground(new java.awt.Color(102, 102, 102));
        panel_titleBar.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                panel_titleBarMouseDragged(evt);
            }
        });
        panel_titleBar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                panel_titleBarMousePressed(evt);
            }
        });

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Customer Booking");

        javax.swing.GroupLayout panel_titleBarLayout = new javax.swing.GroupLayout(panel_titleBar);
        panel_titleBar.setLayout(panel_titleBarLayout);
        panel_titleBarLayout.setHorizontalGroup(
            panel_titleBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_titleBarLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(828, Short.MAX_VALUE))
        );
        panel_titleBarLayout.setVerticalGroup(
            panel_titleBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );

        getContentPane().add(panel_titleBar);
        panel_titleBar.setBounds(0, 0, 990, 30);

        jLayeredPane1.setPreferredSize(new java.awt.Dimension(830, 680));
        jLayeredPane1.setLayout(new java.awt.CardLayout());

        panel_customer_booking.setPreferredSize(new java.awt.Dimension(830, 680));

        kGradientPanel1.setkEndColor(new java.awt.Color(102, 102, 255));
        kGradientPanel1.setkStartColor(new java.awt.Color(255, 51, 51));
        kGradientPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        combobox_origin_airport.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "" }));
        combobox_origin_airport.setBorder(null);
        kGradientPanel1.add(combobox_origin_airport, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 30, 235, 28));

        combobox_destination_airport.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "" }));
        combobox_destination_airport.setBorder(null);
        kGradientPanel1.add(combobox_destination_airport, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 70, 235, 28));

        buttonc_FindFlights.setBackground(new java.awt.Color(45, 52, 54));
        buttonc_FindFlights.setForeground(new java.awt.Color(255, 255, 255));
        buttonc_FindFlights.setText("Find Flights");
        buttonc_FindFlights.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(249, 127, 81)));
        buttonc_FindFlights.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonc_FindFlightsActionPerformed(evt);
            }
        });
        kGradientPanel1.add(buttonc_FindFlights, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 160, 102, 30));

        combobox_flight_no.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "" }));
        combobox_flight_no.setBorder(null);
        combobox_flight_no.setEnabled(false);
        combobox_flight_no.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combobox_flight_noActionPerformed(evt);
            }
        });
        kGradientPanel1.add(combobox_flight_no, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 350, 235, 28));

        table_searched_flights.setBackground(new java.awt.Color(245, 59, 87));
        table_searched_flights.setForeground(new java.awt.Color(255, 255, 255));
        table_searched_flights.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Flight No", "Airline Name", "Departure Time", "Arrival Time"
            }
        ));
        jScrollPane1.setViewportView(table_searched_flights);

        kGradientPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 0, 410, 200));
        kGradientPanel1.add(datechooser_desired_date, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 110, 235, 28));

        combobox_ticket_type.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "" }));
        combobox_ticket_type.setBorder(null);
        combobox_ticket_type.setEnabled(false);
        kGradientPanel1.add(combobox_ticket_type, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 400, 235, 28));

        combobox_travel_class.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "" }));
        combobox_travel_class.setBorder(null);
        combobox_travel_class.setEnabled(false);
        kGradientPanel1.add(combobox_travel_class, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 450, 235, 31));

        buttonc_ShowCost.setBackground(new java.awt.Color(45, 52, 54));
        buttonc_ShowCost.setForeground(new java.awt.Color(255, 255, 255));
        buttonc_ShowCost.setText("Show Cost");
        buttonc_ShowCost.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(249, 127, 81)));
        buttonc_ShowCost.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonc_ShowCostActionPerformed(evt);
            }
        });
        kGradientPanel1.add(buttonc_ShowCost, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 510, 104, 30));

        buttonc_Book.setBackground(new java.awt.Color(45, 52, 54));
        buttonc_Book.setForeground(new java.awt.Color(255, 255, 255));
        buttonc_Book.setText("Book");
        buttonc_Book.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(249, 127, 81)));
        buttonc_Book.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonc_BookActionPerformed(evt);
            }
        });
        kGradientPanel1.add(buttonc_Book, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 510, 108, 30));

        lbl_show_basePrice.setBackground(new java.awt.Color(255, 255, 255));
        lbl_show_basePrice.setFont(new java.awt.Font("Yu Gothic UI", 1, 13)); // NOI18N
        lbl_show_basePrice.setForeground(new java.awt.Color(255, 255, 255));
        lbl_show_basePrice.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        kGradientPanel1.add(lbl_show_basePrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 360, 123, 30));

        lbl_base_price.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N
        lbl_base_price.setForeground(new java.awt.Color(255, 255, 255));
        lbl_base_price.setText("Base Price                     :");
        kGradientPanel1.add(lbl_base_price, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 350, -1, 30));

        lbl_ticket_type_multiplies.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N
        lbl_ticket_type_multiplies.setForeground(new java.awt.Color(255, 255, 255));
        lbl_ticket_type_multiplies.setText("Ticket Type Multiplies   :");
        kGradientPanel1.add(lbl_ticket_type_multiplies, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 390, 156, 30));

        lbl_Show_ticketTypeMultiplies.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N
        lbl_Show_ticketTypeMultiplies.setForeground(new java.awt.Color(255, 255, 255));
        lbl_Show_ticketTypeMultiplies.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        kGradientPanel1.add(lbl_Show_ticketTypeMultiplies, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 400, 123, 30));

        lbl_show_travel_class_difference.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N
        lbl_show_travel_class_difference.setForeground(new java.awt.Color(255, 255, 255));
        lbl_show_travel_class_difference.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        kGradientPanel1.add(lbl_show_travel_class_difference, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 430, 123, 30));

        lbl_travel_class_difference.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N
        lbl_travel_class_difference.setForeground(new java.awt.Color(255, 255, 255));
        lbl_travel_class_difference.setText("Travel Class Difference  :");
        kGradientPanel1.add(lbl_travel_class_difference, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 430, 156, 30));

        lbl_show_total_cost.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N
        lbl_show_total_cost.setForeground(new java.awt.Color(255, 255, 255));
        lbl_show_total_cost.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        kGradientPanel1.add(lbl_show_total_cost, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 470, 123, 30));

        lbl_total_cost.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N
        lbl_total_cost.setForeground(new java.awt.Color(255, 255, 255));
        lbl_total_cost.setText("Total Cost                    :");
        kGradientPanel1.add(lbl_total_cost, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 460, 156, 30));

        lbl_desired_date.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N
        lbl_desired_date.setForeground(new java.awt.Color(255, 255, 255));
        lbl_desired_date.setText("Desired Date");
        kGradientPanel1.add(lbl_desired_date, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 119, 26));

        lbl_origin_airport.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N
        lbl_origin_airport.setForeground(new java.awt.Color(255, 255, 255));
        lbl_origin_airport.setText("Origin Airport");
        kGradientPanel1.add(lbl_origin_airport, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 119, 31));

        lbl_destination_airport.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N
        lbl_destination_airport.setForeground(new java.awt.Color(255, 255, 255));
        lbl_destination_airport.setText("Destination Airport");
        kGradientPanel1.add(lbl_destination_airport, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 130, 31));

        jLabel14.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Flight No.");
        kGradientPanel1.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 350, 119, 31));

        jLabel19.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Ticket Type");
        kGradientPanel1.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 400, 119, 31));

        jLabel20.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Travel Class");
        kGradientPanel1.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 450, 119, 30));

        jLabel21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/warehouse/resourses/airplaneLogin.png"))); // NOI18N
        kGradientPanel1.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 230, 570, 580));

        lbl_show_previous_bookings.setBackground(new java.awt.Color(255, 51, 51));
        lbl_show_previous_bookings.setText("Check Previous Bookings");
        lbl_show_previous_bookings.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lbl_show_previous_bookings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lbl_show_previous_bookingsActionPerformed(evt);
            }
        });
        kGradientPanel1.add(lbl_show_previous_bookings, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 520, 180, 40));

        javax.swing.GroupLayout panel_customer_bookingLayout = new javax.swing.GroupLayout(panel_customer_booking);
        panel_customer_booking.setLayout(panel_customer_bookingLayout);
        panel_customer_bookingLayout.setHorizontalGroup(
            panel_customer_bookingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panel_customer_bookingLayout.setVerticalGroup(
            panel_customer_bookingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_customer_bookingLayout.createSequentialGroup()
                .addComponent(kGradientPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 597, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 83, Short.MAX_VALUE))
        );

        jLayeredPane1.add(panel_customer_booking, "card4");

        panel_admin_login.setPreferredSize(new java.awt.Dimension(830, 680));

        kGradientPanel2.setBackground(new java.awt.Color(102, 153, 255));
        kGradientPanel2.setkEndColor(new java.awt.Color(0, 153, 153));
        kGradientPanel2.setkStartColor(new java.awt.Color(0, 153, 153));
        kGradientPanel2.setkTransparentControls(false);
        kGradientPanel2.setLayout(null);

        jTextField1.setForeground(new java.awt.Color(255, 255, 255));
        jTextField1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(255, 255, 255)));
        jTextField1.setCaretColor(new java.awt.Color(255, 255, 255));
        jTextField1.setOpaque(false);
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        kGradientPanel2.add(jTextField1);
        jTextField1.setBounds(500, 270, 210, 25);

        jLabel5.setFont(new java.awt.Font("Yu Gothic UI", 1, 13)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Admin ID");
        kGradientPanel2.add(jLabel5);
        jLabel5.setBounds(430, 270, 60, 30);

        jLabel7.setBackground(new java.awt.Color(0, 153, 255));
        jLabel7.setFont(new java.awt.Font("Yu Gothic UI", 1, 13)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Password");
        kGradientPanel2.add(jLabel7);
        jLabel7.setBounds(430, 320, 60, 30);

        jButton1.setBackground(new java.awt.Color(45, 52, 54));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Login");
        jButton1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        kGradientPanel2.add(jButton1);
        jButton1.setBounds(550, 400, 90, 30);

        jPasswordField1.setForeground(new java.awt.Color(255, 255, 255));
        jPasswordField1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(255, 255, 255)));
        jPasswordField1.setCaretColor(new java.awt.Color(255, 255, 255));
        jPasswordField1.setOpaque(false);
        kGradientPanel2.add(jPasswordField1);
        jPasswordField1.setBounds(500, 320, 210, 25);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/warehouse/resourses/support.png"))); // NOI18N
        kGradientPanel2.add(jLabel2);
        jLabel2.setBounds(-170, 60, 620, 690);

        jLabel22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/warehouse/resourses/apple.png"))); // NOI18N
        kGradientPanel2.add(jLabel22);
        jLabel22.setBounds(400, 600, 32, 40);

        lbl_tips.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lbl_tips.setForeground(new java.awt.Color(255, 255, 255));
        kGradientPanel2.add(lbl_tips);
        lbl_tips.setBounds(440, 600, 320, 40);

        javax.swing.GroupLayout panel_admin_loginLayout = new javax.swing.GroupLayout(panel_admin_login);
        panel_admin_login.setLayout(panel_admin_loginLayout);
        panel_admin_loginLayout.setHorizontalGroup(
            panel_admin_loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 830, Short.MAX_VALUE)
        );
        panel_admin_loginLayout.setVerticalGroup(
            panel_admin_loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 680, Short.MAX_VALUE)
        );

        jLayeredPane1.add(panel_admin_login, "card3");

        panel_customer_signUp.setPreferredSize(new java.awt.Dimension(830, 680));

        kGradientPanel3.setkEndColor(new java.awt.Color(255, 102, 102));
        kGradientPanel3.setkGradientFocus(1000);
        kGradientPanel3.setkStartColor(new java.awt.Color(102, 102, 255));

        lvlC_Name.setFont(new java.awt.Font("Yu Gothic UI", 1, 13)); // NOI18N
        lvlC_Name.setForeground(new java.awt.Color(255, 255, 255));
        lvlC_Name.setText("Name");

        lvlC_Email.setFont(new java.awt.Font("Yu Gothic UI", 1, 13)); // NOI18N
        lvlC_Email.setForeground(new java.awt.Color(255, 255, 255));
        lvlC_Email.setText("e-mail");

        txtC_Name.setForeground(new java.awt.Color(255, 255, 255));
        txtC_Name.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(255, 255, 255)));
        txtC_Name.setCaretColor(new java.awt.Color(255, 255, 255));
        txtC_Name.setOpaque(false);
        txtC_Name.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtC_NameActionPerformed(evt);
            }
        });

        lvlC_Pass.setFont(new java.awt.Font("Yu Gothic UI", 1, 13)); // NOI18N
        lvlC_Pass.setForeground(new java.awt.Color(255, 255, 255));
        lvlC_Pass.setText("Password");

        txtC_Email.setForeground(new java.awt.Color(255, 255, 255));
        txtC_Email.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(255, 255, 255)));
        txtC_Email.setCaretColor(new java.awt.Color(255, 255, 255));
        txtC_Email.setOpaque(false);

        passC_Pass.setForeground(new java.awt.Color(255, 255, 255));
        passC_Pass.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(255, 255, 255)));
        passC_Pass.setCaretColor(new java.awt.Color(255, 255, 255));
        passC_Pass.setOpaque(false);

        lvlC_Address.setFont(new java.awt.Font("Yu Gothic UI", 1, 13)); // NOI18N
        lvlC_Address.setForeground(new java.awt.Color(255, 255, 255));
        lvlC_Address.setText("Address");

        txtC_Address.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(255, 255, 255)));
        txtC_Address.setOpaque(false);

        lvlC_phone.setFont(new java.awt.Font("Yu Gothic", 1, 13)); // NOI18N
        lvlC_phone.setForeground(new java.awt.Color(255, 255, 255));
        lvlC_phone.setText("Phone");

        txtC_Phone.setForeground(new java.awt.Color(255, 255, 255));
        txtC_Phone.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(255, 255, 255)));
        txtC_Phone.setCaretColor(new java.awt.Color(255, 255, 255));
        txtC_Phone.setOpaque(false);

        jLabel30.setBackground(new java.awt.Color(0, 0, 0));
        jLabel30.setFont(new java.awt.Font("Yu Gothic UI", 1, 13)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(255, 255, 255));
        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel30.setText("Login");
        jLabel30.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(255, 255, 255)));
        jLabel30.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel30MousePressed(evt);
            }
        });

        btnC_SignUp.setBackground(new java.awt.Color(45, 52, 54));
        btnC_SignUp.setForeground(new java.awt.Color(255, 255, 255));
        btnC_SignUp.setText("SIgn Up");
        btnC_SignUp.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        btnC_SignUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnC_SignUpActionPerformed(evt);
            }
        });

        jLabel31.setFont(new java.awt.Font("Yu Gothic", 1, 13)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(255, 255, 255));
        jLabel31.setText("Already Have account?");

        javax.swing.GroupLayout kGradientPanel3Layout = new javax.swing.GroupLayout(kGradientPanel3);
        kGradientPanel3.setLayout(kGradientPanel3Layout);
        kGradientPanel3Layout.setHorizontalGroup(
            kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel3Layout.createSequentialGroup()
                .addGroup(kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(kGradientPanel3Layout.createSequentialGroup()
                        .addGap(189, 189, 189)
                        .addGroup(kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(kGradientPanel3Layout.createSequentialGroup()
                                .addComponent(lvlC_phone, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(64, 64, 64)
                                .addComponent(txtC_Phone))
                            .addComponent(lvlC_Address)
                            .addGroup(kGradientPanel3Layout.createSequentialGroup()
                                .addGroup(kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lvlC_Name, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lvlC_Email, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(kGradientPanel3Layout.createSequentialGroup()
                                        .addComponent(lvlC_Pass)
                                        .addGap(24, 24, 24)))
                                .addGap(42, 42, 42)
                                .addGroup(kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtC_Address, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(passC_Pass, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtC_Email, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtC_Name, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(kGradientPanel3Layout.createSequentialGroup()
                        .addGap(285, 285, 285)
                        .addGroup(kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnC_SignUp, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(251, Short.MAX_VALUE))
        );
        kGradientPanel3Layout.setVerticalGroup(
            kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel3Layout.createSequentialGroup()
                .addGap(154, 154, 154)
                .addGroup(kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(kGradientPanel3Layout.createSequentialGroup()
                        .addGroup(kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtC_Name, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lvlC_Name, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(5, 5, 5)
                        .addGroup(kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtC_Email, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lvlC_Email, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(passC_Pass, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lvlC_Pass))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtC_Address, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lvlC_Address, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtC_Phone, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lvlC_phone))
                .addGap(41, 41, 41)
                .addComponent(btnC_SignUp, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(57, 57, 57)
                .addGroup(kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30))
                .addContainerGap(170, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panel_customer_signUpLayout = new javax.swing.GroupLayout(panel_customer_signUp);
        panel_customer_signUp.setLayout(panel_customer_signUpLayout);
        panel_customer_signUpLayout.setHorizontalGroup(
            panel_customer_signUpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panel_customer_signUpLayout.setVerticalGroup(
            panel_customer_signUpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jLayeredPane1.add(panel_customer_signUp, "card4");

        panel_customer_signIn.setPreferredSize(new java.awt.Dimension(830, 680));

        lvlC_Email_signIn.setText("e-mail or username");

        lvlC_Pass_signIN.setText("Password");

        btnC_SignIn.setText("SIgn In");
        btnC_SignIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnC_SignInActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout kGradientPanel5Layout = new javax.swing.GroupLayout(kGradientPanel5);
        kGradientPanel5.setLayout(kGradientPanel5Layout);
        kGradientPanel5Layout.setHorizontalGroup(
            kGradientPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel5Layout.createSequentialGroup()
                .addGap(267, 267, 267)
                .addGroup(kGradientPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(kGradientPanel5Layout.createSequentialGroup()
                        .addGroup(kGradientPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lvlC_Pass_signIN)
                            .addComponent(lvlC_Email_signIn, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(kGradientPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(passC_Pass_signIn)
                            .addComponent(txtC_Email_signIn, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(kGradientPanel5Layout.createSequentialGroup()
                        .addGap(151, 151, 151)
                        .addComponent(btnC_SignIn)))
                .addContainerGap(296, Short.MAX_VALUE))
        );
        kGradientPanel5Layout.setVerticalGroup(
            kGradientPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel5Layout.createSequentialGroup()
                .addContainerGap(280, Short.MAX_VALUE)
                .addGroup(kGradientPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lvlC_Email_signIn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtC_Email_signIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(kGradientPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lvlC_Pass_signIN)
                    .addComponent(passC_Pass_signIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addComponent(btnC_SignIn)
                .addGap(271, 271, 271))
        );

        javax.swing.GroupLayout panel_customer_signInLayout = new javax.swing.GroupLayout(panel_customer_signIn);
        panel_customer_signIn.setLayout(panel_customer_signInLayout);
        panel_customer_signInLayout.setHorizontalGroup(
            panel_customer_signInLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panel_customer_signInLayout.setVerticalGroup(
            panel_customer_signInLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jLayeredPane1.add(panel_customer_signIn, "card4");

        panel_customer_check.setPreferredSize(new java.awt.Dimension(830, 680));

        kGradientPanel6.setBackground(new java.awt.Color(102, 153, 255));
        kGradientPanel6.setkEndColor(new java.awt.Color(0, 153, 153));
        kGradientPanel6.setkStartColor(new java.awt.Color(0, 153, 153));
        kGradientPanel6.setkTransparentControls(false);
        kGradientPanel6.setLayout(null);

        table_CustomerBookings.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Booking ID", "Booking Status", "Flight No", "Departure Time", "Arrival Time", "Cost", "Billing ID", "Total Paid"
            }
        ));
        table_CustomerBookings.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                table_CustomerBookingsMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(table_CustomerBookings);

        kGradientPanel6.add(jScrollPane3);
        jScrollPane3.setBounds(0, 0, 830, 160);

        jLabel50.setText("Payment ID");
        kGradientPanel6.add(jLabel50);
        jLabel50.setBounds(180, 200, 70, 20);

        jLabel51.setText("Amount");
        kGradientPanel6.add(jLabel51);
        jLabel51.setBounds(180, 240, 110, 16);

        jButton7.setText("Pay");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        kGradientPanel6.add(jButton7);
        jButton7.setBounds(360, 300, 90, 30);
        kGradientPanel6.add(txt_paymentID);
        txt_paymentID.setBounds(330, 200, 150, 22);
        kGradientPanel6.add(txt_paymentMethod);
        txt_paymentMethod.setBounds(330, 240, 150, 22);

        javax.swing.GroupLayout panel_customer_checkLayout = new javax.swing.GroupLayout(panel_customer_check);
        panel_customer_check.setLayout(panel_customer_checkLayout);
        panel_customer_checkLayout.setHorizontalGroup(
            panel_customer_checkLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 830, Short.MAX_VALUE)
        );
        panel_customer_checkLayout.setVerticalGroup(
            panel_customer_checkLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_customer_checkLayout.createSequentialGroup()
                .addComponent(kGradientPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 677, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 3, Short.MAX_VALUE))
        );

        jLayeredPane1.add(panel_customer_check, "card3");

        panel_booking_agent_login.setBackground(new java.awt.Color(106, 176, 76));
        panel_booking_agent_login.setPreferredSize(new java.awt.Dimension(830, 830));

        jButton2.setText("Login");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Agent ID");

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Password");

        javax.swing.GroupLayout panel_booking_agent_loginLayout = new javax.swing.GroupLayout(panel_booking_agent_login);
        panel_booking_agent_login.setLayout(panel_booking_agent_loginLayout);
        panel_booking_agent_loginLayout.setHorizontalGroup(
            panel_booking_agent_loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_booking_agent_loginLayout.createSequentialGroup()
                .addGap(195, 195, 195)
                .addGroup(panel_booking_agent_loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel1))
                .addGap(61, 61, 61)
                .addGroup(panel_booking_agent_loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(passAgent_Pass, javax.swing.GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE)
                    .addComponent(txtAgent_Id))
                .addContainerGap(277, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_booking_agent_loginLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addGap(367, 367, 367))
        );
        panel_booking_agent_loginLayout.setVerticalGroup(
            panel_booking_agent_loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_booking_agent_loginLayout.createSequentialGroup()
                .addGap(203, 203, 203)
                .addGroup(panel_booking_agent_loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtAgent_Id, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(37, 37, 37)
                .addGroup(panel_booking_agent_loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(passAgent_Pass, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(32, 32, 32)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(311, Short.MAX_VALUE))
        );

        jLayeredPane1.add(panel_booking_agent_login, "card7");

        unconfirmed_bookings_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Booking ID", "Booking Date", "Flight Number", "Customer ID", "Billing ID"
            }
        ));
        unconfirmed_bookings_table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                unconfirmed_bookings_tableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(unconfirmed_bookings_table);

        jLabel40.setText("Book");

        jLabel39.setText("Booking ID");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "yes", "no" }));

        AgentsBookingID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AgentsBookingIDActionPerformed(evt);
            }
        });

        btn_confirmBookingAgent.setText("Save");
        btn_confirmBookingAgent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_confirmBookingAgentActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel_booking_processLayout = new javax.swing.GroupLayout(panel_booking_process);
        panel_booking_process.setLayout(panel_booking_processLayout);
        panel_booking_processLayout.setHorizontalGroup(
            panel_booking_processLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 830, Short.MAX_VALUE)
            .addGroup(panel_booking_processLayout.createSequentialGroup()
                .addGroup(panel_booking_processLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_booking_processLayout.createSequentialGroup()
                        .addGap(291, 291, 291)
                        .addGroup(panel_booking_processLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(57, 57, 57)
                        .addGroup(panel_booking_processLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(AgentsBookingID, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panel_booking_processLayout.createSequentialGroup()
                        .addGap(367, 367, 367)
                        .addComponent(btn_confirmBookingAgent, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panel_booking_processLayout.setVerticalGroup(
            panel_booking_processLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_booking_processLayout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 116, Short.MAX_VALUE)
                .addGroup(panel_booking_processLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(AgentsBookingID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel39))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panel_booking_processLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(45, 45, 45)
                .addComponent(btn_confirmBookingAgent)
                .addGap(271, 271, 271))
        );

        jLayeredPane1.add(panel_booking_process, "card8");

        getContentPane().add(jLayeredPane1);
        jLayeredPane1.setBounds(190, 30, 830, 680);

        panel_close.setBackground(new java.awt.Color(45, 52, 54));
        panel_close.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                panel_closeMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panel_closeMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                panel_closeMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                panel_closeMousePressed(evt);
            }
        });

        jLabel6.setBackground(new java.awt.Color(102, 102, 102));
        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("X");

        javax.swing.GroupLayout panel_closeLayout = new javax.swing.GroupLayout(panel_close);
        panel_close.setLayout(panel_closeLayout);
        panel_closeLayout.setHorizontalGroup(
            panel_closeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_closeLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panel_closeLayout.setVerticalGroup(
            panel_closeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );

        getContentPane().add(panel_close);
        panel_close.setBounds(990, 0, 30, 30);

        jPanel2.setBackground(new java.awt.Color(102, 102, 255));
        jPanel2.setForeground(new java.awt.Color(255, 204, 51));

        jPanel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel4MousePressed(evt);
            }
        });

        jLabel43.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel43.setText("Customer SignUp");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel43)
                .addContainerGap(28, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
        );

        jPanel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel6MousePressed(evt);
            }
        });

        jLabel44.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel44.setText("Admin Login");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(jLabel44, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
                .addGap(27, 27, 27))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        jPanel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel5MousePressed(evt);
            }
        });

        jLabel45.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel45.setText("Agent Login");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel45)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(84, 84, 84)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(261, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2);
        jPanel2.setBounds(0, 30, 190, 600);

        setSize(new java.awt.Dimension(1019, 626));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        loginDashboard();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void combobox_flight_noActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combobox_flight_noActionPerformed

    }//GEN-LAST:event_combobox_flight_noActionPerformed
    /**
     * finds flight for the selected day
     *
     * @param evt
     */
    private void buttonc_FindFlightsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonc_FindFlightsActionPerformed

        refreshTable(flightSearchTable);
        String d = f1.format(datechooser_desired_date.getDate());
        combobox_flight_no.removeAllItems();
        System.out.print(OriginAirportCode());
        System.out.print(DestinationAirportCode());
        try {
            con = ConnectSQL.connect();

            sql = "Select flight_no, departure_date_time,  arrival_date_time, airline_name\n"
                    + "from flights\n"
                    + "inner join Airline_Aircraft on Flights.airline_aircraft_code=Airline_Aircraft.airline_aircraft_code\n"
                    + "inner join Airline on airline_aircraft.airline_code=Airline.airline_code \n"
                    + "where origin_airport_code='" + OriginAirportCode() + "' \n"
                    + "and destination_airport_code='" + DestinationAirportCode() + "' \n"
                    + "and convert(nvarchar, departure_date_time, 23)='" + d + "'";
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();

            if (rs.next()) {

                Object[] row = new Object[4];
                row[0] = rs.getString("flight_no");
                row[1] = rs.getString("airline_name");
                row[2] = rs.getTimestamp("departure_date_time");
                row[3] = rs.getTimestamp("arrival_date_time");
                combobox_flight_no.addItem(rs.getString("flight_no"));
                combobox_ticket_type.setEnabled(true);

                flightSearchTable.addRow(row);
                combobox_flight_no.setEnabled(true);
                combobox_ticket_type.setEnabled(true);
                combobox_travel_class.setEnabled(true);

            } else {
                JOptionPane.showMessageDialog(null, "No Flight Found", "Sorry", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_buttonc_FindFlightsActionPerformed


    private void buttonc_ShowCostActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonc_ShowCostActionPerformed
        lbl_show_basePrice.setText(String.valueOf(base_price()));
        lbl_Show_ticketTypeMultiplies.setText(String.valueOf(price_ticket()));
        lbl_show_travel_class_difference.setText(String.valueOf(ticket_varies()) + "%");
        lbl_show_total_cost.setText(String.valueOf(total_cost()));
    }//GEN-LAST:event_buttonc_ShowCostActionPerformed


    private void buttonc_BookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonc_BookActionPerformed
        String d = f1.format(date);
        String flight_no = flight_values()[3];
        try {
            con = ConnectSQL.connect();
            sql = "insert into booking (customer_id, flight_no,bookingdate) values(?,?,?)";

            stm = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stm.setString(1, this.CustomerID);
            stm.setString(2, flight_no);
            stm.setString(3, d);
            stm.execute();
            rs = stm.getGeneratedKeys();
            if (rs.next()) {

                String sql2 = "insert into billing(booking_status_code,cost) values(?,?)";
                PreparedStatement stm2 = con.prepareStatement(sql2, Statement.RETURN_GENERATED_KEYS);
                stm2.setInt(1, rs.getInt(1));
                stm2.setDouble(2, total_cost());
                stm2.execute();
                ResultSet rs2 = stm2.getGeneratedKeys();
                if (rs.next()) {
                    String sql3 = "insert into payment(billing_id,amount,date)values(?,0,getdate())";
                    PreparedStatement stm3 = con.prepareStatement(sql3);
                    stm3.setInt(1, rs.getInt("billing_id"));
                    stm3.execute();

                }
                JOptionPane.showMessageDialog(null, "Successfully Booked", "Success", JOptionPane.INFORMATION_MESSAGE);
            }

            combobox_flight_no.setSelectedItem(null);
        } catch (HeadlessException | SQLException e) {
            System.out.println(e);
        }


    }//GEN-LAST:event_buttonc_BookActionPerformed

    private void panel_closeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panel_closeMouseClicked

    }//GEN-LAST:event_panel_closeMouseClicked

    private void panel_closeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panel_closeMouseEntered
        panel_close.setBackground(Home.color6);
    }//GEN-LAST:event_panel_closeMouseEntered

    private void panel_closeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panel_closeMouseExited
        panel_close.setBackground(Home.color7);
    }//GEN-LAST:event_panel_closeMouseExited

    private void panel_closeMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panel_closeMousePressed
        System.exit(0);
    }//GEN-LAST:event_panel_closeMousePressed

    private void panel_titleBarMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panel_titleBarMousePressed
        xMouse = evt.getX();
        yMouse = evt.getY();
    }//GEN-LAST:event_panel_titleBarMousePressed

    private void panel_titleBarMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panel_titleBarMouseDragged
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation(x - xMouse, y - yMouse);
    }//GEN-LAST:event_panel_titleBarMouseDragged

    private void jPanel4MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel4MousePressed
        SwitchPanel(panel_customer_signUp, "Customer Sign Up");
    }//GEN-LAST:event_jPanel4MousePressed

    private void btnC_SignUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnC_SignUpActionPerformed
        try {
            con = ConnectSQL.connect();

            sql = "Insert into customer_info (customer_name,customer_email,customer_password,customer_phone,customer_address) values(?,?,?,?,?)";
            stm = con.prepareStatement(sql);
            stm.setString(1, txtC_Name.getText());
            stm.setString(2, txtC_Email.getText());
            stm.setString(3, String.valueOf(passC_Pass.getPassword()));
            stm.setString(4, txtC_Phone.getText());
            stm.setString(5, txtC_Address.getText());
            stm.execute();
            SwitchPanel(panel_customer_signIn, "Customer Sign In");

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnC_SignUpActionPerformed

    private void btnC_SignInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnC_SignInActionPerformed

        try {
            con = ConnectSQL.connect();
            sql = "select * from customer where customer_email='" + txtC_Email_signIn.getText() + " ' or customer_id='" + txtC_Email_signIn.getText() + "' and customer_password='" + String.valueOf(passC_Pass_signIn.getPassword()) + "'";
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            if (rs.next()) {

                SwitchPanel(panel_customer_booking, "Customer Booking");

                this.CustomerID = rs.getString("customer_id");

            } else {
                JOptionPane.showMessageDialog(null, "Incorrect Customer Email or Password", "ERROR", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }//GEN-LAST:event_btnC_SignInActionPerformed

    private void jLabel30MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel30MousePressed
        SwitchPanel(panel_customer_signIn, "Customer Sign In");
    }//GEN-LAST:event_jLabel30MousePressed

    private void jPanel6MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel6MousePressed
        SwitchPanel(panel_admin_login, "Login As Admin");
    }//GEN-LAST:event_jPanel6MousePressed

    private void lbl_show_previous_bookingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lbl_show_previous_bookingsActionPerformed

        SwitchPanel(panel_customer_check, "Checkout");
        CustomeBookingsAll();
    }//GEN-LAST:event_lbl_show_previous_bookingsActionPerformed

    private void txtC_NameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtC_NameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtC_NameActionPerformed

    private void table_CustomerBookingsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_CustomerBookingsMouseClicked
        DefaultTableModel model = (DefaultTableModel) table_CustomerBookings.getModel();
        int selected_row = table_CustomerBookings.getSelectedRow();
        txt_paymentID.setText(model.getValueAt(selected_row, 6).toString());
        txt_paymentID.setEditable(false);

    }//GEN-LAST:event_table_CustomerBookingsMouseClicked
//
//    private void update_reservation_status() {
//        try {
//            con = ConnectSQL.connect();
//            sql = "update ref_reservation_status set is_reserved='yes' where reservation_status_code=" + this.reservation_status_code;;
//
//            stm = con.prepareStatement(sql);
//            stm.execute();
//
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
//    }

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        CustomeBookingsAll();
        try {
            con = ConnectSQL.connect();
            sql = "insert into payment (billing_id,amount,date)values(?,?,getdate())";

            stm = con.prepareStatement(sql);
            stm.setInt(1, Integer.parseInt(txt_paymentID.getText()));

            stm.setDouble(2, Double.parseDouble(txt_paymentMethod.getText()));
            stm.execute();
            refreshTable(CustomerBookingTable);
            CustomeBookingsAll();
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }//GEN-LAST:event_jButton7ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jPanel5MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel5MousePressed
        SwitchPanel(panel_booking_agent_login, "Login As Booking Agent");
    }//GEN-LAST:event_jPanel5MousePressed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        
        try {
            sql = "select * from booking_agents where agent_id='" + (txtAgent_Id.getText()) + "' and agent_password='" + String.valueOf(passAgent_Pass.getPassword()) + "'";
            con = ConnectSQL.connect();
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            if (rs.next()) {
                SwitchPanel(panel_booking_process, "Process Bookings");
                this.bookingAgent = rs.getString("agent_id");
                 ShowUnconfirmedBookings();
            }
        } catch (SQLException e) {
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void unconfirmed_bookings_tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_unconfirmed_bookings_tableMouseClicked

        int selected_row = unconfirmed_bookings_table.getSelectedRow();
        AgentsBookingID.setText(unconfirmed_bookings_table.getValueAt(selected_row, 0).toString());
        AgentsBookingID.setEditable(false);
    
    }//GEN-LAST:event_unconfirmed_bookings_tableMouseClicked

    private void AgentsBookingIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AgentsBookingIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_AgentsBookingIDActionPerformed

    private void btn_confirmBookingAgentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_confirmBookingAgentActionPerformed
        
        try {
            con = ConnectSQL.connect();
            sql = "update booking set bookingagent='" + this.bookingAgent + "', is_booked='" + jComboBox1.getSelectedItem().toString() + "' where booking_status_code=" + AgentsBookingID.getText();
            stm = con.prepareStatement(sql);
            stm.execute();
            ShowUnconfirmedBookings();
            refreshTable(unconfirmedBookingTable);
            refreshTable(unconfirmedBookingTable);
            ShowUnconfirmedBookings();
            JOptionPane.showMessageDialog(null, "Success", "ERROR", JOptionPane.INFORMATION_MESSAGE);

          

        } catch (SQLException e) {
            System.out.println(e);
        }

    }//GEN-LAST:event_btn_confirmBookingAgentActionPerformed

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        new LoginsAndCustomerBookings().setVisible(true);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField AgentsBookingID;
    private javax.swing.JButton btnC_SignIn;
    private javax.swing.JButton btnC_SignUp;
    private javax.swing.JButton btn_confirmBookingAgent;
    private javax.swing.JButton buttonc_Book;
    private javax.swing.JButton buttonc_FindFlights;
    private javax.swing.JButton buttonc_ShowCost;
    private javax.swing.JComboBox<String> combobox_destination_airport;
    private javax.swing.JComboBox<String> combobox_flight_no;
    private javax.swing.JComboBox<String> combobox_origin_airport;
    private javax.swing.JComboBox<String> combobox_ticket_type;
    private javax.swing.JComboBox<String> combobox_travel_class;
    private com.toedter.calendar.JDateChooser datechooser_desired_date;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton7;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField jTextField1;
    private keeptoo.KGradientPanel kGradientPanel1;
    private keeptoo.KGradientPanel kGradientPanel2;
    private keeptoo.KGradientPanel kGradientPanel3;
    private keeptoo.KGradientPanel kGradientPanel5;
    private keeptoo.KGradientPanel kGradientPanel6;
    private javax.swing.JLabel lbl_Show_ticketTypeMultiplies;
    private javax.swing.JLabel lbl_base_price;
    private javax.swing.JLabel lbl_desired_date;
    private javax.swing.JLabel lbl_destination_airport;
    private javax.swing.JLabel lbl_origin_airport;
    private javax.swing.JLabel lbl_show_basePrice;
    private javax.swing.JButton lbl_show_previous_bookings;
    private javax.swing.JLabel lbl_show_total_cost;
    private javax.swing.JLabel lbl_show_travel_class_difference;
    private javax.swing.JLabel lbl_ticket_type_multiplies;
    private javax.swing.JLabel lbl_tips;
    private javax.swing.JLabel lbl_total_cost;
    private javax.swing.JLabel lbl_travel_class_difference;
    private javax.swing.JLabel lvlC_Address;
    private javax.swing.JLabel lvlC_Email;
    private javax.swing.JLabel lvlC_Email_signIn;
    private javax.swing.JLabel lvlC_Name;
    private javax.swing.JLabel lvlC_Pass;
    private javax.swing.JLabel lvlC_Pass_signIN;
    private javax.swing.JLabel lvlC_phone;
    public static javax.swing.JPanel panel_admin_login;
    private javax.swing.JPanel panel_booking_agent_login;
    private javax.swing.JPanel panel_booking_process;
    private javax.swing.JPanel panel_close;
    private javax.swing.JPanel panel_customer_booking;
    public static javax.swing.JPanel panel_customer_check;
    private javax.swing.JPanel panel_customer_signIn;
    private javax.swing.JPanel panel_customer_signUp;
    private javax.swing.JPanel panel_titleBar;
    private javax.swing.JPasswordField passAgent_Pass;
    private javax.swing.JPasswordField passC_Pass;
    private javax.swing.JPasswordField passC_Pass_signIn;
    private javax.swing.JTable table_CustomerBookings;
    private javax.swing.JTable table_searched_flights;
    private javax.swing.JTextField txtAgent_Id;
    private javax.swing.JTextField txtC_Address;
    private javax.swing.JTextField txtC_Email;
    private javax.swing.JTextField txtC_Email_signIn;
    private javax.swing.JTextField txtC_Name;
    private javax.swing.JTextField txtC_Phone;
    private javax.swing.JTextField txt_paymentID;
    private javax.swing.JTextField txt_paymentMethod;
    private javax.swing.JTable unconfirmed_bookings_table;
    // End of variables declaration//GEN-END:variables
}
