package com.example.sherrychuang.splitsmart.data;

import com.example.sherrychuang.splitsmart.manager.ManagerFactory;
import com.example.sherrychuang.splitsmart.manager.PersonManager;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sherrychuang on 11/15/16.
 */

public class Event implements Serializable {
    private long id;
    private String name;
    private EventDate startDate;
    private EventDate endDate;

    public Event(String name, EventDate startDate, EventDate endDate) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;

    }

    public Event() {}

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

    public EventDate getStartDate() {
        return startDate;
    }

    public void setStartDate(EventDate startDate) {
        this.startDate = startDate;
    }

    public EventDate getEndDate() {
        return endDate;
    }

    public void setEndDate(EventDate endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
