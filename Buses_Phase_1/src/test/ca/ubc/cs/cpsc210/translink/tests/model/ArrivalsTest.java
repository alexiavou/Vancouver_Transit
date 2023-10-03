package ca.ubc.cs.cpsc210.translink.tests.model;

import ca.ubc.cs.cpsc210.translink.model.Arrival;
import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Test Arrivals
 */
public class ArrivalsTest {
    Route r;
    Arrival a;

    @BeforeEach
    public void setup() {
        r = RouteManager.getInstance().getRouteWithNumber("43");
        a = new Arrival(23, "Home", r);
    }

    @Test
    public void testConstructor() {
        assertEquals(23, a.getTimeToStopInMins());
        assertEquals(r, a.getRoute());
        assertEquals(" ", a.getStatus());
        assertEquals("Home", a.getDestination());
    }

    @Test
    public void testCompareTo() {
        Route r1 = RouteManager.getInstance().getRouteWithNumber("45");
        Arrival a1 = new Arrival(20, "Home", r1);
        assertEquals(3, a.compareTo(a1));
        a1.setStatus("*");
        assertEquals("*", a1.getStatus());

        Arrival a2 = a;
        assertEquals(0, a.compareTo(a2));

        Route r3 = RouteManager.getInstance().getRouteWithNumber("40");
        Arrival a3 = new Arrival(30, "Home", r3);
        assertEquals(-7, a.compareTo(a3));
    }

}
