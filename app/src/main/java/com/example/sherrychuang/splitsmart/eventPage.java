package com.example.sherrychuang.splitsmart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by sherrychuang on 11/15/16.
 */

public class eventPage extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.event_page_layout);

        Button btn= (Button) findViewById(R.id.sampleBill);
        btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent myIntent = new Intent(eventPage.this, billPage.class);
                eventPage.this.startActivity(myIntent);
            }
        });

        Button splitbtn= (Button) findViewById(R.id.split);
        splitbtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent myIntent = new Intent(view.getContext(), splitResult.class);
                startActivityForResult(myIntent,0);
            }

        });
    }

}

