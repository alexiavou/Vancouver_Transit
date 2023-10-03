package ca.ubc.cs.cpsc210.translink.tests.model;

import ca.ubc.cs.cpsc210.translink.model.*;
import ca.ubc.cs.cpsc210.translink.util.LatLon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Test the StopManager
 */
public class StopManagerTest {

    @BeforeEach
    public void setup() {
        StopManager.getInstance().clearStops();
    }

    @Test
    public void testBasic() {
        Stop s9999 = new Stop(9999, "My house", new LatLon(-49.2, 123.2));
        Stop r = StopManager.getInstance().getStopWithNumber(9999);
        assertEquals(s9999, r);
    }

    @Test
    public void testConstructor() {
        assertEquals(0, StopManager.getInstance().getNumStops());
        assertEquals(null, StopManager.getInstance().getSelected());
    }

    @Test
    public void testGetStopWithNumber() {
        Stop r1 = StopManager.getInstance().getStopWithNumber(1999);
        assertEquals(1, StopManager.getInstance().getNumStops());

        Stop s3839 = new Stop(3839, "A", new LatLon(-49.264, 123.0));
        assertEquals(1, StopManager.getInstance().getNumStops());
        Stop r2 = StopManager.getInstance().getStopWithNumber(3839);
        assertEquals(2, StopManager.getInstance().getNumStops());

        Stop r3 = StopManager.getInstance().getStopWithNumber(7346, "B", new LatLon(-49.21, 123.23));
        assertEquals(3, StopManager.getInstance().getNumStops());

        Stop r4 = StopManager.getInstance().getStopWithNumber(3839, "C", new LatLon(-49.2178, 123.253));
        assertEquals(3, StopManager.getInstance().getNumStops());

        StopManager.getInstance().clearStops();
        assertEquals(0, StopManager.getInstance().getNumStops());
    }


    @Test
    public void testFindNearestTo() {
        Stop r1 = StopManager.getInstance().getStopWithNumber(1000, "A", new LatLon(-49.20002, 123.11));
        Stop r2 = StopManager.getInstance().getStopWithNumber(2000, "B", new LatLon(-49.20001, 123.11));
        Stop r3 = StopManager.getInstance().getStopWithNumber(3000, "C", new LatLon(-49.20004, 123.11));
        assertEquals(3, StopManager.getInstance().getNumStops());

        LatLon pt = new LatLon(-49.20000, 123.11);
        assertEquals(r2, StopManager.getInstance().findNearestTo(pt));

        LatLon pt1 = new LatLon(-49.20000, 123.31);
        assertEquals(null, StopManager.getInstance().findNearestTo(pt1));
    }

    @Test
    public void testStopAndBus() {
        Stop s9999 = new Stop(9999, "My house", new LatLon(-49.2, 123.2));
        Stop s = StopManager.getInstance().getStopWithNumber(9999);
        Route r43 = new Route("43");
        Route r = RouteManager.getInstance().getRouteWithNumber("43");
        s.addRoute(r);
        assertEquals(1, s.getRoutes().size());
        s.removeRoute(r);
        assertEquals(0, s.getRoutes().size());
        LatLon pt1 = new LatLon(-49.20000, 123.31);
        s.setLocn(pt1);
        assertEquals(pt1,s.getLocn());

        Bus b = new Bus(r, 42.0, 123.0, "a", "11:00");
        LatLon pt2 = new LatLon(42.0, 123.0);
        assertEquals(pt2, b.getLatLon());
        assertEquals("11:00", b.getTime());
    }

    @Test
    public void Selected() {
        assertEquals(null, StopManager.getInstance().getSelected());
        Stop r1 = StopManager.getInstance().getStopWithNumber(1999);
        assertEquals(1, StopManager.getInstance().getNumStops());
        Stop r2 = StopManager.getInstance().getStopWithNumber(3839);
        assertEquals(2, StopManager.getInstance().getNumStops());
        StopManager.getInstance().clearSelectedStop();

    }

}
