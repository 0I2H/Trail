package com.example.trail.model.message;

import com.example.trail.model.pin.PinDTO;
import com.example.trail.model.trail.TrailDTO;

import java.io.Serializable;

public class MessageDTO implements Serializable {
    public boolean uploadSuccess;

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
