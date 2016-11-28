package com.example.sherrychuang.splitsmart.Activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sherrychuang.splitsmart.R;
import com.example.sherrychuang.splitsmart.data.Event;
import com.example.sherrychuang.splitsmart.data.EventDate;
import com.example.sherrychuang.splitsmart.data.Person;
import com.example.sherrychuang.splitsmart.manager.EventManager;
import com.example.sherrychuang.splitsmart.manager.ManagerFactory;
import com.example.sherrychuang.splitsmart.manager.PersonManager;

import java.util.Calendar;

/**
 * Created by Chiao on 2016/11/28.
 */

public class EditEventPage extends Activity {
    private Event event;
    private EventManager eventManager;
    private PersonManager personManager;

    private EditText eventNameView;
    private TextView startDateView;
    private TextView endDateView;

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

        eventManager = ManagerFactory.getEventManager(this);
        personManager = ManagerFactory.getPersonManager(this);

        // default show name of event
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

        // default show date of event
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


        ImageButton addFriendButton = (ImageButton) findViewById(R.id.addPerson);
        addFriendButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Toast.makeText(getApplicationContext(), "add friend", Toast.LENGTH_SHORT).show();
            }
        });

        Button okButton = (Button) findViewById(R.id.ok);
        okButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                eventManager.updateEvent(event);
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
