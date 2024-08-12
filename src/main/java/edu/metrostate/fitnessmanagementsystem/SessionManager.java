package edu.metrostate.fitnessmanagementsystem;

public class SessionManager {
    private static TrainerData currentTrainer;
    private static ClientData currentClient;
    public static String username;

    public static TrainerData getCurrentTrainer() {
        return currentTrainer;
    }

    public static void setCurrentTrainer(TrainerData trainer) {
        currentTrainer = trainer;
    }

    public static ClientData getCurrentClient() {
        return currentClient;
    }

    public static void setCurrentClient(ClientData client) {
        currentClient = client;
    }

}
