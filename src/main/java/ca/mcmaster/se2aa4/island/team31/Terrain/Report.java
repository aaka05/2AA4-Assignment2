package ca.mcmaster.se2aa4.island.team31.Terrain;


import org.json.JSONArray;
import org.json.JSONObject;

public class Report {
    private static Report instance = null;
    private JSONObject discoveries; // stores creeks and sites (ids, x and y coords)
    private boolean isValid; // indicates if report is viable (has at least one creek and site)

    private Report() {
        discoveries = new JSONObject();
        discoveries.put("creeks", new JSONArray());
        discoveries.put("sites", new JSONArray());
        isValid = false;
    }

    // Get the singleton instance of Report
    public static Report getInstance() {
        if (instance == null) {
            instance = new Report();
        }
        return instance;
    }

    // Add a creek with ID, x, and y position
    public void addCreek(String creekId, int x, int y) {
        JSONObject creek = new JSONObject();
        creek.put("id", creekId);
        creek.put("x", x);
        creek.put("y", y);
        
        discoveries.getJSONArray("creeks").put(creek);
        updateValidity(); // Check if report is valid
    }

    // Add a site with ID, x, and y position
    public void addSite(String siteId, int x, int y) {
        JSONObject site = new JSONObject();
        site.put("id", siteId);
        site.put("x", x);
        site.put("y", y);
        
        discoveries.getJSONArray("sites").put(site);
        updateValidity(); // Check if report is valid
    }

    // Retrieve all creeks as a JSONArray
    public JSONArray getCreeks() {
        return discoveries.getJSONArray("creeks");
    }

    // Retrieve all sites as a JSONArray
    public JSONArray getSites() {
        return discoveries.getJSONArray("sites");
    }

    // Retrieve the entire discoveries object
    public JSONObject getDiscoveries() {
        return discoveries;
    }

    // Check if there is at least one creek and one site
    private void updateValidity() {
        isValid = (getCreeks().length() > 0 && getSites().length() > 0);
    }

    // Check if the report is valid
    public boolean isValid() {
        return isValid;
    }

    public String getClosestCreekToSite() {
        // checks if at least one creek and site is in report
        if (!isValid) {
            return "Insufficient Data";
        }

        JSONArray creeks = getCreeks();
        JSONArray sites = getSites();

        JSONObject site = sites.getJSONObject(0);
        int siteX = site.getInt("x");
        int siteY = site.getInt("y");

        String closestCreekId = null;
        double minDistance = Double.MAX_VALUE;

        for (int i = 0; i < creeks.length(); i++) {
            JSONObject creek = creeks.getJSONObject(i);
            int creekX = creek.getInt("x");
            int creekY = creek.getInt("y");

            // computing distance from creek to site
            double distance = Math.sqrt(Math.pow(siteX - creekX, 2) + Math.pow(siteY - creekY, 2));

            if (distance < minDistance) {
                minDistance = distance;
                closestCreekId = creek.getString("id");
            }
        }

        return closestCreekId;
    }

    public String presentDiscoveries() {
        StringBuilder report = new StringBuilder();
    
        // Fetch discoveries
        JSONArray creeks = getCreeks();
        JSONArray sites = getSites();
    
        // Format creek information
        report.append("Creeks Found: ");
        if (creeks.length() == 0) {
            report.append("Insufficient data\n");
        } else {
            report.append("\n");
            for (int i = 0; i < creeks.length(); i++) {
                JSONObject creek = creeks.getJSONObject(i);
                report.append(String.format("  - ID: %s, X: %d, Y: %d\n",
                        creek.getString("id"),
                        creek.getInt("x"),
                        creek.getInt("y")));
            }
        }
    
        // Format site information
        report.append("Emergency Site: ");
        if (sites.length() == 0) {
            report.append("Insufficient data\n");
        } else {
            JSONObject site = sites.getJSONObject(0);
            report.append(String.format("\n  - ID: %s, X: %d, Y: %d\n",
                    site.getString("id"),
                    site.getInt("x"),
                    site.getInt("y")));
        }

        String closestCreek = getClosestCreekToSite();
        report.append("Closest creek to site: ").append(closestCreek).append("\n");
    
        return report.toString();
    }
    
}
