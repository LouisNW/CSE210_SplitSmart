package com.example.sherrychuang.splitsmart.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.String;
import java.lang.Long;

import com.example.sherrychuang.splitsmart.R;
import com.example.sherrychuang.splitsmart.data.*;
import com.example.sherrychuang.splitsmart.manager.*;


/**
 * Created by sherrychuang on 11/15/16.
 */

public class BillPage extends Activity {
    private String billName;
    private List<Person> person;
    private List<String> personlist;
    private String ownerName;
    private PersonManager personManager;
    private BillManager billManager;
    private Map<String, Long> map;
    private Event event;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.bill_page_layout);

        //Instantiates a layout XML file into the View object alertLayout
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.bill_dialog, null);
        final EditText etBillName = (EditText) alertLayout.findViewById(R.id.billName);

        //Creates a builder for an alert dialog that uses the default alert dialog theme
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Create a New Bill");
        //Disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(false);

        /***
        event = (Event)intent.getSerializableExtra("Event");
        personManager = ManagerFactory.getPersonManager(this);
        person = personManager.getAllPersonsOfEvent(event.getId());
        personlist = new ArrayList<>();
        map = new HashMap<String, Long>();
        for (int i = 0; i < 10; i++){
            personlist.add(person.get(i).getName());
            map.put(person.get(i).getName(), person.get(i).getId());
        }
         ***/

        String[] items = new String[] {"Alice", "Jenny", "Louis", "Jeff", "Sherry", "Aaron", "Chiao", "Ning", "Chia-i"};
        //Use a Spinner to select one value from items
        final Spinner spinner = (Spinner) alertLayout.findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(BillPage.this, android.R.layout.simple_spinner_item,items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //Set a button for cancel
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getBaseContext(), "Cancel clicked", Toast.LENGTH_SHORT).show();
                //Back to the previous page
                finish();
            }
        });

        //Set a button for OK
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                billName = etBillName.getText().toString();
                ownerName = spinner.getSelectedItem().toString();
                //billManager.insertBill(new Bill(billName,map.get(ownerName), 0, 1));
                Intent i  = new Intent(((Dialog)dialog).getContext(), BillContentPage.class);
                startActivity(i);
                Toast.makeText(getBaseContext(), "Bill Name: " +  billName + " Owner: " + ownerName, Toast.LENGTH_SHORT).show();
            }
        });

        //Set the view from XML inside AlertDialog
        alert.setView(alertLayout);

        AlertDialog dialog = alert.create();
        dialog.show();
    }

}

