package com.example.trail.model.pin;

import java.io.Serializable;

public class PinDTO implements Serializable {
    public int id;
    public String placeName;
    public String pinTime;
    public String journeysId;
    public String category;
    public String note;
    public String image;
    public String longitude;
    public String latitude;
    public String status;   /** 0: 기록 O / 1: 기록 X */
    public String userId;
    public String userName;
    public String updatedAt;
    public String createdAt;

    public String type;

    public PinDTO() {
    }


    public PinDTO(String placeName, String pinTime, String journeysId, String longitude, String latitude, String status, String userId, String userName) {
        this.placeName = placeName;
        this.pinTime = pinTime;
        this.journeysId = journeysId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.status = status;
        this.userId = userId;
        this.userName = userName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPinTime() {
        return pinTime;
    }

    public void setPinTime(String pinTime) {
        this.pinTime = pinTime;
    }

    public String getJourneysId() {
        return journeysId;
    }

    public void setJourneysId(String journeysId) {
        this.journeysId = journeysId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    // this goes into SQLite db

}
