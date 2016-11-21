package com.example.sherrychuang.splitsmart.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.sherrychuang.splitsmart.R;

/**
 * Created by sherrychuang on 11/15/16.
 */

public class SplitResultPage extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        setContentView(R.layout.split_result_layout);


    }

}
