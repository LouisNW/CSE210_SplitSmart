package com.example.sherrychuang.splitsmart.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.cunoraz.tagview.Tag;
import com.cunoraz.tagview.TagView;
import com.example.sherrychuang.splitsmart.R;
import com.example.sherrychuang.splitsmart.data.Bill;
import com.example.sherrychuang.splitsmart.data.Item;
import com.example.sherrychuang.splitsmart.data.Person;
import com.example.sherrychuang.splitsmart.manager.BillManager;
import com.example.sherrychuang.splitsmart.manager.ItemManager;
import com.example.sherrychuang.splitsmart.manager.ManagerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jenny on 12/3/16.
 */

public class EditBillContentPage extends AppCompatActivity {
    private ListView myList;
    private EditBillContentPage.BillAdapter myAdapter;
    private List<ItemInput> myItems;
    private Bill bill;
    private BillManager billManager;
    private ItemManager itemManager;
    private List<Person> person;
    private Item item;

    private ArrayAdapter<String> spinnerAdapter;
    private List<String> eventPeopleList;
    private List<List<Integer>> personSelectIndex;
    private List<Item> itemList;

    private EditText taxRate;
    private List<Boolean> oldItemOrNot;
    private boolean toDelete;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.bill_content_page_layout);

        //Get the information of event, bill and person, and initial the itemManager
        bill = (Bill)intent.getSerializableExtra("Bill");
        billManager = ManagerFactory.getBillManager(this);
        person = billManager.getSharingPersonsOfBill(bill.getId());
        itemManager  = ManagerFactory.getItemManager(this);


        // Set Action Bar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(bill.getName());

        //Get people name
        eventPeopleList = new ArrayList<String>();
        eventPeopleList.add(""); //add an empty String to the front
        for(Person p: person) {
            eventPeopleList.add(p.getName());
        }
        personSelectIndex = new ArrayList<List<Integer>>();

        myItems = new ArrayList<ItemInput>();
        oldItemOrNot = new ArrayList<Boolean>();

        //Set the custom adapter
        myList = (ListView) findViewById(R.id.MyList);
        myList.setItemsCanFocus(true);
        myAdapter = new EditBillContentPage.BillAdapter();
        myList.setAdapter(myAdapter);

        //Get all items and show them
        itemList = itemManager.getAllItemsOfBill(bill.getId());
        for(int i = 0; i < itemList.size(); i++) {
            Item item = itemList.get(i);
            addNewItem(item.isTaxItem(), item.getName(), String.valueOf(item.getPrice()), true);
            myItems.get(i).setTax(item.isTaxItem());
            myItems.get(i).setItemName(item.getName());
            myItems.get(i).setPrice(String.valueOf(item.getPrice()));
            List<Person> personShareList = itemManager.getSharingPersonsOfAnItem(item.getId());

            for(int j = 0; j < personShareList.size(); j++) {
                Tag tempTag = new Tag(personShareList.get(j).getName());
                tempTag.layoutColor = Color.GRAY;
                tempTag.isDeletable = true;
                myItems.get(i).setSelectedPeople(tempTag);
            }

            //Get shared by people for an item
            List<Person> shareList = itemManager.getSharingPersonsOfAnItem(item.getId());
            for(int a = 0; a < person.size(); a++) {
                for(int b = 0; b < shareList.size(); b++) {
                    if(person.get(a).getId() == shareList.get(b).getId()) {
                        personSelectIndex.get(i).add(a);
                    }
                }
            }
        }

        //Get tax rate and show it
        taxRate = (EditText) findViewById(R.id.ItemTax);
        taxRate.setText(String.valueOf(billManager.getBill(bill.getId()).getTaxRate()));

        //Set a button for SAVE
        final Button save = (Button) findViewById(R.id.setup_macroSavebtn);
        save.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                boolean flagEmptyCheck = false;
                for(int i = 0; i < myItems.size(); i++) {
                    ItemInput myItem = myItems.get(i);
                    String itemI = myItem.getItemName();
                    String priceI = myItem.getPrice();
                    boolean taxI = myItem.getTax();
                    double priceVal;
                    if (itemI.length() >= 2  && priceI.length() >= 2) {
                        flagEmptyCheck = false;
                        priceVal = Double.parseDouble(priceI);
                        //Update existed item
                        if(oldItemOrNot.get(i)) {
                            item = itemList.get(i);
                            item.setName(itemI);
                            item.setPrice(priceVal);
                            item.setTaxItem(taxI);
                            itemManager.updateItem(item);
                        }
                        //Insert new item
                        else {
                            item = new Item(itemI, priceVal, taxI, bill.getId());
                            itemManager.insertItem(item);
                        }

                        //Insert Shared by
                        List<Person> personToShare = new ArrayList<Person>();
                        for(int j = 0; j < personSelectIndex.get(i).size(); j++) {
                            int indexToShare = personSelectIndex.get(i).get(j);
                            personToShare.add(person.get(indexToShare));
                        }
                        itemManager.setSharingPersonsOfAnItem(item, personToShare);

                    }else if(itemI.length() == 1 || priceI.length() == 1){
                        flagEmptyCheck = true;
                        //Error message dialog
                        LayoutInflater inflater= getLayoutInflater();
                        View layout = inflater.inflate(R.layout.bill_warning_layout, null);
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditBillContentPage.this);
                        alertDialogBuilder.setView(layout);
                        alertDialogBuilder.setCancelable(false);
                        final AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                        final TextView warning_text = (TextView) layout.findViewById(R.id.warning_message);
                        warning_text.setText("Some items might be empty \n" + "Please check them");
                        Button okButton = (Button) layout.findViewById(R.id.ok);
                        okButton.setOnClickListener(new View.OnClickListener(){
                            public void onClick(View view){
                                alertDialog.dismiss();
                            }
                        });
                    }
                }

                //Get the tax rate and store it to db in Bill
                String taxInput = taxRate.getText().toString();
                double taxRateVal;
                try
                {
                    taxRateVal = Double.parseDouble(taxInput);
                }
                catch(NumberFormatException e)
                {
                    taxRateVal = 0;
                }

                if(flagEmptyCheck == false) {
                    bill.setTaxRate(taxRateVal);
                    billManager.updateBill(bill);
                    finish();
                }
            }
        });

        //Click cancel button and go back to Event Page
        final Button cancel = (Button) findViewById(R.id.setup_macroCancelbtn);
        cancel.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                finish();
            }
        });
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

        public View getView(final int position, View convertView, ViewGroup parent) {
            //Use ViewHolder to avoid frequent call of findViewById() during ListView scrolling,
            //and that will make it smooth
            toDelete = false;
            final EditBillContentPage.ViewHolder holder;
            if (convertView == null) {
                holder = new EditBillContentPage.ViewHolder();
                convertView = mInflater.inflate(R.layout.bill_contect_item, null);
                holder.itemName = (EditText) convertView.findViewById(R.id.ItemName);
                holder.itemPrice = (EditText) convertView.findViewById(R.id.ItemPrice);
                holder.taxSelect = (CheckBox) convertView.findViewById(R.id.TaxCheckbox);
                holder.sharedBy = (Spinner) convertView.findViewById(R.id.spinner1);
                holder.selectedPeople = (TagView) convertView.findViewById(R.id.peopleTag);
                convertView.setTag(holder);
            } else {
                holder = (EditBillContentPage.ViewHolder) convertView.getTag();
            }

            final int delPos = position;
            //Delete an item
            final ImageButton del = (ImageButton) convertView.findViewById(R.id.deleteBtn);
            del.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    toDelete = true;
                    if(oldItemOrNot.get(delPos))
                        itemManager.deleteItem(itemList.get(delPos).getId());
                    myItems.remove(delPos);
                    notifyDataSetChanged();
                }
            });

            //Use a Spinner to select one value from items
            final Spinner spinner = (Spinner) convertView.findViewById(R.id.spinner1);
            spinnerAdapter = new ArrayAdapter<String>(EditBillContentPage.this, android.R.layout.simple_spinner_item, eventPeopleList);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spinnerAdapter);
            spinner.setSelection(0, false); //unselected the default first item

            //Fill EditText with the value you have in data source
            ItemInput myThisItem = myItems.get(position);
            holder.itemName.setText(myThisItem.getItemName());
            holder.itemName.setId(position);
            holder.itemPrice.setText(myThisItem.getPrice());
            holder.itemPrice.setId(position);

            //Check the tax check box
            holder.taxSelect.setChecked(myThisItem.getTax());
            holder.taxSelect.setId(position);

            //Fill selectPeople with the value we have in data source
            holder.selectedPeople.addTags(myThisItem.getSelectedPeople());
            holder.selectedPeople.setId(position);

            //Update adapter once we finish with editing
            holder.itemName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus && !toDelete){
                        final int position = v.getId();
                        final EditText Caption = (EditText) v;
                        ItemInput myThisItem = myItems.get(position);
                        String editResult = Caption.getText().toString();
                        myThisItem.setItemName(editResult);
                    }
                }
            });

            holder.itemPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus && !toDelete){
                        final int position = v.getId();
                        final EditText Caption = (EditText) v;
                        ItemInput myThisItem = myItems.get(position);
                        String editResult = Caption.getText().toString();
                        myThisItem.setPrice(editResult);
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
                }
            });

            holder.sharedBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (spinner.getSelectedItemPosition() != 0) {
                        String selectName = spinner.getSelectedItem().toString();
                        Tag tempTag = new Tag(selectName);

                        ItemInput myThisItem = myItems.get(position);
                        tempTag.layoutColor = Color.GRAY;
                        tempTag.isDeletable = true;

                        myThisItem.setSelectedPeople(tempTag);
                        holder.selectedPeople.addTag(tempTag);
                        personSelectIndex.get(position).add(i-1); //minus the first dummy empty entry
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            holder.selectedPeople.setOnTagDeleteListener(new TagView.OnTagDeleteListener() {
                @Override
                public void onTagDeleted(final TagView view, final Tag tag, final int i) {
                    ItemInput myThisItem = myItems.get(position);
                    personSelectIndex.get(position).remove(i);
                    myThisItem.getSelectedPeople().remove(i);
                    view.remove(i);
                }
            });

            return convertView;
        }
    }

    class ViewHolder {
        CheckBox taxSelect;
        EditText itemName;
        EditText itemPrice;
        Spinner sharedBy;
        TagView selectedPeople;
    }

    //Add a new ListView item
    public void addNewItem(boolean taxSelected, String itemName, String itemPrice, boolean flag) {
        List<Tag> people = new ArrayList<Tag>();
        ItemInput itemInput = new ItemInput(taxSelected, itemName, itemPrice, people);
        myItems.add(itemInput);
        personSelectIndex.add(new ArrayList<Integer>());
        oldItemOrNot.add(flag);
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
            addNewItem(false, " ", " ", false);
            myAdapter.notifyDataSetChanged();

            //Scroll to the bottom of list so that user see the last record
            myList.post(new Runnable() {
                public void run() {
                    myList.setSelection(myList.getCount() - 1);
                }
            });
        }
    }
    // on back button press
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

}
