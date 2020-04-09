package warehouse.airport;



import java.sql.SQLException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author yeami
 */
public class ConnectSQLTest {
    @Test
    public void ConnectionTest() throws SQLException {
        String result=ConnectSQL.connect().getCatalog();
        assertEquals("airport_dm",result);
    }   
}
