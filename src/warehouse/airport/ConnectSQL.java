package warehouse.airport;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class ConnectSQL {

    Connection con = null;

    public static Connection connect() {

        try {
            try {
               Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            } catch (ClassNotFoundException e) {
                JOptionPane.showMessageDialog(null, e);
            }
            String url = "jdbc:sqlserver://localhost;databaseName=AirportDM";
            Connection con = DriverManager.getConnection(url, "sa", "root");

              return con;
       } catch (Throwable e) {
            e.printStackTrace();
            return  null;
        }
      
    }


}
