package com.example.livecamera.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;

import com.example.livecamera.Presenter.FrameReceive;
import com.example.livecamera.Presenter.FrameSender;
import com.example.livecamera.R;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private SurfaceView localPreviewSfv;
    private ImageView remotePreviewImg;
    Context mContext;
    Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        initView();
        initCamera();
        addEvent();

    }

    private void initCamera() {
        camera = Camera.open(1);
        camera.startPreview();
    }

    private void initView() {
        remotePreviewImg = findViewById(R.id.remotePreviewImg);
        localPreviewSfv=  findViewById(R.id.localPreviewSfv);
    }

    private void addEvent() {

        FrameReceive.setOnDataCome(new FrameReceive.OnDataCome() {
            @Override
            public void callBack(final Bitmap bitmap) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        remotePreviewImg.setImageBitmap(bitmap);
                    }
                });

            }
        });
        camera.setOneShotPreviewCallback(new Camera.PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] bytes, Camera camera) {
                Camera.Parameters parameters = camera.getParameters();
                int h = parameters.getPreviewSize().height;
                int w = parameters.getPreviewSize().width;
                int f = parameters.getPreviewFormat();
                FrameSender.send(mContext, bytes,w,h,f);



                camera.setOneShotPreviewCallback(this);
            }
        });
        final SurfaceHolder holder = localPreviewSfv.getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                try {
                    camera.setPreviewDisplay(holder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

            }
        });
    }
}
