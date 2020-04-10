![Dashboard](https://github.com/yeamin21/AirportDMW/blob/master/src/warehouse/resourses/DashboardSS.png)

# Airport Data Management
This is the updated version of previous model I designed for Object Oriented Programing. This one is for Database Management System course. Database is well designed, normalized and the queries are much cleaner.

## Incase you want to see whats inside:
* Admin ID: Root
* Admin Password: Root
## Requirements:
 ### Software:
* IDE: Netbeans, IntelliJ, eclipse. Netbeans Recommended
* For Database: Microsoft SQL server
### Hardware: 
* CPU: Intel pentium or equivalent AMD processor
* RAM: 2GB
* HDD free space: 40mb
 
## Programming Language used in this projects are:
* JAVA: Frontend
* SQL: Backend

# Features:
* Visual representation of data for data analyst.

*  Admin can create Create, Read, Update and Delete data

![CRUD](https://github.com/yeamin21/Airport_Data_Management/blob/master/src/warehouse/resourses/SS_manageAirport.png)

* Admin can search to see data for different date, agent and airlines

![warehouse](https://github.com/yeamin21/AirportDMW/blob/master/src/warehouse/resourses/WarehouseSS.png)

* Customer books flight
* Booking agent confirms whether 'customers booking' is possible or not


# Snippets

* Customer Login using id or email
```java
   private void btnC_SignInActionPerformed(java.awt.event.ActionEvent evt) {                                            

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

    }
```
  
* Most Popular Airline
```java 
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
 ```
 * Performing Booking request
 
 ```java
   private void buttonc_BookActionPerformed(java.awt.event.ActionEvent evt) {                                             
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


    }
```
