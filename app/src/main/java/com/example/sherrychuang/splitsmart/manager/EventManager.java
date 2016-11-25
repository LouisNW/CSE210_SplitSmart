package com.example.sherrychuang.splitsmart.manager;

import com.example.sherrychuang.splitsmart.data.Event;

import java.util.List;

/**
 * Created by Louis on 11/23/16.
 */

public interface EventManager {
    public List<Event> getAllEvents();

    /**
     * @param eventID
     * @return null when the bill does not exist
     */
    public Event getEvent(long eventID);
    public boolean deleteEvent(long eventID);

    /**
     * Updating a non-existing event will fail.
     * @param event to update
     * @return boolean indicating whether the update succeeded
     */
    public boolean updateEvent(Event event);

    /**
     * Inserting an existing event has no effect.
     * @param event to insert
     * @return the inserted event with an id assigned by the DB; null when failing
     */
    public Event insertEvent(Event event);
}
