package com.example.sherrychuang.splitsmart.Activity;

import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.widget.TextView;

import com.example.sherrychuang.splitsmart.R;

import java.util.List;

/**
 * Created by Chihhung on 11/27/16.
 */

public class EventAdapter extends ArrayAdapter<String> {
    Context context;
    List<String> items;
    public EventAdapter(Context context, List<String> items) {
        super(context, android.R.layout.simple_list_item_1, items);
        this.context = context;
        this.items = items;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutInflater inflater;
        if (row == null) {
            inflater = LayoutInflater.from(context);
            row = inflater.inflate(R.layout.custom_listview_item, null);
        }
        if (position % 2 == 0) {
            row.setBackgroundColor(0xFFF5F9F9);
        }
        else {
            row.setBackgroundColor(0xFFD4E6F1);
        }
        TextView bill_name = (TextView)row.findViewById(R.id.item_name);
        bill_name.setText(items.get(position));
        return row;
    }
}
