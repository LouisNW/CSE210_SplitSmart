package com.example.sherrychuang.splitsmart.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.sherrychuang.splitsmart.R;

/**
 * Created by alice on 12/7/16.
 */

public class MainPage extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //the layout for main
        setContentView(R.layout.main);
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.main_layout);
        layout.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                Intent i = new Intent(v.getContext(), MainActivity.class);
                startActivity(i);
                finish();
                return true;
            }
        });
    }
}
