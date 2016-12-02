package com.example.sherrychuang.splitsmart.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.lang.String;

import com.example.sherrychuang.splitsmart.R;
import com.example.sherrychuang.splitsmart.data.*;
import com.example.sherrychuang.splitsmart.manager.*;


/**
 * Created by sherrychuang on 11/15/16.
 */

public class BillPage extends Activity {
    private List<Person> person;
    private List<String> personlist;
    private PersonManager personManager;
    private BillManager billManager;
    private Event event;
    private String billName;


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
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        //Disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(false);

        //Get event id and people
        event = (Event)intent.getSerializableExtra("Event");
        billManager = ManagerFactory.getBillManager(this);
        personManager = ManagerFactory.getPersonManager(this);
        person = personManager.getAllPersonsOfEvent(event.getId());
        personlist = new ArrayList<String>();
        for(int i = 0; i < person.size(); i++) {
            String personName = person.get(i).getName();
            personlist.add(i,personName);
        }

        //Use a Spinner to select one value from items
        final Spinner spinner = (Spinner) alertLayout.findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(BillPage.this, android.R.layout.simple_spinner_item,personlist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //Set a button for CANCEL
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getBaseContext(), "Cancel clicked", Toast.LENGTH_SHORT).show();
                //Back to the Event page
                finish();
            }
        });

        //Set a button for OK
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                billName = etBillName.getText().toString();
                int selectPos = spinner.getSelectedItemPosition();
                long ownerID = person.get(selectPos).getId();
                boolean checkFlag = false;
                double initTax = 0.0;
                Bill bill2Insert = new Bill(billName, person.get(selectPos).getId(), initTax, event.getId());
                if(billName.length() > 0){
                    checkFlag = false;
                    if (bill2Insert != null) {
                        bill2Insert.setName(billName);
                        bill2Insert.setOwnerID(ownerID);
                        bill2Insert.setTaxRate(initTax);
                        bill2Insert.setEventID(event.getId());
                        Bill billCheck = billManager.insertBill(bill2Insert);
                        billManager.setSharingPersonsOfBill(bill2Insert, person); //set people in the bill

                        if(billCheck != null){
                            System.out.print("Bill ID: " + billCheck.getId());
                        }else{
                            System.out.print("Bill is empty");
                        }
                    } else {
                        System.out.println("Null object reference");
                    }
                }else{
                    checkFlag = true;
                    //Error message dialog
                    LayoutInflater inflater= getLayoutInflater();
                    View layout = inflater.inflate(R.layout.bill_warning_layout, null);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(BillPage.this);
                    alertDialogBuilder.setView(layout);
                    alertDialogBuilder.setCancelable(false);
                    final AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                    final TextView warning_text = (TextView) layout.findViewById(R.id.warning_message);
                    warning_text.setText("Bill Name is empty!");
                    Button okButton = (Button) layout.findViewById(R.id.ok);
                    okButton.setOnClickListener(new View.OnClickListener(){
                        public void onClick(View view){
                            alertDialog.dismiss();
                            //Go back to the Event page
                            finish();
                        }
                    });
                }

                if(checkFlag == false) {
                    Intent myIntent = new Intent(((Dialog) dialog).getContext(), BillContentPage.class);
                    myIntent.putExtra("Bill", bill2Insert);
                    myIntent.putExtra("Event", event);
                    BillPage.this.startActivity(myIntent);
                    Toast.makeText(getBaseContext(), "Bill Name: " + billName + " Pos: " + selectPos, Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        AlertDialog dialog = alert.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        dialog.show();
    }

}
