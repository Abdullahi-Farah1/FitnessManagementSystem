package edu.metrostate.fitnessmanagementsystem;

public class ClientData {
    private Integer id;
    private String clientId;
    private String name;
    private String email;
    private String username;
    private String password;
    private String address;
    private String gender;
    private Integer phoneNum;
    private String status;


    private String trainerId;

    public ClientData(Integer id, String clientId, String name, String username, String password, String address, String gender, Integer phoneNum, String status, String trainerId) {
        this.id = id;
        this.clientId = clientId;
        this.name = name;
        this.username = username;
        this.password = password;
        this.address = address;
        this.gender = gender;
        this.phoneNum = phoneNum;
        this.status = status;
        this.trainerId = trainerId;
    }


    public Integer getId() {
        return id;
    }

    public String getClientId() {
        return clientId;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getAddress() {
        return address;
    }

    public String getGender() {
        return gender;
    }

    public Integer getPhoneNum() {
        return phoneNum;
    }

    public String getStatus() {
        return status;
    }

    public String getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(String trainerId) {
        this.trainerId = trainerId;
    }
}
