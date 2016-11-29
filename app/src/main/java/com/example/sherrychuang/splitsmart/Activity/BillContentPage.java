package com.example.sherrychuang.splitsmart.Activity;

<<<<<<< HEAD
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
=======
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
>>>>>>> 9b0216ee996c21b1d348eb29b91dd6db9a180a59
import android.widget.ListView;
import java.util.ArrayList;

import com.example.sherrychuang.splitsmart.R;

/**
 * Created by sherrychuang on 11/15/16.
 */

<<<<<<< HEAD
public class BillContentPage extends AppCompatActivity {
=======
public class BillContentPage extends ListActivity{
>>>>>>> 9b0216ee996c21b1d348eb29b91dd6db9a180a59
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
<<<<<<< HEAD
        listView.setAdapter(adapter);
=======
        setListAdapter(adapter);
>>>>>>> 9b0216ee996c21b1d348eb29b91dd6db9a180a59

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
