package ca.mcmaster.se2aa4.island.team31;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Report class - Singleton pattern for tracking island discoveries
 */
public class Report {
    private static Report instance = null;  // Singleton instance
    private JSONObject discoveries;         // Stores all POIs
    private boolean isValid;                // Tracks if we have both creeks and sites

    // Constructor
    private Report() {
        discoveries = new JSONObject();
        discoveries.put("creeks", new JSONArray());
        discoveries.put("sites", new JSONArray());
        isValid = false;
    }

    // Singleton getter
    public static Report getInstance() {
        if (instance == null) {
            instance = new Report();
        }
        return instance;
    }

    // Add methods
    public void addCreek(String id, int x, int y) {
        JSONObject creek = createPOI(id, x, y);
        discoveries.getJSONArray("creeks").put(creek);
        updateValidity();
    }

    public void addSite(String id, int x, int y) {
        JSONObject site = createPOI(id, x, y);
        discoveries.getJSONArray("sites").put(site);
        updateValidity();
    }

    // Helper method for creating POIs
    private JSONObject createPOI(String id, int x, int y) {
        JSONObject poi = new JSONObject();
        poi.put("id", id);
        poi.put("x", x);
        poi.put("y", y);
        return poi;
    }

    // Getters
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

    // Distance calculation
    private double calculateDistance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    // Find closest creek to emergency site
    public String getClosestCreekToSite() {
        if (!isValid) {
            return "No valid data available";
        }

        JSONObject site = getSites().getJSONObject(0);
        int siteX = site.getInt("x");
        int siteY = site.getInt("y");

        String closestCreek = null;
        double minDistance = Double.MAX_VALUE;

        for (int i = 0; i < getCreeks().length(); i++) {
            JSONObject creek = getCreeks().getJSONObject(i);
            double distance = calculateDistance(siteX, siteY, 
                                             creek.getInt("x"), 
                                             creek.getInt("y"));
            if (distance < minDistance) {
                minDistance = distance;
                closestCreek = creek.getString("id");
            }
        }

        return closestCreek;
    }

    // Display all discoveries
    public String displayDiscoveries() {
        StringBuilder report = new StringBuilder();
        
        report.append("Creeks Found: \n");
        if (getCreeks().length() == 0) {
            report.append("  None found\n");
        } else {
            for (int i = 0; i < getCreeks().length(); i++) {
                report.append(formatPOI(getCreeks().getJSONObject(i)));
            }
        }
        
        report.append("Emergency Site: \n");
        if (getSites().length() == 0) {
            report.append("  None found\n");
        } else {
            report.append(formatPOI(getSites().getJSONObject(0)));
        }

        report.append("Closest creek: ").append(getClosestCreekToSite()).append("\n");
        return report.toString();
    }

    private String formatPOI(JSONObject poi) {
        return String.format("  - %s at (%d,%d)\n",
                poi.getString("id"),
                poi.getInt("x"),
                poi.getInt("y"));
    }

    private void updateValidity() {
        isValid = getCreeks().length() > 0 && getSites().length() > 0;
    }
}

