package edu.metrostate.fitnessmanagementsystem;

public class UserSession {

    private static UserSession instance;

    private String trainerId;

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

    public void clearSession() {
        trainerId = null;
    }
}
