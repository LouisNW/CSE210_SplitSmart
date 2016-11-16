package com.example.sherrychuang.splitsmart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton addEvent= (ImageButton) findViewById(R.id.addEvent);
        addEvent.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent myIntent = new Intent(view.getContext(), createEvent.class);
                startActivityForResult(myIntent,0);
            }

        });

        Button btn= (Button) findViewById(R.id.sampleEvent);
        btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent myIntent = new Intent(view.getContext(), eventPage.class);
                startActivityForResult(myIntent,0);
            }

        });
    }
}
