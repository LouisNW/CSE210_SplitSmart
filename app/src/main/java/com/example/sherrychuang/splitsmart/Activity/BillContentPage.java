package com.example.sherrychuang.splitsmart.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.sherrychuang.splitsmart.R;

/**
 * Created by sherrychuang on 11/15/16.
 */

public class BillContentPage extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.bill_content_page_layout);
    }
}
