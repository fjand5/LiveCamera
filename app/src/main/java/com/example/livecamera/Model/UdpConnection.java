package com.example.livecamera.Model;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;

import com.example.livecamera.Presenter.FrameReceive;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import static android.content.Context.WIFI_SERVICE;

public class UdpConnection {
    String ip;
    final int PORT = 4444;
    static UdpConnection instance;


    private UdpConnection(Context context){
        WifiManager wm = (WifiManager) context.getSystemService(WIFI_SERVICE);
        ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());


        new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] buf = new byte[1000000];
                try {
                    DatagramSocket datagramSocket = new DatagramSocket(PORT, InetAddress.getByName(ip));
                    while (true){

                        DatagramPacket dp = new DatagramPacket(buf, 1000000);
                        datagramSocket.receive(dp);
                        FrameReceive.receive(dp.getData());
                    }

                } catch (SocketException e) {
                    e.printStackTrace();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
    public static UdpConnection getInstance(Context context) {
        if(instance == null){
            instance = new UdpConnection(context);
        }
        return instance;
    }

    public void send(final byte[] data){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DatagramSocket datagramSocket = new DatagramSocket();
                    byte []buf = data;
                    DatagramPacket dp = new DatagramPacket(buf, data.length, InetAddress.getByName(ip),PORT);
                    datagramSocket.send(dp);

                } catch (SocketException e) {
                    e.printStackTrace();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
