package com.example.sherrychuang.splitsmart.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import java.util.ArrayList;

import com.example.sherrychuang.splitsmart.R;

/**
 * Created by sherrychuang on 11/15/16.
 */

public class BillContentPage extends AppCompatActivity {

    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    ArrayList<ItemTest> listItems = new ArrayList<ItemTest>();

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    CustAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_layout);

        adapter=new CustAdapter(this, listItems);
        ListView listView = (ListView) findViewById(android.R.id.list);

        listView.setAdapter(adapter);

        listView.setAdapter(adapter);

        ItemTest newItem = new ItemTest(true, "Apple", "1");
        adapter.add(newItem);
    }

    /***
    //METHOD WHICH WILL HANDLE DYNAMIC INSERTION
    public void addItems(View v) {
        listItems.add(0.1, "");
        adapter.notifyDataSetChanged();
    }
     ***/
}
