package com.example.sherrychuang.splitsmart.Activity;

import android.content.Intent;
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
    private EditText personNameView;
    private EditText personEmailView;
    private Bill bill;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.event_page_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        event = (Event)intent.getSerializableExtra("Event");
        TextView description = (TextView)findViewById(R.id.description);
        String text = event.getStartDate().getMonth() + "/" + event.getStartDate().getDay() + " - " +
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
                CharSequence text = personManager.getPerson(bills.get(i).getOwnerID()).getName();
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
                        dialog.cancel();
                    }
                }
            });
            builder.show();
        }
        else if (itemId == R.id.addPerson) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater= this.getLayoutInflater();
            View layout = inflater.inflate(R.layout.add_friend_dialog_layout, null);
            alertDialogBuilder.setView(layout);
            final AlertDialog alertDialog = alertDialogBuilder.create();
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
//                        Tag tempTag = new Tag(tempName);
//                        tempTag.layoutColor = Color.GRAY;
//                        tempTag.isDeletable = true;
//                        peopleView.addTag(tempTag);
//                        // add person into array
//                        peopleName.add(personNameView.getText().toString());
//                        peopleEmail.add(personEmailView.getText().toString());
                        Person p = new Person(tempName, tempEmail, event.getId());
                        personManager.insertPerson(p);
                    }

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
            menu.add(Menu.NONE, 0, Menu.NONE, "Edit this bill");
            menu.add(Menu.NONE, 1, Menu.NONE, "Delete this bill");
        }
    }
    private EditText billNameView;
    private EditText taxRateView;
    public boolean onContextItemSelected(MenuItem menuItem) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuItem.getMenuInfo();
        int menuItemIndex = menuItem.getItemId();
        if (menuItemIndex == 0) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater= this.getLayoutInflater();
            View layout = inflater.inflate(R.layout.edit_bill_dialog_layout, null);
            alertDialogBuilder.setView(layout);
            final AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

            billNameView = (EditText) layout.findViewById(R.id.bill_name);
            System.out.println(billNameView);

            bill = bills.get(info.position);
            billNameView.setText(bill.getName());
            taxRateView = (EditText) layout.findViewById(R.id.tax_rate);
            taxRateView.setText(String.valueOf(bill.getTaxRate()));

            ArrayList<String> spinnerArray = new ArrayList<>();
            for (int i = 0; i < people.size(); i++) {
                spinnerArray.add(people.get(i).getName());
            }
            final Spinner spinner = (Spinner) layout.findViewById(R.id.owner);
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerArray);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spinnerArrayAdapter);

            Button okButton = (Button) layout.findViewById(R.id.ok);
            okButton.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                    String tempBillName = billNameView.getText().toString();
                    String tempTaxRate = taxRateView.getText().toString();
                    long owner_id = people.get(spinner.getSelectedItemPosition()).getId();
                    if (tempBillName.length() > 0 && tempTaxRate.length() > 0) {
                        bill.setName(tempBillName);
                        bill.setTaxRate(Double.parseDouble(tempTaxRate));
                        bill.setOwnerID(owner_id);
                        billManager.updateBill(bill);
                    }
                    alertDialog.dismiss();
                }
            });

            Button cancelButton = (Button) layout.findViewById(R.id.cancel);
            cancelButton.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                    alertDialog.dismiss();
                }
            });
        }
        else {
            // choose "Delete"
            billManager.deleteBill(bills.get(info.position).getId());
            bills.remove(bills.get(info.position));
            adapter.remove(adapter.getItem(info.position));
        }
        return true;
    }
}
