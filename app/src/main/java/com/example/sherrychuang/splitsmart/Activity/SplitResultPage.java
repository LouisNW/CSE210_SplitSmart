package com.example.sherrychuang.splitsmart.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.sherrychuang.splitsmart.Calculator;
import com.example.sherrychuang.splitsmart.R;
import com.example.sherrychuang.splitsmart.data.Event;

import java.util.List;

/**
 * Created by sherrychuang on 11/15/16.
 */


public class SplitResultPage extends AppCompatActivity {
    private ListView resultListView;
    private List<String> Result;
    private ArrayAdapter adapter;
    static boolean flag = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.split_result_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        resultListView = (ListView) findViewById(R.id.result_list);


        final Event event = (Event)intent.getSerializableExtra("Event");
        Calculator cal = new Calculator(getApplicationContext(),event.getId());
        if(flag) {Result = cal.getResult_pay();}
        else {Result = cal.getResult_final();}
        flag = !flag;
        Button result_button = (Button) findViewById(R.id.result);
        result_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(SplitResultPage.this, SplitResultPage.class);
                myIntent.putExtra("Event", event);
                SplitResultPage.this.startActivity(myIntent);
            }
        });
        adapter = new ArrayAdapter(this, R.layout.result_listview, Result);
        resultListView.setAdapter(adapter);
        registerForContextMenu(resultListView);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_result_page, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void clickMenuItem(MenuItem item) {
        int itemId = item.getItemId();
        //不會成功QQ
        //System.err.println("test3");
        if (itemId == android.R.id.home) {
            Intent myIntent = new Intent(SplitResultPage.this, EventPage.class);
            SplitResultPage.this.startActivity(myIntent);
        }
        return;
    }

}
