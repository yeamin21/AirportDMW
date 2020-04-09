package warehouse.airport;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author yeami
 */
public class HomeTest {
    @Test
    public void testShow_Unconfirmed_Bookings() {
       Home p= new Home();
       assertNull(p.NullAgents());
    }
    
}
