package ca.ubc.cs.cpsc210.translink.parsers;

import ca.ubc.cs.cpsc210.translink.model.*;
import ca.ubc.cs.cpsc210.translink.parsers.exception.ArrivalsDataMissingException;
import org.json.JSONException;
import org.json.*;

/**
 * A parser for the data returned by the Translink arrivals at a stop query
 */
public class ArrivalsParser {

    /**
     * Parse arrivals from JSON response produced by TransLink query.  All parsed arrivals are
     * added to the given stop assuming that corresponding JSON object has a RouteNo: and an
     * array of Schedules:
     * Each schedule must have an ExpectedCountdown, ScheduleStatus, and Destination.  If
     * any of the aforementioned elements is missing, the arrival is not added to the stop.
     *
     * @param stop             stop to which parsed arrivals are to be added
     * @param jsonResponse    the JSON response produced by Translink
     * @throws JSONException  when:
     * <ul>
     *     <li>JSON response does not have expected format (JSON syntax problem)</li>
     *     <li>JSON response is not an array</li>
     * </ul>
     * @throws ArrivalsDataMissingException  when no arrivals are found in the reply
     */
    public static void parseArrivals(Stop stop, String jsonResponse) throws JSONException, ArrivalsDataMissingException {
        boolean adme = false;
        JSONArray arrivals = new JSONArray(jsonResponse);
        Stop stop1 = StopManager.getInstance().getStopWithNumber(stop.getNumber(), stop.getName(), stop.getLocn());

        for (int i = 0; i < arrivals.length(); i++) {
            try {
                JSONObject arrival = arrivals.getJSONObject(i);
                String routeNo = arrival.getString("RouteNo");
                JSONArray schedules = arrival.getJSONArray("Schedules");
                String routeName = arrival.getString("RouteName");
                Route route = RouteManager.getInstance().getRouteWithNumber(routeNo, routeName);

                for (int j = 0; j < schedules.length(); j++) {
                    int expectedCountdown = schedules.getJSONObject(j).getInt("ExpectedCountdown");
                    String destination = schedules.getJSONObject(j).getString("Destination");
                    String scheduledStatus = schedules.getJSONObject(j).getString("ScheduleStatus");

                    Arrival newArrival = new Arrival(expectedCountdown, destination, route);
                    newArrival.setStatus(scheduledStatus);
                    stop1.addArrival(newArrival);
                    route.addStop(stop1);
                    stop1.addRoute(route);
                }
            }
            catch (JSONException je) {
                adme = true;
            }
        }
        if (adme) {
            throw new ArrivalsDataMissingException("Arrivals data missing exception");
        }
    }
}
