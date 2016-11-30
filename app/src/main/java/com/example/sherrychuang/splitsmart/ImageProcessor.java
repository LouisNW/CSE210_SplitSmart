package com.example.sherrychuang.splitsmart;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sherrychuang on 11/15/16.
 */

public class ImageProcessor {
    String LOG_TAG="ImageProcessor";
    ArrayList<String> itemList = new ArrayList<>();
    ArrayList<Float> priceList = new ArrayList<>();
    public ImageProcessor(Bitmap bitmap, Context c){
        if(bitmap!=null){
            Log.d(LOG_TAG, "bitmap != null");
            TextRecognizer textRecognizer = new TextRecognizer.Builder(c).build();
            if(!textRecognizer.isOperational()) {
                // Note: The first time that an app using a Vision API is installed on a
                // device, GMS will download a native libraries to the device in order to do detection.
                // Usually this completes before the app is run for the first time.  But if that
                // download has not yet completed, then the above call will not detect any text,
                // barcodes, or faces.
                // isOperational() can be used to check if the required native libraries are currently
                // available.  The detectors will automatically become operational once the library
                // downloads complete on device.
                Log.w(LOG_TAG, "Detector dependencies are not yet available.");

                // Check for low storage.  If there is low storage, the native library will not be
                // downloaded, so detection will not become operational.
                IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
                /*boolean hasLowStorage = registerReceiver(null, lowstorageFilter) != null;

                if (hasLowStorage) {
                    Toast.makeText(c,"Low Storage", Toast.LENGTH_LONG).show();
                    Log.w(LOG_TAG, "Low Storage");
                }*/
            }


            Frame imageFrame = new Frame.Builder().setBitmap(bitmap).build();

            SparseArray<TextBlock> textBlocks = textRecognizer.detect(imageFrame);
            Log.d(LOG_TAG, "size: "+textBlocks.size());
            for (int i = 0; i < textBlocks.size(); i++) {
                TextBlock textBlock = textBlocks.get(textBlocks.keyAt(i));
                List<? extends Text> textComponents = textBlock.getComponents();
                /*if(i==0){//price
                    for(Text currentText : textComponents) {//loop through all component of block
                        String s = currentText.getValue();
                        Float f = Float.parseFloat(s);
                        priceList.add(f);
                    }
                }
                if(i==6){//item
                    for(Text currentText : textComponents) {//loop through all component of block
                        String s = currentText.getValue();
                        itemList.add(s);
                    }
                }*/

                Log.i(LOG_TAG, textBlock.getValue());
                // Do something with value
            }
        }
        for(int i=0; i<itemList.size(); i++)
            Log.d(LOG_TAG, itemList.get(i)+" ");

        for(int i=0; i<priceList.size(); i++)
            Log.d(LOG_TAG, ""+priceList.get(i)+" ");

    }

}
