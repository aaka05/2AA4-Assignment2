package ca.mcmaster.se2aa4.island.team31;

import org.json.JSONArray;
import org.json.JSONObject;


//singleton pattern for tracking island discoveries
public class Report {
    private static Report instance = null;  
    private JSONObject discoveries;         
    private boolean isValid;               

    //constructor
    private Report() {
        discoveries = new JSONObject();
        discoveries.put("creeks", new JSONArray());
        discoveries.put("sites", new JSONArray());
        isValid = false;
    }

    //singleton getter
    public static Report getInstance() {
        if (instance == null) {
            instance = new Report();
        }
        return instance;
    }

    //add methods
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

    //helper method for creating POIs
    private JSONObject createPOI(String id, int x, int y) {
        JSONObject poi = new JSONObject();
        poi.put("id", id);
        poi.put("x", x);
        poi.put("y", y);
        return poi;
    }

    //getter methods 
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

    //distance calculation
    private double calculateDistance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    //find closest creek to emergency site
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

    //display all discoveries
    public String displayDiscoveries() {
        StringBuilder report = new StringBuilder();
        report.append(createHeader())
              .append(createCreeksSection())
              .append(createEmergencySiteSection())
              .append(createClosestCreekSection());
        return report.toString();
    }

    private String createHeader() {
        return "\n=== Island Exploration Report ===\n\n";
    }

    private String createCreeksSection() {
        StringBuilder section = new StringBuilder();
        section.append("CREEKS FOUND:\n");
        section.append("-------------\n");
        
        if (getCreeks().length() == 0) {
            section.append("   No creeks discovered yet\n");
        } else {
            for (int i = 0; i < getCreeks().length(); i++) {
                section.append(formatPOI(getCreeks().getJSONObject(i)));
            }
        }
        section.append("\n");
        return section.toString();
    }

    private String createEmergencySiteSection() {
        StringBuilder section = new StringBuilder();
        section.append("EMERGENCY SITE:\n");
        section.append("--------------\n");
        
        if (getSites().length() == 0) {
            section.append("   No emergency site found yet\n");
        } else {
            section.append(formatPOI(getSites().getJSONObject(0)));
        }
        section.append("\n");
        return section.toString();
    }

    private String createClosestCreekSection() {
        StringBuilder section = new StringBuilder();
        section.append("NEAREST CREEK TO EMERGENCY SITE:\n");
        section.append("------------------------------\n");
        section.append("   ").append(getClosestCreekToSite()).append("\n\n");
        return section.toString();
    }

    private String formatPOI(JSONObject poi) {
        return String.format("   * %s at coordinates (%d, %d)\n",
                poi.getString("id"),
                poi.getInt("x"),
                poi.getInt("y"));
    }

    private void updateValidity() {
        isValid = getCreeks().length() > 0 && getSites().length() > 0;
    }
}

