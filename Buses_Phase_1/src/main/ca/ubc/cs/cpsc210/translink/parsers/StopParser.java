package ca.ubc.cs.cpsc210.translink.parsers;

import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import ca.ubc.cs.cpsc210.translink.model.Stop;
import ca.ubc.cs.cpsc210.translink.model.StopManager;
import ca.ubc.cs.cpsc210.translink.parsers.exception.StopDataMissingException;
import ca.ubc.cs.cpsc210.translink.providers.DataProvider;
import ca.ubc.cs.cpsc210.translink.providers.FileDataProvider;
import ca.ubc.cs.cpsc210.translink.util.LatLon;
import org.json.JSONException;
import org.json.*;

import java.io.IOException;
import java.lang.reflect.Array;

/**
 * A parser for the data returned by Translink stops query
 */
public class StopParser {

    private String filename;

    public StopParser(String filename) {
        this.filename = filename;
    }
    /**
     * Parse stop data from the file and add all stops to stop manager.
     *
     */
    public void parse() throws IOException, StopDataMissingException, JSONException{
        DataProvider dataProvider = new FileDataProvider(filename);

        parseStops(dataProvider.dataSourceToString());
    }
    /**
     * Parse stop information from JSON response produced by Translink.
     * Stores all stops and routes found in the StopManager and RouteManager.
     *
     * @param  jsonResponse    string encoding JSON data to be parsed
     * @throws JSONException when:
     * <ul>
     *     <li>JSON data does not have expected format (JSON syntax problem)</li>
     *     <li>JSON data is not an array</li>
     * </ul>
     * If a JSONException is thrown, no stops should be added to the stop manager
     * @throws StopDataMissingException when
     * <ul>
     *  <li> JSON data is missing Name, StopNo, Routes or location (Latitude or Longitude) elements for any stop</li>
     * </ul>
     * If a StopDataMissingException is thrown, all correct stops are first added to the stop manager.
     */

    public void parseStops(String jsonResponse) throws JSONException, StopDataMissingException {
        boolean sdme = false;
        JSONArray stops = new JSONArray(jsonResponse);

        for (int i = 0; i < stops.length(); i++) {
            try {
                JSONObject stop = stops.getJSONObject(i);
                String name = stop.getString("Name");
                int stopNo = stop.getInt("StopNo");
                double latitude = stop.getDouble("Latitude");
                double longitude = stop.getDouble("Longitude");
                String[] routes = stop.getString("Routes").split(",");

                LatLon locn = new LatLon(latitude, longitude);
                Stop newStop = StopManager.getInstance().getStopWithNumber(stopNo, name, locn);

                for (String r : routes) {
                    String s = r.trim();
                    RouteManager.getInstance().getRouteWithNumber(s);
                }
            }
            catch (JSONException je) {
                sdme = true;
            }
        }
        if (sdme) {
            throw new StopDataMissingException("Stop data missing exception");
        }
    }

}
