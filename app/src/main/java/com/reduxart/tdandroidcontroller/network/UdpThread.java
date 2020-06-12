package com.reduxart.tdandroidcontroller.network;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import com.reduxart.tdandroidcontroller.utils.Constants;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import static android.content.ContentValues.TAG;

public class UdpThread {
    private static InetAddress inetAddress;

    public UdpThread(InetAddress ipAddress) {
        inetAddress = ipAddress;
    }

    @SuppressLint({"NewApi", "StaticFieldLeak"})
    public static void SendMesssage(final String messageStr) {
        AsyncTask<Void, Void, Void> async_cient = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                try (DatagramSocket socket = new DatagramSocket()) {
                    DatagramPacket dp;
                    dp = new DatagramPacket(messageStr.getBytes(), messageStr.length(), inetAddress, Constants.PORT);
                    socket.setBroadcast(true);
                    socket.send(dp);
                    Log.e(TAG, "Message sent " + messageStr);

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
