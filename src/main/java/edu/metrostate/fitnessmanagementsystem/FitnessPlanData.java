package edu.metrostate.fitnessmanagementsystem;

public class FitnessPlanData {
    private String clientId;
    private String plan;

    public FitnessPlanData(String clientId, String plan) {
        this.clientId = clientId;
        this.plan = plan;
    }

    public String getClientId() {
        return clientId;
    }

    public String getPlan() {
        return plan;
    }
}
