package com.example.sherrychuang.splitsmart.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cunoraz.tagview.Tag;
import com.cunoraz.tagview.TagView;
import com.example.sherrychuang.splitsmart.R;
import com.example.sherrychuang.splitsmart.data.Event;
import com.example.sherrychuang.splitsmart.data.EventDate;
import com.example.sherrychuang.splitsmart.data.Person;
import com.example.sherrychuang.splitsmart.manager.EventManager;
import com.example.sherrychuang.splitsmart.manager.ManagerFactory;
import com.example.sherrychuang.splitsmart.manager.PersonManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Created by Chiao on 2016/11/28.
 */

public class EditEventPage extends Activity {
    private Event event;
    private List<Person> people;
    private List<Integer> peopleStatus;
    private EventManager eventManager;
    private PersonManager personManager;

    private EditText eventNameView;
    private TextView startDateView;
    private TextView endDateView;
    private EditText personNameView;
    private EditText personEmailView;
    private TagView peopleView;

    private DatePickerDialog.OnDateSetListener startDatePickListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            startDateView.setText("Start Date: " + (month + 1) + "/" + day);
            event.setStartDate(new EventDate(month + 1, day));
        }
    };
    private DatePickerDialog.OnDateSetListener endDatePickListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            endDateView.setText("End Date: " + (month + 1) + "/" + day);
            event.setEndDate(new EventDate(month + 1, day));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event_layout);
        Intent intent = getIntent();
        event = (Event)intent.getSerializableExtra("Event");

        eventNameView = (EditText) findViewById(R.id.event_name);
        startDateView = (TextView) findViewById(R.id.start_date);
        endDateView = (TextView) findViewById(R.id.end_date);
        peopleView = (TagView) findViewById(R.id.people);

        eventManager = ManagerFactory.getEventManager(this);
        personManager = ManagerFactory.getPersonManager(this);
        people = personManager.getAllPersonsOfEvent(event.getId());
        peopleStatus = new ArrayList<Integer>(Collections.nCopies(people.size(), 0));

        // show people of event
        for(int i = 0; i < people.size(); i++) {
            // add a new tag in page
            Tag tempTag = new Tag(people.get(i).getName());
            tempTag.layoutColor = Color.GRAY;
            tempTag.isDeletable = true;
            peopleView.addTag(tempTag);
        }
        peopleView.setOnTagDeleteListener(new TagView.OnTagDeleteListener() {
            @Override
            public void onTagDeleted(final TagView view, final Tag tag, final int position) {
                view.remove(position);
                peopleStatus.set(position, -1);
                Toast.makeText(getApplicationContext(), "#people " + Integer.toString(people.size()), Toast.LENGTH_SHORT).show();
            }
        });

        // show name of event
        eventNameView.setText(event.getName());
        eventNameView.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_SHORT).show();
                event.setName(eventNameView.getText().toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        // show date of event
        startDateView.setText("Start Date: " + event.getStartDate().getMonth() + "/" + event.getStartDate().getDay());
        endDateView.setText("End Date: " + event.getEndDate().getMonth() + "/" + event.getEndDate().getDay());
        startDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EditEventPage.this, startDatePickListener, Calendar.getInstance().get(Calendar.YEAR), event.getStartDate().getMonth() - 1, event.getStartDate().getDay()).show();
                Toast.makeText(getApplicationContext(), "start", Toast.LENGTH_SHORT).show();
            }
        });
        endDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EditEventPage.this, endDatePickListener, Calendar.getInstance().get(Calendar.YEAR), event.getEndDate().getMonth() - 1, event.getEndDate().getDay()).show();
                Toast.makeText(getApplicationContext(), "end", Toast.LENGTH_SHORT).show();
            }
        });


        ImageButton addFriendButton = (ImageButton) findViewById(R.id.add_person);
        addFriendButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                // show add friend dialog
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditEventPage.this);
                LayoutInflater inflater= EditEventPage.this.getLayoutInflater();
                View layout = inflater.inflate(R.layout.add_friend_dialog_layout, null);
                alertDialogBuilder.setView(layout);
//                alertDialogBuilder.setView(R.layout.add_friend_dialog_layout);
                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.setTitle( "Add Friend" );
                alertDialog.show();

                personNameView = (EditText) layout.findViewById(R.id.person_name);
                personEmailView = (EditText) layout.findViewById(R.id.person_email);

                Button okButton = (Button) layout.findViewById(R.id.ok);
                okButton.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View view){
                        String tempName = personNameView.getText().toString();
                        String tempEmail = personEmailView.getText().toString();
                        if (tempName.length() > 0 && tempEmail.length() > 0) {
                            // add a new tag in page
                            Tag tempTag = new Tag(tempName);
                            tempTag.layoutColor = Color.GRAY;
                            tempTag.isDeletable = true;
                            peopleView.addTag(tempTag);
                            // add person into array
                            people.add(new Person(tempName, tempEmail, event.getId()));
                            peopleStatus.add(1);
                        }
                        Toast.makeText(getApplicationContext(), "#friend " + Integer.toString(people.size()), Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }
                });

                Button cancelButton = (Button) layout.findViewById(R.id.cancel);
                cancelButton.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View view){
                        Toast.makeText(getApplicationContext(), "add friend cancel", Toast.LENGTH_SHORT).show();
                        alertDialog.cancel();
                    }
                });

                Toast.makeText(getApplicationContext(), "add friend", Toast.LENGTH_SHORT).show();
            }
        });

        Button okButton = (Button) findViewById(R.id.ok);
        okButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                // update event
                eventManager.updateEvent(event);
                // update people
                for(int i = 0; i < people.size(); i++) {
                    switch (peopleStatus.get(i)) {
                        case 1:
                            // add a new person
                            personManager.insertPerson(people.get(i));
                            break;
                        case -1:
                            // remove a person
                            personManager.deletePerson(people.get(i).getId());
                            break;
                    }
                }
                Toast.makeText(getApplicationContext(), "OK!", Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(view.getContext(), MainActivity.class);
                startActivityForResult(myIntent,0);
            }
        });

        Button cancelButton = (Button) findViewById(R.id.cancel);
        cancelButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent myIntent = new Intent(view.getContext(), MainActivity.class);
                startActivityForResult(myIntent,0);
            }
        });

    }
}
