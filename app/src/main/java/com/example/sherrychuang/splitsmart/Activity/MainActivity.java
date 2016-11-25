package com.example.sherrychuang.splitsmart.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import java.util.*;
import com.example.sherrychuang.splitsmart.data.*;

import com.example.sherrychuang.splitsmart.R;

public class MainActivity extends AppCompatActivity {
    private ListView event_list;
    private ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        event_list = (ListView) findViewById(R.id.event_list);
        String[] list_events = new String[3];
        list_events[0] = "Fuck";
        list_events[1] = "Shit";
        list_events[2] = "Damn";

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list_events);
        event_list.setAdapter(adapter);

        event_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CharSequence text = Integer.toString(i);
                Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        ImageButton addEvent= (ImageButton) findViewById(R.id.addEvent);
        addEvent.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent myIntent = new Intent(MainActivity.this, CreateEventPage.class);
                MainActivity.this.startActivity(myIntent);
            }

        });

//        Button btn= (Button) findViewById(R.id.sampleEvent);
//        btn.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View view){
//                Intent myIntent = new Intent(view.getContext(), EventPage.class);
//                startActivityForResult(myIntent,0);
//            }
//
//        });
    }
}
