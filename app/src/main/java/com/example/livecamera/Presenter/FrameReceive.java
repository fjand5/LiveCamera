package com.example.livecamera.Presenter;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.livecamera.Model.UdpConnection;

public class FrameReceive {

    static OnDataCome _onDataCome;
    public static void setOnDataCome(OnDataCome onDataCome) {
        _onDataCome = onDataCome;
    }
    public static void receive(byte[] data) {

        final Bitmap ter = BitmapFactory.decodeByteArray(data,0,data.length);
        if(_onDataCome != null){
            _onDataCome.callBack(ter);
        }
    }

    public interface OnDataCome{
        void callBack(Bitmap bitmap);
    }
}
