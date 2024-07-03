package edu.metrostate.fitnessmanagementsystem;

public class TrainerData {
    private Integer id;
    private String trainerId;
    private String name;
    private String username;
    private String password;
    private String address;
    private String gender;
    private Integer phoneNum;
    private String status;

    public TrainerData(Integer id, String trainerId, String name, String username,
                        String password, String address,
                        String gender,Integer phoneNum, String status) {
        this.id = id;
        this.trainerId = trainerId;
        this.name = name;
        this.username = username;
        this.password = password;
        this.address = address;
        this.gender = gender;
        this.phoneNum = phoneNum;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public String getTrainerId() {
        return trainerId;
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



}
