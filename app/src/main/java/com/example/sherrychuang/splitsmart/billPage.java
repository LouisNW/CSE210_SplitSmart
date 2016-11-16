package com.example.sherrychuang.splitsmart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by sherrychuang on 11/15/16.
 */

public class billPage {public class eventPage extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_page_layout);
        Button btn= (Button) findViewById(R.id.ok);
        btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent myIntent = new Intent(view.getContext(), eventPage.class);
                startActivityForResult(myIntent,0);
            }

        });

    }

}
}
