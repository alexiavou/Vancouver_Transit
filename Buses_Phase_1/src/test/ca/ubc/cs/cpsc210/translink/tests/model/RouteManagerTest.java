package ca.ubc.cs.cpsc210.translink.tests.model;

import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import ca.ubc.cs.cpsc210.translink.model.RoutePattern;
import ca.ubc.cs.cpsc210.translink.model.Stop;
import ca.ubc.cs.cpsc210.translink.util.LatLon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Test the RouteManager
 */
public class RouteManagerTest {

    @BeforeEach
    public void setup() {
        RouteManager.getInstance().clearRoutes();
    }

    @Test
    public void testBasic() {
        Route r43 = new Route("43");
        Route r = RouteManager.getInstance().getRouteWithNumber("43");
        assertEquals(r43, r);
    }

    @Test
    public void testConstructor() {
        assertEquals(0, RouteManager.getInstance().getNumRoutes());
    }

    @Test
    public void testGetRouteWithNumber() {
        Route r0 = RouteManager.getInstance().getRouteWithNumber("5", "R0");
        assertEquals(1, RouteManager.getInstance().getNumRoutes());
        RouteManager.getInstance().clearRoutes();
        assertEquals(0, RouteManager.getInstance().getNumRoutes());

        Route r1 = RouteManager.getInstance().getRouteWithNumber("5");
        assertEquals(1, RouteManager.getInstance().getNumRoutes());

        Route r2 = RouteManager.getInstance().getRouteWithNumber("5", "R2");
        assertEquals(1, RouteManager.getInstance().getNumRoutes());

        Route r3 = RouteManager.getInstance().getRouteWithNumber("7");
        assertEquals(2, RouteManager.getInstance().getNumRoutes());

        Route r4 = RouteManager.getInstance().getRouteWithNumber("9", "R4");
        assertEquals(3, RouteManager.getInstance().getNumRoutes());
        assertEquals("R4", r4.getName());

        Route r5 = RouteManager.getInstance().getRouteWithNumber("9", "R5");
        assertEquals(3, RouteManager.getInstance().getNumRoutes());
        assertEquals("R5", r5.getName());


        RouteManager.getInstance().clearRoutes();
        assertEquals(0, RouteManager.getInstance().getNumRoutes());

    }

    @Test
    public void testRoutes() {
        Route r43 = new Route("43");
        Route r = RouteManager.getInstance().getRouteWithNumber("43");

        assertTrue(r.getStops().isEmpty());

        LatLon locn = new LatLon(42.0000, 123.0000);
        Stop stop = new Stop(1, "", locn);
        r.addStop(stop);
        assertEquals(1, r.getStops().size());

        RoutePattern rp = new RoutePattern("a", "b", "c", r);
        List<LatLon> path = new LinkedList<LatLon>();
        r.addPattern(rp);
        assertEquals(0, rp.getPath().size());
        assertEquals("c", rp.getDirection());

        r.removeStop(stop);
        assertEquals(0, r.getStops().size());

    }


}
