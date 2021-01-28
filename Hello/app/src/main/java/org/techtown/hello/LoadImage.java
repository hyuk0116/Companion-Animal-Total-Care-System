package org.techtown.hello;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

//프로필사진을 외부에서 받아 ImageView에 씌우는 메소드
public class LoadImage {

    private String imgPath;
    private Bitmap bitmap;

    LoadImage(String imgPath){
        this.imgPath = imgPath;
    }

    public LoadImage(){}

    public Bitmap getBitmap(){
        Thread imgThread = new Thread(){
            @Override
            public void run(){
                try {
                    URL url = new URL(imgPath);
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                }catch (IOException e){
                }
            }
        };
        imgThread.start();
        try{
            imgThread.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            return bitmap;
        }
    }

}

