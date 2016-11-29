package com.example.sherrychuang.splitsmart.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import java.util.List;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.DialogInterface;
import android.app.AlertDialog;

import com.example.sherrychuang.splitsmart.R;
import com.example.sherrychuang.splitsmart.data.*;
import com.example.sherrychuang.splitsmart.manager.*;

import java.util.ArrayList;

/**
 * Created by sherrychuang on 11/15/16.
 */

public class EventPage extends AppCompatActivity {
    private ListView billListView;
    private ArrayAdapter adapter;
    private List<Bill> bills;
    private List<String> billsName;
    private BillManager billManager;
    private PersonManager personManager;
    private List<Person> people;
    private Event event;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.event_page_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        event = (Event)intent.getSerializableExtra("Event");
        TextView description = (TextView)findViewById(R.id.description);
        String text = "Date  : " + event.getStartDate().getMonth() + "/" + event.getStartDate().getDay() + " - " +
                      event.getEndDate().getMonth() + "/" + event.getEndDate().getDay() + "\n";

        description.setText(text);

        getSupportActionBar().setTitle(event.getName());
        billListView = (ListView) findViewById(R.id.bill_list);
        personManager = ManagerFactory.getPersonManager(this);
        people = personManager.getAllPersonsOfEvent(event.getId());
        billManager = ManagerFactory.getBillManager(this);
        bills = billManager.getAllBillsOfEvent(event.getId());
        billsName = new ArrayList<>();
        for(int i = 0; i < bills.size(); i++) {
            billsName.add(bills.get(i).getName());
        }

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, billsName);
        billListView.setAdapter(adapter);
        registerForContextMenu(billListView);
        billListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CharSequence text = billsName.get(i);
                Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_event_page, menu);
        return true;
    }
    public void clickMenuItem(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.addBill) {
            final CharSequence[] items = {"Camera", "Gallery", "Manual", "Cancel"};

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setItems(items, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int i) {
                    if (items[i].equals("Camera")) {
                        // Go to Camera
                        Toast toast = Toast.makeText(getApplicationContext(), items[i], Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    else if (items[i].equals("Gallery")) {
                        // Go to gallery
                        Toast toast = Toast.makeText(getApplicationContext(), items[i], Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    else if (items[i].equals("Manual")) {
                        // manually input
                        Toast toast = Toast.makeText(getApplicationContext(), items[i], Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    else if (items[i].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        }
        else if (itemId == R.id.addPerson) {
            String pName = "";
            for(int i = 0; i < people.size(); i++) {
                pName = pName + people.get(i).getName() + " ";
            }
        }
        else if (itemId == android.R.id.home) {
            Intent myIntent = new Intent(EventPage.this, MainActivity.class);
            EventPage.this.startActivity(myIntent);
        }
        return;
    }
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.bill_list) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle(billsName.get(info.position));
            menu.add(Menu.NONE, 0, Menu.NONE, "Edit");
            menu.add(Menu.NONE, 1, Menu.NONE, "Delete");
        }
    }
    public boolean onContextItemSelected(MenuItem menuItem) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuItem.getMenuInfo();
        int menuItemIndex = menuItem.getItemId();
        if (menuItemIndex == 0) {
            // choose "Edit"
            Intent myIntent = new Intent(EventPage.this, EditBillPage.class);
            myIntent.putExtra("Event", event);
            myIntent.putExtra("Bill", bills.get(info.position));
            EventPage.this.startActivity(myIntent);
        }
        else {
            // choose "Delete"
            billManager.deleteBill(bills.get(info.position).getId());
            bills.remove(bills.get(info.position));
            adapter.remove(adapter.getItem(info.position));
            Toast toast = Toast.makeText(getApplicationContext(), "deleted " + Integer.toString(info.position), Toast.LENGTH_SHORT);
            toast.show();
        }
        return true;
    }
}
