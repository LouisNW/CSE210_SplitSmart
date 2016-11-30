package com.example.sherrychuang.splitsmart.Activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sherrychuang.splitsmart.R;
import com.thomashaertel.widget.MultiSpinner;

/**
 * Modified by jenny on 11/29/16.
 * Description: The billContentPage shows the name, price, tax, person to shared of each item by
 * getting data from image-parse-to-text or manually inputs. Items will then be stored after users
 * clicked on the "SAVE" button; otherwise, items will not be stored and it will go back to the
 * Event Page.
 */

public class BillContentPage extends AppCompatActivity {
    private ListView myList;
    private BillAdapter myAdapter;
    private List<ItemInput> myItems;
    private MultiSpinner spinner;
    private ArrayAdapter<String> adapter;
    private String[] peopleList;
    private String taxRateBill;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_content_page_layout);


        //Get items from the image-parse-to-text
        myItems = new ArrayList<ItemInput>();
        /***
        myItems = (ArrayList<ItemInput>)getIntent().getSerializableExtra("ItemListExtra");

        //Go back to the Event Page if myItems is empty
        if(myItems.size() <= 0){
            Intent i = new Intent(BillContentPage.this, EventPage.class) ;
            startActivity (i);
            BillContentPage.this.finish();
            Toast.makeText(getBaseContext(), "Empty Item for set up this bill", Toast.LENGTH_SHORT).show();
        }
         ***/

        myList = (ListView) findViewById(R.id.MyList);
        myList.setItemsCanFocus(true);
        myAdapter = new BillAdapter();
        myList.setAdapter(myAdapter);

        //Click the SAVE button
        final Button save = (Button) findViewById(R.id.setup_macroSavebtn);
        save.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                for(ItemInput myItem: myItems) {
                    //TODO: insert to Item table
                    String itemI = myItem.getItemName();
                    String priceI = myItem.getPrice();
                    boolean taxI = myItem.getTax();
                    Toast.makeText(getBaseContext(), "Tax: " + taxI + " Item: " + itemI + " Price: " + priceI, Toast.LENGTH_SHORT).show();
                }

                //Get the tax rate and store it to db in Bill
                EditText taxRate = (EditText) findViewById(R.id.ItemTax);
                taxRateBill = taxRate.getText().toString();
                Toast.makeText(getBaseContext(), "Tax rate: " + taxRateBill, Toast.LENGTH_SHORT).show();
                //TODO: inset tax rate to DB
            }
        });

        //Click cancel button and go back to Event Page
        final Button cancel = (Button) findViewById(R.id.setup_macroCancelbtn);
        cancel.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //Go back to the Event page
                Intent myIntent = new Intent(v.getContext(), EventPage.class);
                startActivityForResult(myIntent, 0);
            }
        });
    }

    //Add a new ListView item
    public void addNewItem() {
        ItemInput itemInput = new ItemInput(false, "", "");
        myItems.add(itemInput);
    }

    //Custom adapter for the ListView
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
            //Use ViewHolder to avoid frequent call of findViewById() during ListView scrolling,
            //and that will make it smooth
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

            final int delPos = position;
            //Delete an item
            final ImageButton del = (ImageButton) convertView.findViewById(R.id.deleteBtn);
            del.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    myItems.remove(delPos);
                    notifyDataSetChanged();
                }
            });

            //TODO: get people from DB
            //Mock People
            peopleList = new String[] {"Alice", "Jenny", "Louis", "Jeff", "Sherry", "Aaron", "Chiao", "Ning", "Chia-i"};
            //Use a MultiSpinner to select people to share
            adapter = new ArrayAdapter<String>(BillContentPage.this, android.R.layout.simple_spinner_item);
            for(String person : peopleList) {
                adapter.add(person);
            }

            //Get spinner and set adapter
            spinner = (MultiSpinner) convertView.findViewById(R.id.spinner2);
            spinner.setAdapter(adapter, false, onSelectedListener);

            //Set initial selection
            boolean[] selectedItems = new boolean[adapter.getCount()];
            selectedItems[0] = true; // select first item
            spinner.setSelected(selectedItems);

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

    //Action after select whom to share
    private MultiSpinner.MultiSpinnerListener onSelectedListener = new MultiSpinner.MultiSpinnerListener() {
        public void onItemsSelected(boolean[] selected) {
            for(int i = 0; i < selected.length; i++) {
                if(selected[i] == true) {
                    String personName = peopleList[i];
                    Toast.makeText(getBaseContext(), personName + " is selected", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    class ViewHolder {
        CheckBox taxSelect;
        EditText itemName;
        EditText itemPrice;
    }

    //Show Item via +1 icon in the menu
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.add_item_icon, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Add Item via +1 icon in the menu
    public void clickMenuItem(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.action_add) {
            addNewItem();
            myAdapter.notifyDataSetChanged();

            //Scroll to the bottom of list so that user see the last record
            myList.post(new Runnable() {
                public void run() {
                    myList.setSelection(myList.getCount() - 1);
                }
            });
        }
    }
}