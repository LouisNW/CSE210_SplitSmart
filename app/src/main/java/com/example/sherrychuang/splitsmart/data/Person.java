package com.example.sherrychuang.splitsmart.data;

import java.io.Serializable;

/**
 * Created by sherrychuang on 11/15/16.
 */

public class Person implements Serializable {
    private long id;
    private String name;
    private String email;
    private long eventID;

    public Person(String name, String email, long eventID) {
        this.name = name;
        this.email = email;
        this.eventID = eventID;
    }

    public Person() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getEventID() {
        return eventID;
    }

    public void setEventID(long eventID) {
        this.eventID = eventID;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", eventID=" + eventID +
                '}';
    }
}
