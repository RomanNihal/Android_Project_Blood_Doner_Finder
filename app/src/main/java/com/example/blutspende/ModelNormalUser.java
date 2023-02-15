package com.example.blutspende;

public class ModelNormalUser {
    private String username, password, phone, division,district,bloodGroup,day,month,year,pictureLink;

    public ModelNormalUser() {
    }

    public ModelNormalUser(String username, String password, String phone, String division, String district, String bloodGroup, String day, String month, String year, String pictureLink) {
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.division = division;
        this.district = district;
        this.bloodGroup = bloodGroup;
        this.day = day;
        this.month = month;
        this.year = year;
        this.pictureLink = pictureLink;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public String getDivision() {
        return division;
    }

    public String getDistrict() {
        return district;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public String getDay() {
        return day;
    }

    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
    }

    public String getPictureLink() {
        return pictureLink;
    }
}