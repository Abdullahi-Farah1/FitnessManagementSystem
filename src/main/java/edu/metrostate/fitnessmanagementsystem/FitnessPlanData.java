package edu.metrostate.fitnessmanagementsystem;

public class FitnessPlanData {

    private Integer id;
    private String clientId;
    private String plan;

    public FitnessPlanData(Integer id, String clientId, String plan) {
        this.clientId = clientId;
        this.plan = plan;
    }

    public Integer getId() {
        return id;
    }

    public String getClientId() {
        return clientId;
    }

    public String getPlan() {
        return plan;
    }
}
