package edu.metrostate.fitnessmanagementsystem;

public class UserSession {

    private static UserSession instance;

    private String trainerId;
    private String clientId; // Add this to store the client ID

    private UserSession() {
        // Private constructor to restrict instantiation
    }

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public String getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(String trainerId) {
        this.trainerId = trainerId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void clearSession() {
        trainerId = null;
        clientId = null; // Clear client ID as well
    }
}