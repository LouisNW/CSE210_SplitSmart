package com.example.sherrychuang.splitsmart.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import java.util.List;

/**
 * Created by sherrychuang on 11/15/16.
 */

public class CreateEventPage extends Activity {
    private String eventName;
    private EventDate startDate;
    private EventDate endDate;
    private EventManager eventManager;
    private PersonManager personManager;

    private EditText eventNameView;
    private TextView startDateView;
    private TextView endDateView;
    private EditText personNameView;
    private EditText personEmailView;
    private TagView peopleView;

    private Calendar calendar;
    private DatePickerDialog.OnDateSetListener startDatePickListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            startDateView.setText("Start Date: " + (month + 1) + "/" + day + "/" + year);
            startDate.setMonth(month + 1);
            startDate.setDay(day);
        }
    };
    private DatePickerDialog.OnDateSetListener endDatePickListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            endDateView.setText("End Date: " + (month + 1) + "/" + day + "/" + year);
            endDate.setMonth(month + 1);
            endDate.setDay(day);
        }
    };

    private List<String> peopleName;
    private List<String> peopleEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event_layout);

        eventNameView = (EditText) findViewById(R.id.event_name);
        startDateView = (TextView) findViewById(R.id.start_date);
        endDateView = (TextView) findViewById(R.id.end_date);
        peopleView = (TagView) findViewById(R.id.people);
        peopleView.setOnTagDeleteListener(new TagView.OnTagDeleteListener() {
            @Override
            public void onTagDeleted(final TagView view, final Tag tag, final int position) {
                view.remove(position);
                peopleName.remove(position);
                peopleEmail.remove(position);
                Toast.makeText(getApplicationContext(), "#people " + Integer.toString(peopleName.size()), Toast.LENGTH_SHORT).show();
            }
        });

        // default show date of today
        calendar = Calendar.getInstance();
        int year, month, day;
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        startDateView.setText("Start Date: " + (month + 1) + "/" + day + "/" + year);
        endDateView.setText("End Date: " + (month + 1) + "/" + day + "/" + year);
        startDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CreateEventPage.this, startDatePickListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                Toast.makeText(getApplicationContext(), "start", Toast.LENGTH_SHORT).show();
            }
        });
        endDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CreateEventPage.this, endDatePickListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                Toast.makeText(getApplicationContext(), "end", Toast.LENGTH_SHORT).show();
            }
        });

        startDate = new EventDate(month, day);
        endDate = new EventDate(month, day);
        peopleName = new ArrayList<String>();
        peopleEmail = new ArrayList<String>();

        eventManager = ManagerFactory.getEventManager(this);
        personManager = ManagerFactory.getPersonManager(this);

        ImageButton addFriendButton = (ImageButton) findViewById(R.id.add_person);
        addFriendButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                // show add friend dialog
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CreateEventPage.this);
                LayoutInflater inflater= CreateEventPage.this.getLayoutInflater();
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
                            peopleName.add(personNameView.getText().toString());
                            peopleEmail.add(personEmailView.getText().toString());
                        }
                        Toast.makeText(getApplicationContext(), "#friend " + Integer.toString(peopleName.size()), Toast.LENGTH_SHORT).show();
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
                // create a new event
                eventName = eventNameView.getText().toString();
                Event event = new Event(eventName, startDate, endDate);
                eventManager.insertEvent(event);

                // create people
                for(int i = 0; i < peopleName.size(); i++) {
                    personManager.insertPerson(new Person(peopleName.get(i), peopleEmail.get(i), event.getId()));
                }

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
