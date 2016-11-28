package com.example.sherrychuang.splitsmart.Activity;

import android.widget.ArrayAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.graphics.Color;
import android.content.Context;
import java.util.List;

/**
 * Created by Chihhung on 11/27/16.
 */

public class ColorAdapter extends ArrayAdapter<String> {
    public ColorAdapter(Context context, int resources, List<String> items) {
        super(context, resources, items);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        if (position % 2 == 1) {
            view.setBackgroundColor(0x30FF0000);
        } else {
            view.setBackgroundColor(0x30FF0010);
        }

        return view;
    }
}
