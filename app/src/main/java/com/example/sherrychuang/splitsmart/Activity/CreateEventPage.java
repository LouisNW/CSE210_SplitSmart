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

public class CreateEventPage extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.create_event_layout);

        Button cancelbtn = (Button) findViewById(R.id.cancel);
        cancelbtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent myIntent = new Intent(view.getContext(), MainActivity.class);
                startActivityForResult(myIntent,0);
            }

        });

        //TODO: if you think 3 buttons looks too ugly, you can modify it to:
        // click 'ok' button to launch widget with 3 options
        Button camerabtn = (Button) findViewById(R.id.camera);
        camerabtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                //TODO: launch camera activity
            }

        });

        Button gallerybtn = (Button) findViewById(R.id.gallery);
        gallerybtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                //TODO: launch gallery
            }

        });

        Button manualbtn = (Button) findViewById(R.id.manual);
        manualbtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent myIntent = new Intent(view.getContext(), BillPage.class);
                startActivityForResult(myIntent,0);
            }

        });

    }

}
