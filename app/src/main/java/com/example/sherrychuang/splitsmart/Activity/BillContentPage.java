package com.example.sherrychuang.splitsmart.Activity;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
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
import android.widget.Toast;

import com.cunoraz.tagview.Tag;
import com.cunoraz.tagview.TagView;
import com.example.sherrychuang.splitsmart.R;
import com.example.sherrychuang.splitsmart.data.Bill;
import com.example.sherrychuang.splitsmart.data.Event;
import com.example.sherrychuang.splitsmart.data.Item;
import com.example.sherrychuang.splitsmart.data.Person;
import com.example.sherrychuang.splitsmart.manager.BillManager;
import com.example.sherrychuang.splitsmart.manager.ItemManager;
import com.example.sherrychuang.splitsmart.manager.ManagerFactory;

/**
 * Modified by Jenny and Alice on 12/3/16.
 * Description: The billContentPage shows the name, price, tax, person to shared of each item by
 * getting data from image-parse-to-text or manually inputs. Items will then be stored after users
 * clicked on the "SAVE" button; otherwise, items will not be stored and it will go back to the
 * Event Page.
 */

public class BillContentPage extends AppCompatActivity {
    private ListView myList;
    private BillAdapter myAdapter;
    private List<ItemInput> myItems;
    private Bill bill;
    private Event event;
    private BillManager billManager;
    private ItemManager itemManager;
    private List<Person> person;
    private Item item;

    private ArrayAdapter<String> spinnerAdapter;
    private List<String> eventPeopleList;
    private List<List<Integer>> personSelectIndex;
    private boolean toDelete;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.bill_content_page_layout);

        //Get the information of event, bill and person, and initial the itemManager
        bill = (Bill)intent.getSerializableExtra("Bill");
        event = (Event)intent.getSerializableExtra("Event");
        //Get items from image-parse-to-text

        personSelectIndex = new ArrayList<List<Integer>>();
        String[] myItemAr = (String[]) intent.getSerializableExtra("ItemInput");
        String[] myPriceAr = (String[]) intent.getSerializableExtra("PriceInput");
        myItems = new ArrayList<ItemInput>();
        if(myItemAr!=null) {
            for (int i = 0; i < myItemAr.length && i < myPriceAr.length; i++) {
                addNewItem(false, myItemAr[i], myPriceAr[i]);
            }
        }
        billManager = ManagerFactory.getBillManager(this);
        person = billManager.getSharingPersonsOfBill(bill.getId());
        itemManager  = ManagerFactory.getItemManager(this);

        //Get people name
        eventPeopleList = new ArrayList<String>();
        eventPeopleList.add(""); //add an empty String to the front
        for(Person p: person) {
            eventPeopleList.add(p.getName());
        }


        //Set the custom adapter
        myList = (ListView) findViewById(R.id.MyList);
        myList.setItemsCanFocus(true);
        myAdapter = new BillAdapter();
        myList.setAdapter(myAdapter);

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
                    if (itemI.length() >= 2 && priceI.length() >= 2) {
                        flagEmptyCheck = false;
                        double priceVal = Double.parseDouble(priceI);
                        item = new Item(itemI, priceVal, taxI, bill.getId());
                        itemManager.insertItem(item);

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
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(BillContentPage.this);
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
                EditText taxRate = (EditText) findViewById(R.id.ItemTax);
                String taxInput = taxRate.getText().toString();
                //Toast.makeText(getBaseContext(), "Tax rate: " + taxInput, Toast.LENGTH_SHORT).show();
                double taxRateVal;
                try
                {
                    taxRateVal = Double.parseDouble(taxInput);
                }
                catch(NumberFormatException e)
                {
                    taxRateVal = 0;
                }

                //Toast.makeText(getBaseContext(), "Tax rate: " + taxRateVal, Toast.LENGTH_SHORT).show();

                if(flagEmptyCheck == false) {
                    bill.setTaxRate(taxRateVal);
                    billManager.updateBill(bill);
                    //Toast.makeText(getBaseContext(), "Tax rate: " + taxRateVal, Toast.LENGTH_SHORT).show();
                    //finish();
                    Intent myIntent = new Intent(BillContentPage.this, EventPage.class);
                    myIntent.putExtra("Event", event);
                    BillContentPage.this.startActivity(myIntent);
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
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.bill_contect_item, null);
                holder.itemName = (EditText) convertView.findViewById(R.id.ItemName);
                holder.itemPrice = (EditText) convertView.findViewById(R.id.ItemPrice);
                holder.taxSelect = (CheckBox) convertView.findViewById(R.id.TaxCheckbox);
                holder.sharedBy = (Spinner) convertView.findViewById(R.id.spinner1);
                holder.selectedPeople = (TagView) convertView.findViewById(R.id.peopleTag);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final int delPos = position;
            //Delete an item
            final ImageButton del = (ImageButton) convertView.findViewById(R.id.deleteBtn);
            del.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    toDelete = true;
                    myItems.remove(delPos);
                    notifyDataSetChanged();
                }
            });

            //Use a Spinner to select one value from items
            final Spinner spinner = (Spinner) convertView.findViewById(R.id.spinner1);
            spinnerAdapter = new ArrayAdapter<String>(BillContentPage.this, android.R.layout.simple_spinner_item, eventPeopleList);
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
            holder.itemName.setOnFocusChangeListener(new OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus && !toDelete){
                        final int position = v.getId();
                        final EditText Caption = (EditText) v;
                        ItemInput myThisItem = myItems.get(position);
                        String editResult = Caption.getText().toString();
                        myThisItem.setItemName(editResult);
                        //Toast.makeText(getBaseContext(), "Name " + position + " is " + editResult, Toast.LENGTH_SHORT).show();
                    }
                }
            });

            holder.itemPrice.setOnFocusChangeListener(new OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus && !toDelete){
                        final int position = v.getId();
                        final EditText Caption = (EditText) v;
                        ItemInput myThisItem = myItems.get(position);
                        String editResult = Caption.getText().toString();
                        myThisItem.setPrice(editResult);
                        //Toast.makeText(getBaseContext(), "Price " + position + " is " + editResult, Toast.LENGTH_SHORT).show();
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
                    //
                    // Toast.makeText(getBaseContext(), "Tax " + position + " is " + checkRes, Toast.LENGTH_SHORT).show();
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
    public void addNewItem(boolean taxSelected, String itemName, String itemPrice) {
        List<Tag> people = new ArrayList<Tag>();
        ItemInput itemInput = new ItemInput(taxSelected, itemName, itemPrice, people);
        myItems.add(itemInput);
        personSelectIndex.add(new ArrayList<Integer>());
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
            addNewItem(false, " ", " ");
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