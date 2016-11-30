package com.example.sherrychuang.splitsmart.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.view.WindowManager;

import java.util.List;
import java.util.ArrayList;


import com.example.sherrychuang.splitsmart.R;
import com.example.sherrychuang.splitsmart.data.*;
import com.example.sherrychuang.splitsmart.manager.*;
import com.example.sherrychuang.splitsmart.manager.ManagerFactory;

/**
 * Created by Chihhung on 11/28/16.
 */

public class EditBillPage extends Activity{
    private EventManager eventManager;
    private BillManager billManager;
    private PersonManager personManager;
    private Event event;
    private Bill bill;
    private List<Person> p_list;
    private Spinner spinner;

    private EditText billNameView;
    private EditText taxRateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.edit_bill_dialog_layout);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        event = (Event) intent.getSerializableExtra("Event");
        bill = (Bill) intent.getSerializableExtra("Bill");

        billNameView = (EditText) findViewById(R.id.bill_name);
        billNameView.setText(bill.getName());
        taxRateView = (EditText) findViewById(R.id.tax_rate);
        taxRateView.setText(String.valueOf(bill.getTaxRate()));

        eventManager = ManagerFactory.getEventManager(this);
        billManager = ManagerFactory.getBillManager(this);
        personManager = ManagerFactory.getPersonManager(this);
        p_list = personManager.getAllPersonsOfEvent(event.getId());
        ArrayList<String> spinnerArray = new ArrayList<>();
        int owner_index = 0;
        for(int i = 0; i < p_list.size(); i++) {
            if (p_list.get(i) == personManager.getPerson(bill.getOwnerID())) {
                spinner.setSelection(i);
            }
            spinnerArray.add(p_list.get(i).getName());
            System.out.println(owner_index);
            spinner = (Spinner) findViewById(R.id.owner);
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerArray);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setSelection(owner_index);
            spinner.setAdapter(spinnerArrayAdapter);

            Button okButton = (Button) findViewById(R.id.ok);
            okButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    String billName = billNameView.getText().toString();
                    String taxRate = taxRateView.getText().toString();
                    long owner_id = p_list.get(spinner.getSelectedItemPosition()).getId();
                    if (!billName.isEmpty() && !taxRate.isEmpty()) {
                        bill.setName(billName);
                        bill.setTaxRate(Double.parseDouble(taxRate));
                        bill.setOwnerID(owner_id);
                        billManager.updateBill(bill);
                    }
                    System.out.println(bill.getOwnerID());
                    Intent myIntent = new Intent(view.getContext(), EventPage.class);
                    myIntent.putExtra("Event", event);
                    startActivityForResult(myIntent, 0);
                }
            });

            Button cancelButton = (Button) findViewById(R.id.cancel);
            cancelButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Intent myIntent = new Intent(view.getContext(), EventPage.class);
                    myIntent.putExtra("Event", event);
                    startActivityForResult(myIntent, 0);
                }
            });
        }
    }
}
