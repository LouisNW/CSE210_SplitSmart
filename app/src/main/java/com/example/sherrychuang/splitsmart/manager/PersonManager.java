package com.example.sherrychuang.splitsmart.manager;

import com.example.sherrychuang.splitsmart.data.Person;

import java.util.List;

/**
 * Created by Louis on 11/23/16.
 */

public interface PersonManager {
    /**
     * Effectively Event.getAttendingPersons()
     */
    public List<Person> getAllPersonsOfEvent(long eventID);

    /**
     * @param personID
     * @return null when the bill does not exist
     */
    public Person getPerson(long personID);
    public boolean deletePerson(long personID);

    /**
     * Updating a non-existing person will fail.
     * @param person to update
     * @return boolean indicating whether the update succeeded
     */
    public boolean updatePerson(Person person);

    /**
     * Inserting an existing person has no effect.
     * @param person to insert
     * @return the inserted person with an id assigned by the DB; null when failing
     */
    public Person insertPerson(Person person);
}
