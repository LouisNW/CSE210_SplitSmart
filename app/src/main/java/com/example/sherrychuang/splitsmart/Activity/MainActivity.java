package com.example.sherrychuang.splitsmart.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Menu;
import android.view.ContextMenu;
import android.view.MenuItem;
import java.util.*;

import com.example.sherrychuang.splitsmart.data.*;
import com.example.sherrychuang.splitsmart.manager.*;
import com.example.sherrychuang.splitsmart.R;
import com.example.sherrychuang.splitsmart.model.CreateSampleDB;
import com.example.sherrychuang.splitsmart.model.DatabaseTest;


/**
 * Modified by Chiao Fu on 11/27/16.
 */

public class MainActivity extends AppCompatActivity {
    private ListView eventListView;
    private EventAdapter adapter;
    private List<Event> events;
    private List<String> eventsName;
    private EventManager eventManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        new CreateSampleDB(getApplicationContext()).EraseAll();
//        new CreateSampleDB(getApplicationContext()).Run();

        eventListView = (ListView) findViewById(R.id.event_list);
        eventManager = ManagerFactory.getEventManager(this);
        events = eventManager.getAllEvents();
        eventsName = new ArrayList<>();
        for(int i = 0; i < events.size(); i++) {
            eventsName.add(events.get(i).getName());
        }

        adapter = new EventAdapter(this, eventsName);
        eventListView.setAdapter(adapter);
        registerForContextMenu(eventListView);
        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent myIntent = new Intent(MainActivity.this, EventPage.class);
                myIntent.putExtra("Event", events.get(i));
                MainActivity.this.startActivity(myIntent);
            }
        });
    }
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.event_list) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle(eventsName.get(info.position));
            menu.add(Menu.NONE, 0, Menu.NONE, "Edit");
            menu.add(Menu.NONE, 1, Menu.NONE, "Delete");
        }
    }
    public boolean onContextItemSelected(MenuItem menuItem) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuItem.getMenuInfo();
        int menuItemIndex = menuItem.getItemId();
        if (menuItemIndex == 0) {
            // choose "Edit"
            Intent myIntent = new Intent(MainActivity.this, EditEventPage.class);
            myIntent.putExtra("Event", events.get(info.position));
            MainActivity.this.startActivity(myIntent);
//            Toast toast = Toast.makeText(getApplicationContext(), Integer.toString(menuItemIndex), Toast.LENGTH_SHORT);
//            toast.show();
        }
        else {
            // choose "Delete"
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater= this.getLayoutInflater();
            View layout = inflater.inflate(R.layout.delete_warning_dialog_layout, null);
            alertDialogBuilder.setView(layout);
            final AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

            final TextView warning_text = (TextView) layout.findViewById(R.id.warning_message);
            warning_text.setText("Are you sure you want to delete \"" + events.get(info.position).getName() + "\" ? ");
            Button okButton = (Button) layout.findViewById(R.id.ok);
            okButton.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                    eventManager.deleteEvent(events.get(info.position).getId());
                    events.remove(events.get(info.position));
                    adapter.remove(adapter.getItem(info.position));
                    alertDialog.dismiss();
                }
            });
            // click cancel
            Button cancelButton = (Button) layout.findViewById(R.id.cancel);
            cancelButton.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                    alertDialog.dismiss();
                }
            });

        }
        return true;

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
    public void onBackPressed() {
        Intent myIntent = new Intent(MainActivity.this, MainPage.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// clear back stack
        startActivity(myIntent);
        finish();
        return;
    }

}
