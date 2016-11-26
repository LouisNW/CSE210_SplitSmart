package com.example.sherrychuang.splitsmart.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.view.Menu;
import android.view.ContextMenu;
import android.view.MenuItem;
import java.util.*;

import com.example.sherrychuang.splitsmart.data.*;
import com.example.sherrychuang.splitsmart.manager.*;
import com.example.sherrychuang.splitsmart.R;

public class MainActivity extends AppCompatActivity {
    private ListView event_list_view;
    private ArrayAdapter adapter;
    private List<Event> events;
    private String[] events_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        event_list_view = (ListView) findViewById(R.id.event_list);
        EventManager eventManager = ManagerFactory.getEventManager(this);
        events = eventManager.getAllEvents();
        events_name = new String[events.size()];
        for(int i = 0; i < events.size(); i++) {
            events_name[i] = events.get(i).getName();
        }

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, events_name);
        event_list_view.setAdapter(adapter);
        registerForContextMenu(event_list_view);
        event_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CharSequence text = Integer.toString(i);
                Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
                toast.show();
            }
        });

//        event_list_view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                CharSequence text = list_events[i];
//                Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
//                toast.show();
//                return true;
//            }
//        });

//        ImageButton addEvent= (ImageButton) findViewById(R.id.addEvent);
//        addEvent.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View view){
//                Intent myIntent = new Intent(MainActivity.this, CreateEventPage.class);
//                MainActivity.this.startActivity(myIntent);
//            }
//
//        });

//        Button btn= (Button) findViewById(R.id.sampleEvent);
//        btn.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View view){
//                Intent myIntent = new Intent(view.getContext(), EventPage.class);
//                startActivityForResult(myIntent,0);
//            }
//
//        });
    }
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.event_list) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle(events_name[info.position]);
            menu.add("Edit");
            menu.add("Delete");
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    public void clickMenuItem(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.addEvent) {
            Intent myIntent = new Intent(MainActivity.this, CreateEventPage.class);
            MainActivity.this.startActivity(myIntent);
        }
        return;
    }
}
