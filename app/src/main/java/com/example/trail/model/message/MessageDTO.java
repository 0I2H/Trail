package com.example.trail.model.message;

import com.example.trail.model.pin.PinDTO;
import com.example.trail.model.trail.TrailDTO;

import java.io.Serializable;

public class MessageDTO implements Serializable {
    public boolean uploadSuccess;

    public boolean registerSuccess;

    public String comment;

    public String message;

    public TrailDTO journey;

    public PinDTO place;

    public MessageDTO() {
    }

    public boolean isUploadSuccess() {
        return uploadSuccess;
    }

    public void setUploadSuccess(boolean uploadSuccess) {
        this.uploadSuccess = uploadSuccess;
    }

    public boolean isRegisterSuccess() {
        return registerSuccess;
    }

    public void setRegisterSuccess(boolean registerSuccess) {
        this.registerSuccess = registerSuccess;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TrailDTO getJourney() {
        return journey;
    }

    public void setJourney(TrailDTO journey) {
        this.journey = journey;
    }

    public PinDTO getPlace() {
        return place;
    }

    public void setPlace(PinDTO place) {
        this.place = place;
    }
}
