package com.example.sherrychuang.splitsmart.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import java.util.List;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.DialogInterface;
import android.app.AlertDialog;

import com.cunoraz.tagview.Tag;
import com.cunoraz.tagview.TagView;

import com.example.sherrychuang.splitsmart.R;
import com.example.sherrychuang.splitsmart.data.*;
import com.example.sherrychuang.splitsmart.manager.*;

import java.util.ArrayList;


/**
 * Created by sherrychuang on 11/15/16.
 */

public class EventPage extends AppCompatActivity {
    private ListView billListView;
    private EventAdapter adapter;
    private List<Bill> bills;
    private List<String> billsName;
    private BillManager billManager;
    private PersonManager personManager;
    private List<Person> people;
    private Event event;
    private EditText personNameView;
    private EditText personEmailView;
    private Bill bill;
    private TagView tagGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // initialize some default stuff, get all the manager, data
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.event_page_layout);
        event = (Event)intent.getSerializableExtra("Event");
        personManager = ManagerFactory.getPersonManager(this);
        people = personManager.getAllPersonsOfEvent(event.getId());
        billManager = ManagerFactory.getBillManager(this);
        bills = billManager.getAllBillsOfEvent(event.getId());

        // get and set action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(event.getName());

        // get the date text view
        TextView dateTextView = (TextView)findViewById(R.id.date);
        String text = event.getStartDate().getMonth() + "/" + event.getStartDate().getDay() + " - " +
                      event.getEndDate().getMonth() + "/" + event.getEndDate().getDay();
        dateTextView.setText(text);

        // tag view of participants
        tagGroup = (TagView) findViewById(R.id.people_tag);
        ArrayList<Tag> pNameTag = new ArrayList<>();
        for (int i = 0; i < people.size(); i++) {
            Tag tag = new Tag(people.get(i).getName());
            tag.layoutColor = Color.GRAY;
            tag.isDeletable = false;
            tag.tagTextSize = 15;
            tag.radius = 15;
            pNameTag.add(tag);
        }
        tagGroup.addTags(pNameTag);


        // get and display the bill list
        billListView = (ListView) findViewById(R.id.bill_list);
        billsName = new ArrayList<>();
        for(int i = 0; i < bills.size(); i++) {
            billsName.add(bills.get(i).getName());
        }

        adapter = new EventAdapter(this, billsName);
        billListView.setAdapter(adapter);
        registerForContextMenu(billListView);

        // setup bill list onclick listener
        billListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CharSequence text = personManager.getPerson(bills.get(i).getOwnerID()).getName();
                //Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
                //toast.show();

                // go to edit bill content page
                Intent myIntent = new Intent(EventPage.this, EditBillContentPage.class);
                myIntent.putExtra("Bill", bills.get(i));
                EventPage.this.startActivity(myIntent);

                return;
            }
        });

        // setup split button listener
        final Button split_button = (Button) findViewById(R.id.split);
        split_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "go to split smart!", Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(EventPage.this, SplitResultPage.class);
                myIntent.putExtra("Event", event);
                EventPage.this.startActivity(myIntent);
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_event_page, menu);
        return true;
    }

    // click menu bar icon on top of the screen
    public void clickMenuItem(MenuItem item) {
        int itemId = item.getItemId();
        // click create bill icon on top, generate a dialog for user to choose camera, gallery, manual, or cancel
        if (itemId == R.id.addBill) {
            final CharSequence[] items = {"Camera", "Manual", "Cancel"};

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setItems(items, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int i) {
                    // click camera
                    if (items[i].equals("Camera")) {
                        // Go to Camera
                        Intent myIntent = new Intent(EventPage.this, OcrCaptureActivity.class);
                        myIntent.putExtra("event", event);
                        EventPage.this.startActivity(myIntent);
                        //Toast toast = Toast.makeText(getApplicationContext(), items[i], Toast.LENGTH_SHORT);
                        //toast.show();
                    }

                    // click manual
                    else if (items[i].equals("Manual")) {
                        // directly go to createBillPage
                        //Toast toast = Toast.makeText(getApplicationContext(), items[i], Toast.LENGTH_SHORT);
                        //toast.show();
                        Intent myIntent = new Intent(EventPage.this, BillPage.class);
                        myIntent.putExtra("Event", event);
                        EventPage.this.startActivity(myIntent);
                    }
                    // click cancel
                    else if (items[i].equals("Cancel")) {
                        dialog.cancel();
                    }
                }
            });
            builder.show();
        }
        // click add person
        else if (itemId == R.id.addPerson) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater= this.getLayoutInflater();
            View layout = inflater.inflate(R.layout.add_friend_dialog_layout, null);
            alertDialogBuilder.setView(layout);
            final AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

            personNameView = (EditText) layout.findViewById(R.id.person_name);
            personEmailView = (EditText) layout.findViewById(R.id.person_email);

            // ok button listener
            Button okButton = (Button) layout.findViewById(R.id.ok);
            okButton.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                    String tempName = personNameView.getText().toString();
                    String tempEmail = personEmailView.getText().toString();
                    if (tempName.length() > 0 && tempEmail.length() > 0) {
                        // update tag
                        Tag tag = new Tag(tempName);
                        tag.layoutColor = Color.GRAY;
                        tag.isDeletable = false;
                        tag.tagTextSize = 15;
                        tag.radius = 15;
                        tagGroup.addTag(tag);
                        // update people list
                        Person p = new Person(tempName, tempEmail, event.getId());
                        people.add(p);
                        // update database
                        personManager.insertPerson(p);
                    }
                    alertDialog.dismiss();
                }
            });
            // cancel button listener
            Button cancelButton = (Button) layout.findViewById(R.id.cancel);
            cancelButton.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                    //Toast.makeText(getApplicationContext(), "add friend cancel", Toast.LENGTH_SHORT).show();
                    alertDialog.cancel();
                }
            });
        }
        else if (itemId == android.R.id.home) {
            Intent myIntent = new Intent(EventPage.this, MainActivity.class);
            EventPage.this.startActivity(myIntent);
        }
        return;
    }

    // add things into menu bar
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.bill_list) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.add(Menu.NONE, 0, Menu.NONE, "Edit this bill");
            menu.add(Menu.NONE, 1, Menu.NONE, "Delete this bill");
        }
    }

    private EditText billNameView;
    private EditText taxRateView;

    //long click on bill listener
    public boolean onContextItemSelected(MenuItem menuItem) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuItem.getMenuInfo();
        int menuItemIndex = menuItem.getItemId();

        // click edit
        if (menuItemIndex == 0) {
            // build alert dialog and get everything needed
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater= this.getLayoutInflater();
            View layout = inflater.inflate(R.layout.edit_bill_dialog_layout, null);
            alertDialogBuilder.setView(layout);
            final AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            billNameView = (EditText) layout.findViewById(R.id.bill_name);
            bill = bills.get(info.position);
            billNameView.setText(bill.getName());
            taxRateView = (EditText) layout.findViewById(R.id.tax_rate);
            taxRateView.setText(String.valueOf(bill.getTaxRate()));
            ArrayList<String> spinnerArray = new ArrayList<>();
            int owner_pos = 0;
            for (int i = 0; i < people.size(); i++) {
                spinnerArray.add(people.get(i).getName());
                if (bill.getOwnerID() == people.get(i).getId()) {
                    owner_pos = i;
                }
            }
            final Spinner spinner = (Spinner) layout.findViewById(R.id.owner);
            ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerArray);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spinnerArrayAdapter);
            spinner.setSelection(owner_pos);

            // click ok
            Button okButton = (Button) layout.findViewById(R.id.ok);
            okButton.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                    // get text field and spinner data
                    String tempBillName = billNameView.getText().toString();
                    String tempTaxRate = taxRateView.getText().toString();
                    long owner_id = people.get(spinner.getSelectedItemPosition()).getId();
                    if (tempBillName.length() > 0 && tempTaxRate.length() > 0) {
                        // update database
                        bill.setName(tempBillName);
                        bill.setTaxRate(Double.parseDouble(tempTaxRate));
                        bill.setOwnerID(owner_id);
                        billManager.updateBill(bill);

                        // update adapter
                        int bill_index = info.position;
                        bills.set(bill_index, bill);
                        billsName.set(bill_index, tempBillName);
                        adapter = new EventAdapter(getApplicationContext(), billsName);
                        billListView.setAdapter(adapter);
                    }
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
        // click delete
        else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater= this.getLayoutInflater();
            View layout = inflater.inflate(R.layout.delete_warning_dialog_layout, null);
            alertDialogBuilder.setView(layout);
            final AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

            final TextView warning_text = (TextView) layout.findViewById(R.id.warning_message);
            warning_text.setText("Are you sure you want to delete \"" + bills.get(info.position).getName() + "\" ? ");
            Button okButton = (Button) layout.findViewById(R.id.ok);
            okButton.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                    billManager.deleteBill(bills.get(info.position).getId());
                    bills.remove(bills.get(info.position));
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
}
