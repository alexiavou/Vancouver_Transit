package ca.ubc.cs.cpsc210.translink.model;

import ca.ubc.cs.cpsc210.translink.model.exception.RouteException;
import ca.ubc.cs.cpsc210.translink.util.LatLon;

import java.util.*;

/**
 * Represents a bus stop with an number, name, location (lat/lon)
 * set of routes which stop at this stop, list of buses that pass
 * through this stop and a list of arrivals.
 */
// TODO: Task 2: Complete all the methods of this class

public class Stop implements Iterable<Arrival> {
    private List<Arrival> arrivals;
    private Set<Route> routes;
    private List<Bus> buses;
    private List<LatLon> latLons;
    private int number;
    private String name;
    private LatLon locn;

    /**
     * Constructs a stop with given number, name and location.
     * Set of routes and list of arrivals are empty.
     *
     * @param number    the number of this stop
     * @param name      name of this stop
     * @param locn      location of this stop
     */
    public Stop(int number, String name, LatLon locn) {
        this.number = number;
        this.name = name;
        this.locn = locn;
        this.arrivals = new LinkedList<Arrival>();
        this.routes = new HashSet<Route>();
        this.buses = new LinkedList<Bus>();

    }

    /**
     * getter for name
     * @return      the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * getter for locn
     * @return      the location
     */
    public LatLon getLocn() {
        return this.locn;
    }

    /**
     * getter for number
     * @return      the number
     */
    public int getNumber() {
        return this.number;
    }

    /**
     * getter for set of routes
     * @return      an unmodifiable set of routes using this stop
     */
    public Set<Route> getRoutes() {
        return Collections.unmodifiableSet(routes);
    }

    /**
     * Add route to set of routes with stops at this stop.
     *
     * @param route  the route to add
     */
    public void addRoute(Route route) {
        if (!onRoute(route)) {
            this.routes.add(route);
            route.addStop(this);
        }
    }

    /**
     * Remove route from set of routes with stops at this stop
     *
     * @param route the route to remove
     */
    public void removeRoute(Route route) {
        this.routes.remove(route);
    }

    /**
     * Determine if this stop is on a given route
     * @param route  the route
     * @return  true if this stop is on given route
     */
    public boolean onRoute(Route route) {
        for (Stop s: route.getStops()) {
            return (equals(s));
        }
        return false;
    }

    /**
     * Add bus arrival travelling on a particular route at this stop.
     * Arrivals are to be sorted in order by arrival time
     *
     * @param arrival  the bus arrival to add to stop
     */
    public void addArrival(Arrival arrival) {
        this.arrivals.add(arrival);
        for (Arrival next : this.arrivals) {
            next.compareTo(arrival);
        }
    }

    /**
     * Remove all arrivals from this stop
     */
    public void clearArrivals() {
        this.arrivals.clear();
    }

    /**
     * Adds bus that is on a route passing through this stop
     * @param bus  bus to add
     * @throws RouteException if bus is not on a route on which this stop lies
     */
    public void addBus(Bus bus) throws RouteException {
        for(Stop s : bus.getRoute().getStops()) {
            if (equals(s)) {
                this.buses.add(bus);
                break;
            }
        }
        if (!this.buses.contains(bus)) {
            throw new RouteException("The bus' route does not contain this stop.");
        }
    }

    /**
     * Get unmodifiable list of buses on routes serving this stop
     * @return  unmodifiable list of buses
     */
    public List<Bus> getBuses() {
        return Collections.unmodifiableList(buses);
    }

    /**
     * Clear all buses from this stop
     */
    public void clearBuses() {
        this.buses.clear();
    }

    /**
     * Two stops are equal if their numbers are equal
     */
    @Override
    public boolean equals(Object o) {
        return ((o instanceof Stop) && (this.number == ((Stop) o).getNumber()));
    }

    /**
     * Two stops are equal if their numbers are equal.
     * Therefore hashCode only pays attention to number.
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(this.number);
    }

    @Override
    public Iterator<Arrival> iterator() {
        // Do not modify the implementation of this method!
        return arrivals.iterator();
    }

    /**
     * setter for name
     *
     * @param name      the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * setter for location
     * @param locn      the new location
     */
    public void setLocn(LatLon locn) {
        this.locn = locn;
    }
}
