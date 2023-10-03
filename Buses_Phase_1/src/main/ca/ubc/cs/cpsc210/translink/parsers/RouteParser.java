package ca.ubc.cs.cpsc210.translink.parsers;

import ca.ubc.cs.cpsc210.translink.model.RouteManager;
import ca.ubc.cs.cpsc210.translink.model.RoutePattern;
import ca.ubc.cs.cpsc210.translink.model.Route;
import ca.ubc.cs.cpsc210.translink.parsers.exception.RouteDataMissingException;
import ca.ubc.cs.cpsc210.translink.providers.DataProvider;
import ca.ubc.cs.cpsc210.translink.providers.FileDataProvider;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Parse route information in JSON format.
 */
public class RouteParser {
    private String filename;

    public RouteParser(String filename) {
        this.filename = filename;
    }
    /**
     * Parse route data from the file and add all route to the route manager.
     *
     */
    public void parse() throws IOException, RouteDataMissingException, JSONException{
        DataProvider dataProvider = new FileDataProvider(filename);

        parseRoutes(dataProvider.dataSourceToString());
    }
    /**
     * Parse route information from JSON response produced by Translink.
     * Stores all routes and route patterns found in the RouteManager.
     *
     * @param  jsonResponse    string encoding JSON data to be parsed
     * @throws JSONException   when:
     * <ul>
     *     <li>JSON data does not have expected format (JSON syntax problem)
     *     <li>JSON data is not an array
     * </ul>
     * If a JSONException is thrown, no routes should be added to the route manager
     *
     * @throws RouteDataMissingException when
     * <ul>
     *  <li>JSON data is missing RouteNo, Name, or Patterns element for any route</li>
     *  <li>The value of the Patterns element is not an array for any route</li>
     *  <li>JSON data is missing PatternNo, Destination, or Direction element for any route pattern</li>
     * </ul>
     * If a RouteDataMissingException is thrown, all correct routes are first added to the route manager.
     */

    public void parseRoutes(String jsonResponse) throws JSONException, RouteDataMissingException {
        boolean rdme = false;
        JSONArray routes = new JSONArray(jsonResponse);

        for (int i = 0; i < routes.length(); i++) {
            try {
                JSONObject route = routes.getJSONObject(i);
                String routeNo = route.getString("RouteNo");
                String name = route.getString("Name");
                JSONArray patterns = route.getJSONArray("Patterns");
                Route newRoute = RouteManager.getInstance().getRouteWithNumber(routeNo, name);

                for (int j = 0; j < patterns.length(); j++) {
                    JSONObject pattern = patterns.getJSONObject(j);
                    String patternNo = pattern.getString("PatternNo");
                    String destination = pattern.getString("Destination");
                    String direction = pattern.getString("Direction");
                    newRoute.getPattern(patternNo, destination, direction);
                }
            }
            catch (JSONException je) {
                rdme = true;
            }
        }
        if (rdme) {
            throw new RouteDataMissingException("Route data missing exception");
        }
    }
}