package com.example.blutspende;

public class ModelRecyclerView {
   private String userName,phoneNumber,pictureLink,bloodGroup;

    public ModelRecyclerView(String userName, String phoneNumber, String pictureLink, String bloodGroup) {
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.pictureLink = pictureLink;
        this.bloodGroup = bloodGroup;
    }

    public ModelRecyclerView() {
    }

    public String getUserName() {
        return userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPictureLink() {
        return pictureLink;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }
}
