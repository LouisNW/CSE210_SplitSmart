package com.example.sherrychuang.splitsmart.Activity;

import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.content.Context;
import java.util.ArrayList;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.sherrychuang.splitsmart.R;

/**
 * Created by alice on 11/26/16.
 */

public class CustAdapter extends ArrayAdapter<ItemInput> {
    private EditText tvItem;
    private EditText tvPrice;

    public CustAdapter(Context context, ArrayList<ItemInput> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ItemInput items = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.bill_item_row_layout, parent, false);
            viewHolder.tvItem = (EditText) convertView.findViewById(R.id.itemName);
            viewHolder.tvPrice = (EditText) convertView.findViewById(R.id.priceValue);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Lookup view for data population
        CheckBox tvTax = (CheckBox) convertView.findViewById(R.id.taxCheckbox);
        //tvItem = (EditText) convertView.findViewById(R.id.itemName);
        //tvPrice = (EditText) convertView.findViewById(R.id.priceValue);
        // Populate the data into the template view using the data object
        viewHolder.tvItem.setText(items.itemName);
        viewHolder.tvPrice.setText(items.price);
        //tvItem.setText(items.itemName);
        //tvPrice.setText(items.price);

        // Return the completed view to render on screen
        return convertView;
    }

    // The ViewHolder, only one item for simplicity and demonstration purposes, you can put all the views inside a row of the list into this ViewHolder
    private static class ViewHolder {
        EditText tvItem;
        EditText tvPrice;
    }
}
