package ca.mcmaster.se2aa4.island.team31.Terrain;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Report class - Singleton pattern implementation for tracking island discoveries
 * Stores and manages information about creeks and emergency sites found during exploration
 */
public class Report {
    //singleton instance
    private static Report instance = null;
    
    //data storage
    private final JSONObject discoveries;  //stores all POIs
    private boolean isValid;               //true if we have at least one creek and site

    private Report() {
        discoveries = new JSONObject();
        //initialize empty arrays
        discoveries.put("creeks", new JSONArray());
        discoveries.put("sites", new JSONArray());
        isValid = false;
    }

    //singleton access
    public static Report getInstance() {
        if (instance == null) {
            instance = new Report();
        }
        return instance;
    }

    //POI management methods
    
    /**
     * Records a newly discovered creek location
     * @param creekId unique identifier for the creek
     * @param x x-coordinate of the creek
     * @param y y-coordinate of the creek
     */
    public void addCreek(String creekId, int x, int y) {
        JSONObject creek = createPOIObject(creekId, x, y);
        discoveries.getJSONArray("creeks").put(creek);
        updateValidity();
    }

    /**
     * Records a newly discovered emergency site location
     * @param siteId unique identifier for the site
     * @param x x-coordinate of the site
     * @param y y-coordinate of the site
     */
    public void addSite(String siteId, int x, int y) {
        JSONObject site = createPOIObject(siteId, x, y);
        discoveries.getJSONArray("sites").put(site);
        updateValidity();
    }

    //helper method to create POI JSON objects
    private JSONObject createPOIObject(String id, int x, int y) {
        JSONObject poi = new JSONObject();
        poi.put("id", id);
        poi.put("x", x);
        poi.put("y", y);
        return poi;
    }

    //data access methods
    public JSONArray getCreeks() {
        return discoveries.getJSONArray("creeks");
    }

    public JSONArray getSites() {
        return discoveries.getJSONArray("sites");
    }

    public JSONObject getDiscoveries() {
        return discoveries;
    }

    public boolean isValid() {
        return isValid;
    }

    //gets the closest creek to the site
    public String getClosestCreekToSite() {
        if (!isValid) {
            return "Insufficient Data";
        }

        JSONObject site = getSites().getJSONObject(0);
        int siteX = site.getInt("x");
        int siteY = site.getInt("y");

        String closestCreekId = null;
        double minDistance = Double.MAX_VALUE;

        for (int i = 0; i < getCreeks().length(); i++) {
            JSONObject creek = getCreeks().getJSONObject(i);
            double distance = calculateDistance(
                siteX, siteY,
                creek.getInt("x"),
                creek.getInt("y")
            );

            if (distance < minDistance) {
                minDistance = distance;
                closestCreekId = creek.getString("id");
            }
        }

        return closestCreekId;
    }

    //helper method to calculate distance
    private double calculateDistance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    public String presentDiscoveries() {
        StringBuilder report = new StringBuilder();
        
        //format creek information
        report.append("Creeks Found: ");
        if (getCreeks().length() == 0) {
            report.append("Insufficient data\n");
        } else {
            report.append("\n");
            for (int i = 0; i < getCreeks().length(); i++) {
                JSONObject creek = getCreeks().getJSONObject(i);
                report.append(formatPOIEntry(creek, "  "));
            }
        }
        
        //format site information
        report.append("Emergency Site: ");
        if (getSites().length() == 0) {
            report.append("Insufficient data\n");
        } else {
            report.append("\n").append(formatPOIEntry(getSites().getJSONObject(0), "  "));
        }

        //add closest creek information
        report.append("Closest creek to site: ")
              .append(getClosestCreekToSite())
              .append("\n");
        
        return report.toString();
    }

    //helper method to format POI entries
    private String formatPOIEntry(JSONObject poi, String indent) {
        return String.format("%s- ID: %s, X: %d, Y: %d\n",
            indent,
            poi.getString("id"),
            poi.getInt("x"),
            poi.getInt("y")
        );
    }

    //validation methods
    private void updateValidity() {
        isValid = (getCreeks().length() > 0 && getSites().length() > 0);
    }
}
