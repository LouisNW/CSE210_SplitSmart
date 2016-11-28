package com.example.sherrychuang.splitsmart.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import java.util.List;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sherrychuang.splitsmart.R;
import com.example.sherrychuang.splitsmart.data.*;
import com.example.sherrychuang.splitsmart.manager.*;

import java.util.ArrayList;

/**
 * Created by sherrychuang on 11/15/16.
 */

public class EventPage extends Activity {
    private ListView billListView;
    private ArrayAdapter adapter;
    private List<Bill> bills;
    private List<String> billsName;
    private BillManager billManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.event_page_layout);

        Event event = (Event)getIntent().getSerializableExtra("Event");
        System.out.println(event.getName());
        billListView = (ListView) findViewById(R.id.bill_list);
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

}

