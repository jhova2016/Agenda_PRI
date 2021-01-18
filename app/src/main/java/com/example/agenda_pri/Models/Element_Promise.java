package com.example.agenda_pri.Models;

import android.widget.TextView;

public class Element_Promise {
    String Id;
    String Location;
    String EventDate;
    String Place;
    String Information;
    String Promised;
    String PromisedEstate;

    public Element_Promise(String id, String location, String eventDate, String place, String information, String promised, String promisedEstate) {
        Id = id;
        Location = location;
        EventDate = eventDate;
        Place = place;
        Information = information;
        Promised = promised;
        PromisedEstate = promisedEstate;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getEventDate() {
        return EventDate;
    }

    public void setEventDate(String eventDate) {
        EventDate = eventDate;
    }

    public String getPlace() {
        return Place;
    }

    public void setPlace(String place) {
        Place = place;
    }

    public String getInformation() {
        return Information;
    }

    public void setInformation(String information) {
        Information = information;
    }

    public String getPromised() {
        return Promised;
    }

    public void setPromised(String promised) {
        Promised = promised;
    }

    public String getPromisedEstate() {
        return PromisedEstate;
    }

    public void setPromisedEstate(String promisedEstate) {
        PromisedEstate = promisedEstate;
    }
}
