package com.example.sherrychuang.splitsmart.Activity;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.sherrychuang.splitsmart.R;

/**
 * Modified by jenny on 11/29/16.
 */

public class BillContentPage extends AppCompatActivity {
    private ListView myList;
    private BillAdapter myAdapter;
    private ArrayList<ItemInput> myItems;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_content_page_layout);

        myItems = new ArrayList<ItemInput>();

        myList = (ListView) findViewById(R.id.MyList);
        myList.setItemsCanFocus(true);
        myAdapter = new BillAdapter();
        myList.setAdapter(myAdapter);

        //Add a bill_content_item manually
        final Button addButton = (Button) findViewById(R.id.addBtn);
        addButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                addNewItem();
                myAdapter.notifyDataSetChanged();

                //scroll to the bottom of list so that user see the last record
                myList.post(new Runnable(){
                    public void run() {
                        myList.setSelection(myList.getCount() - 1);
                    }
                });
            }
        });


        //Click save button
        final Button save = (Button) findViewById(R.id.setup_macroSavebtn);
        save.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                for(int i = 0; i < myItems.size(); i++){
                    //TODO: insert to Item table
                    String itemI = myItems.get(i).getItemName();
                    String priceI = myItems.get(i).getPrice();
                    boolean taxI = myItems.get(i).getTax();
                    Toast.makeText(getBaseContext(), "Tax: " + taxI + " Item: " + itemI + " Price: " + priceI, Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Click cancel button
        final Button cancel = (Button) findViewById(R.id.setup_macroCancelbtn);
        cancel.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //Go back to Event page
                Intent myIntent = new Intent(v.getContext(), EventPage.class);
                startActivityForResult(myIntent, 0);
            }
        });
    }

    //Add new list view item
    public void addNewItem() {
        ItemInput itemInput = new ItemInput(false, "", "");
        myItems.add(itemInput);
    }

    //My custom adapter for the listview
    public class BillAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public BillAdapter() {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return myItems.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.bill_contect_item, null);
                holder.itemName = (EditText) convertView.findViewById(R.id.ItemName);
                holder.itemPrice = (EditText) convertView.findViewById(R.id.ItemPrice);
                holder.taxSelect = (CheckBox) convertView.findViewById(R.id.TaxCheckbox);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            //Mock People
            String[] items = new String[] {"Alice", "Jenny", "Louis", "Jeff", "Sherry", "Aaron", "Chiao", "Ning", "Chia-i"};
            //Use a Spinner to select one value from items
            final Spinner spinner = (Spinner) convertView.findViewById(R.id.spinner2);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(BillContentPage.this, android.R.layout.simple_spinner_item,items);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);

            //Fill EditText with the value you have in data source
            ItemInput myThisItem = myItems.get(position);
            holder.itemName.setText(myThisItem.getItemName());
            holder.itemName.setId(position);
            holder.itemPrice.setText(myThisItem.getPrice());
            holder.itemPrice.setId(position);

            //Check the tax check box
            holder.taxSelect.setChecked(myThisItem.getTax());
            holder.taxSelect.setId(position);

            //Update adapter once we finish with editing
            holder.itemName.setOnFocusChangeListener(new OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus){
                        final int position = v.getId();
                        final EditText Caption = (EditText) v;
                        ItemInput myThisItem = myItems.get(position);
                        String editResult = Caption.getText().toString();
                        myThisItem.setItemName(editResult);
                        Toast.makeText(getBaseContext(), "Name " + position + " is " + editResult, Toast.LENGTH_SHORT).show();
                    }
                }
            });

            holder.itemPrice.setOnFocusChangeListener(new OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus){
                        final int position = v.getId();
                        final EditText Caption = (EditText) v;
                        ItemInput myThisItem = myItems.get(position);
                        String editResult = Caption.getText().toString();
                        myThisItem.setPrice(editResult);
                        Toast.makeText(getBaseContext(), "Price " + position + " is " + editResult, Toast.LENGTH_SHORT).show();
                    }
                }
            });

            holder.taxSelect.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    final int position = v.getId();
                    final CheckBox cb = (CheckBox) v;
                    ItemInput myThisItem = myItems.get(position);
                    boolean checkRes = cb.isChecked();
                    myThisItem.setTax(checkRes);
                    Toast.makeText(getBaseContext(), "Tax " + position + " is " + checkRes, Toast.LENGTH_SHORT).show();
                }
            });

            return convertView;
        }
    }

    class ViewHolder {
        CheckBox taxSelect;
        EditText itemName;
        EditText itemPrice;
    }
}