package com.example.sherrychuang.splitsmart.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.example.sherrychuang.splitsmart.R;
import com.example.sherrychuang.splitsmart.data.Item;

/**
 * Created by sherrychuang on 11/15/16.
 */

public class BillContentPage extends AppCompatActivity {

    private EditText textItem;
    private EditText textPrice;
    private String strItem;
    private String strPrice;
    private List<ItemInput> itemInputs;
    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    private ArrayList<ItemInput> listItems;

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    private CustAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_layout);

        itemInputs = new ArrayList<ItemInput>();
        listItems = new ArrayList<ItemInput>();
        adapter = new CustAdapter(this, listItems);

        ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(adapter);

        //Add a item manually
        final Button button = (Button) findViewById(R.id.addBtn);
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                ItemInput item = new ItemInput(false, "", "");
                listItems.add(item);
                adapter.notifyDataSetChanged();
            }
        });


        for(int i =  0; i < 3; i++) {
            ItemInput newItem = new ItemInput(true, "Apple", "1");
            itemInputs.add(newItem);
            adapter.add(newItem);
        }

        View inflatedView = getLayoutInflater().inflate(R.layout.bill_item_row_layout, null);
        textItem = (EditText) inflatedView.findViewById(R.id.itemName);
        textItem.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(getBaseContext(), "Edit item Successfully", Toast.LENGTH_SHORT).show();
                strItem = textItem.getText().toString();
                textItem.getText().clear();
                textItem.setText(strItem);
                adapter.notifyDataSetChanged();
            }
        });
/***
        textItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    Toast.makeText(getBaseContext(), "Edit item Successfully", Toast.LENGTH_SHORT).show();
                    strItem = textItem.getText().toString();
                    textItem.getText().clear();
                    textItem.setText(strItem);
                    adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                    Toast.makeText(getBaseContext(), "Edit item Successfully", Toast.LENGTH_SHORT).show();
                    strItem = textItem.getText().toString();
                    textItem.getText().clear();
                    textItem.setText(strItem);
                    adapter.notifyDataSetChanged();
            }
        });

***/

        //Add a item manually
        final Button save = (Button) findViewById(R.id.setup_macroSavebtn);
        save.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Toast.makeText(getBaseContext(), "Item: " +  strItem + " Price " + strPrice, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
