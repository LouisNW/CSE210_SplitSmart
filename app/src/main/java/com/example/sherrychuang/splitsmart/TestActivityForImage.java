package com.example.sherrychuang.splitsmart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.example.sherrychuang.splitsmart.Ocr.*;

import com.example.sherrychuang.splitsmart.Activity.EventPage;

/**
 * Created by sherrychuang on 11/23/16.
 */



public class TestActivityForImage extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Log.d("ImageProcessor", "enter");
        setContentView(R.layout.test_layout);
        Button btn = (Button) findViewById(R.id.ocr);
        btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent myIntent = new Intent(view.getContext(), OcrCaptureActivity.class);
                startActivityForResult(myIntent,0);
            }

        });
        /*Context c = getApplicationContext();
        Bitmap bitmap = BitmapFactory.decodeResource(c.getResources(), R.drawable.zoomin);
        ImageProcessor ip = new ImageProcessor(bitmap, c);*/


    }

}

