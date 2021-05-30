package com.example.trail.model.pin;

import java.io.Serializable;

public class PinDTO implements Serializable {
    public int id;
    public String time;
    // or
    public double logTime;

    public String type;


    // this goes into SQLite db

}
