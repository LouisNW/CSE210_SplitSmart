package com.example.sherrychuang.splitsmart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Created by sherrychuang on 11/15/16.
 */

public class createEvent extends Activity {
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

        //TODO: if you think 3 buttons looks too ugly, you can modify it to widget
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
                Intent myIntent = new Intent(view.getContext(), billPage.class);
                startActivityForResult(myIntent,0);
            }

        });

    }

}
