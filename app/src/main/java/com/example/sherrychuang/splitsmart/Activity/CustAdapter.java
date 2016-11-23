package com.example.sherrychuang.splitsmart.Activity;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.content.Context;
import java.util.ArrayList;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.sherrychuang.splitsmart.R;

/**
 * Created by alice on 11/26/16.
 */

public class CustAdapter extends ArrayAdapter<ItemTest> {
    public CustAdapter(Context context, ArrayList<ItemTest> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ItemTest items = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.bill_item_row_layout, parent, false);
        }
        // Lookup view for data population
        CheckBox tvTax = (CheckBox) convertView.findViewById(R.id.taxCheckbox);
        TextView tvItem = (TextView) convertView.findViewById(R.id.itemName);
        TextView tvPrice = (TextView) convertView.findViewById(R.id.priceValue);
        // Populate the data into the template view using the data object
        tvItem.setText(items.itemName);
        tvPrice.setText(items.price);
        // Return the completed view to render on screen
        return convertView;
    }
}
