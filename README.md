![Dashboard](https://github.com/yeamin21/AirportDMW/blob/master/src/warehouse/resourses/DashboardSS.png)

# Airport Data Management

 This was my university OOP project as well as a project I was doing for a publication under a former Daffodil University professor.The user interface is designed in Java Swing. 

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

*  Admin can create Create, Read, Update and Delete datas

![CRUD](https://github.com/yeamin21/Airport_Data_Management/blob/master/src/warehouse/resourses/SS_manageAirport.png)

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
