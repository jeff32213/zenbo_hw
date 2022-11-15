package com.example.zenbo_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.Inet4Address;
import java.net.SocketException;
import java.util.Enumeration;

import java.util.*;
import org.json.*;

public class MainActivity extends AppCompatActivity {

    Button forward, backward, turn_left, turn_right, stop, send, sensor;
    EditText input;
    Socket s;
    BufferedReader br;
    BufferedWriter bw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        forward = findViewById(R.id.forward);
        backward = findViewById(R.id.backward);
        turn_left = findViewById(R.id.turn_left);
        turn_right = findViewById(R.id.turn_right);
        stop = findViewById(R.id.stop);

        input = findViewById(R.id.input);
        send = findViewById(R.id.send);

        sensor = findViewById(R.id.sensor);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    s = new Socket("192.168.0.236", 7100);
                    br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

                }catch(IOException e){
                    e.printStackTrace();
                }
            }

        }).start();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {

                    @Override
                    public void run() {


                        try{
                            HashMap<String, String> hash= new HashMap<String, String>();
                            hash.put("action", "speak");
                            hash.put("text", input.getText().toString());
                            Map<String, String> tmp;
                            tmp = hash;

                            JSONObject json = new JSONObject(tmp);
                            bw.write(json.toString());
                            bw.newLine();
                            bw.flush();


                        }catch (IOException e){
                            e.printStackTrace();
                        }

                    }

                }).start();
            }
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {

                    @Override
                    public void run() {


                        try{
                            HashMap<String, String> hash= new HashMap<String, String>();
                            hash.put("action", "forward");
                            Map<String, String> tmp;
                            tmp = hash;

                            JSONObject json = new JSONObject(tmp);
                            bw.write(json.toString());
                            bw.newLine();
                            bw.flush();


                        }catch (IOException e){
                            e.printStackTrace();
                        }

                    }

                }).start();
            }
        });

        backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {

                    @Override
                    public void run() {


                        try{
                            HashMap<String, String> hash= new HashMap<String, String>();
                            hash.put("action", "backward");
                            Map<String, String> tmp;
                            tmp = hash;

                            JSONObject json = new JSONObject(tmp);
                            bw.write(json.toString());
                            bw.newLine();
                            bw.flush();


                        }catch (IOException e){
                            e.printStackTrace();
                        }

                    }

                }).start();
            }
        });

        turn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {

                    @Override
                    public void run() {


                        try{
                            HashMap<String, String> hash= new HashMap<String, String>();
                            hash.put("action", "turn_left");
                            Map<String, String> tmp;
                            tmp = hash;

                            JSONObject json = new JSONObject(tmp);
                            bw.write(json.toString());
                            bw.newLine();
                            bw.flush();


                        }catch (IOException e){
                            e.printStackTrace();
                        }

                    }

                }).start();
            }
        });

        turn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {

                    @Override
                    public void run() {


                        try{
                            HashMap<String, String> hash= new HashMap<String, String>();
                            hash.put("action", "turn_right");
                            Map<String, String> tmp;
                            tmp = hash;

                            JSONObject json = new JSONObject(tmp);
                            bw.write(json.toString());
                            bw.newLine();
                            bw.flush();


                        }catch (IOException e){
                            e.printStackTrace();
                        }

                    }

                }).start();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {

                    @Override
                    public void run() {


                        try{
                            HashMap<String, String> hash= new HashMap<String, String>();
                            hash.put("action", "stop");
                            Map<String, String> tmp;
                            tmp = hash;

                            JSONObject json = new JSONObject(tmp);
                            bw.write(json.toString());
                            bw.newLine();
                            bw.flush();


                        }catch (IOException e){
                            e.printStackTrace();
                        }

                    }

                }).start();
            }
        });


        sensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Thread(new Runnable() {

                    @Override
                    public void run() {


                        try{
                            HashMap<String, String> hash= new HashMap<String, String>();
                            hash.put("action", "read_temp");
                            Map<String, String> tmp;
                            tmp = hash;

                            JSONObject json = new JSONObject(tmp);
                            bw.write(json.toString());
                            bw.newLine();
                            bw.flush();


                        }catch (IOException e){
                            e.printStackTrace();
                        }

                    }

                }).start();


            }
        });

    }

    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    System.out.println("ip1--:" + inetAddress);
                    System.out.println("ip2--:" + inetAddress.getHostAddress());
                    // for getting IPV4 format
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("IP Address", ex.toString());
        }
        return null;
    }


}