package com.example.mutualtwo.Utility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

public class PhotoRetrieverTask extends AsyncTask<String, Void, Bitmap> {
    private String url;

    private ImageView iv;
    public void setIV(ImageView iv) {
        this.iv = iv;
    }

    private Bitmap myBitmap;
    public void setBM(Bitmap myBitmap) {
        this.myBitmap = myBitmap;
    }

    //Start thread, do something independent of main thread
    @Override
    protected Bitmap doInBackground(String... strings) {
        url = strings[0];
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            return BitmapFactory.decodeStream(is);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //Once it's done, what do we do about it?
    @Override
    protected void onPostExecute(Bitmap bm) {
        super.onPostExecute(bm);
        iv.setImageBitmap(bm);
        myBitmap = bm;
        DataCache.getInstance().putPic(url, myBitmap);
    }
}