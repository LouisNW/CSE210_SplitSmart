package com.example.sherrychuang.splitsmart.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.WindowManager;

import java.util.List;

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
    private Event event;
    private Bill bill;
    private List<Person> p_list;

    private EditText billNameView;
    private EditText taxRateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.edit_bill_layout);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        event = (Event) intent.getSerializableExtra("Event");
        bill = (Bill) intent.getSerializableExtra("Bill");

        billNameView = (EditText) findViewById(R.id.bill_name);
        taxRateView = (EditText) findViewById(R.id.tax_rate);

        billNameView.setText(bill.getName());
        taxRateView.setText(String.valueOf(bill.getTaxRate()));

        eventManager = ManagerFactory.getEventManager(this);
        billManager = ManagerFactory.getBillManager(this);


        Button okButton = (Button) findViewById(R.id.ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String billName = billNameView.getText().toString();
                String taxRate = taxRateView.getText().toString();
                if (!billName.isEmpty() && !taxRate.isEmpty()) {
                    bill.setName(billName);
                    bill.setTaxRate(Double.parseDouble(taxRate));
                    billManager.updateBill(bill);
                }
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
