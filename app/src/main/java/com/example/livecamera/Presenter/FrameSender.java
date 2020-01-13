package com.example.livecamera.Presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.YuvImage;

import com.example.livecamera.Model.UdpConnection;
import com.example.livecamera.View.MainActivity;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class FrameSender {
    public static void send(Context mContext, byte[] dataImage, int w, int h, int f){
        byte[] ter;

        YuvImage yuvImage = new YuvImage(dataImage,f,w,h,null);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0,0,w,h),100,byteArrayOutputStream);

        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArrayOutputStream.toByteArray(),
                0,byteArrayOutputStream.toByteArray().length);
        ter = bitmapToByteArray(bitmap,10);

        UdpConnection.getInstance(mContext).send(ter);
//        FrameReceive.receive(ter);
    }

    public static byte[] bitmapToByteArray(Bitmap inbitmap, int scale) {

        Bitmap bitmap = Bitmap.createScaledBitmap(inbitmap,inbitmap.getWidth()/scale,
                inbitmap.getHeight()/scale,false);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream(bitmap.getWidth() * bitmap.getHeight());
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, buffer);

        return buffer.toByteArray();
    }
}
