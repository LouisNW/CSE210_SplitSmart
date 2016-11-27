package com.example.sherrychuang.splitsmart.Activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sherrychuang.splitsmart.R;
import com.example.sherrychuang.splitsmart.data.Event;
import com.example.sherrychuang.splitsmart.data.EventDate;
import com.example.sherrychuang.splitsmart.manager.EventManager;
import com.example.sherrychuang.splitsmart.manager.ManagerFactory;

import java.util.Calendar;

/**
 * Created by sherrychuang on 11/15/16.
 */

public class CreateEventPage extends Activity {
    private String eventName;
    private EventDate startDate;
    private EventDate endDate;
    private EventManager eventManager;

    private EditText eventNameView;
    private TextView startDateView;
    private TextView endDateView;

    private Calendar calendar;
    private DatePicker startDatePicker;
    private DatePicker datePicker;
    private DatePickerDialog.OnDateSetListener startDatePickListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            startDateView.setText("Start: " + day + "/" + (month + 1) + "/" + year);
            startDate.setMonth(month + 1);
            startDate.setDay(day);
        }
    };
    private DatePickerDialog.OnDateSetListener endDatePickListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            endDateView.setText("Start: " + day + "/" + (month + 1) + "/" + year);
            endDate.setMonth(month + 1);
            endDate.setDay(day);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.create_event_layout);

        eventNameView = (EditText) findViewById(R.id.event_name);
        startDateView = (TextView) findViewById(R.id.start_date);
        endDateView = (TextView) findViewById(R.id.end_date);

        eventManager = ManagerFactory.getEventManager(this);

        // default show date of today
        calendar = Calendar.getInstance();
        int year, month, day;
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
//        startDateView.setText(new StringBuilder().append(day).append("/").append(month).append("/").append(year));
        startDateView.setText("Start: " + day + "/" + (month + 1) + "/" + year);
        endDateView.setText("End: " + day + "/" + (month + 1) + "/" + year);
        startDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CreateEventPage.this, startDatePickListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
                Toast.makeText(getApplicationContext(), "start", Toast.LENGTH_SHORT).show();
            }
        });
        endDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CreateEventPage.this, endDatePickListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
                Toast.makeText(getApplicationContext(), "end", Toast.LENGTH_SHORT).show();
            }
        });

        startDate = new EventDate(month, day);
        endDate = new EventDate(month, day);

//        startDatePicker = (DatePicker) findViewById(R.id.start_date);

        Button okButton = (Button) findViewById(R.id.ok);
        okButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                eventName  = eventNameView.getText().toString();
                eventManager.insertEvent(new Event(eventName, startDate, endDate));
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




//        //TODO: if you think 3 buttons looks too ugly, you can modify it to:
//        // click 'ok' button to launch widget with 3 options
//        Button camerabtn = (Button) findViewById(R.id.camera);
//        camerabtn.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View view){
//                //TODO: launch camera activity
//            }
//
//        });
//
//        Button gallerybtn = (Button) findViewById(R.id.gallery);
//        gallerybtn.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View view){
//                //TODO: launch gallery
//            }
//
//        });
//
//        Button manualbtn = (Button) findViewById(R.id.manual);
//        manualbtn.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View view){
//                Intent myIntent = new Intent(view.getContext(), BillPage.class);
//                startActivityForResult(myIntent,0);
//            }
//
//        });

    }
}
