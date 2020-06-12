package com.reduxart.tdandroidcontroller.network;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.reduxart.tdandroidcontroller.utils.Constants;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpThread {
    private AsyncTask<Void, Void, Void> async_cient;
    private String Message;
    private InetAddress inetAddress;

    public UdpThread(String messageStr, InetAddress ipAddress) {
        this.Message = messageStr;
        this.inetAddress = ipAddress;
    }

    @SuppressLint({"NewApi", "StaticFieldLeak"})
    public void SendMesssage() {
        async_cient = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                try (DatagramSocket socket = new DatagramSocket()) {
                    DatagramPacket dp;
                    dp = new DatagramPacket(Message.getBytes(), Message.length(), inetAddress, Constants.PORT);
                    socket.setBroadcast(true);
                    socket.send(dp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
            }
        };
        async_cient.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}
