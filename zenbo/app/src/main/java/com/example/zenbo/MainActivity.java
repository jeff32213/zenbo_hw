package com.example.zenbo;

import androidx.appcompat.app.AppCompatActivity;

import android.icu.text.RelativeDateTimeFormatter;
import android.os.Bundle;
import org.json.*;
import android.util.Log;
import android.widget.TextView;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.net.Inet4Address;

import com.asus.robotframework.API.MotionControl;
import com.asus.robotframework.API.RobotCallback;
import com.asus.robotframework.API.RobotFace;
import com.asus.robotframework.API.RobotCommand;
import com.asus.robotframework.API.RobotCmdState;
import com.asus.robotframework.API.RobotErrorCode;


public class MainActivity extends Robotactivity {

    public MainActivity(RobotCallback robotCallback, RobotCallback.Listen robotListenCallback){
        super(robotCallback, robotListenCallback);
    }
    TextView test;

    ServerSocket ss;
    public String temp="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        test = findViewById(R.id.test);
        test.setText(getLocalIpAddress());


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ss = new ServerSocket(7100);
                    while(!ss.isClosed()){
                        Socket s = ss.accept();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //test.setText("connect");
                            }
                        });
                        Test client = new Test(s);

                        Thread thread = new Thread(client);
                        thread.start();

                    }
                }catch(IOException e){
                    //shutdown();
                    e.printStackTrace();
                }
            }
        }).start();





    }

    class Test implements Runnable{

        public Socket s;
        public BufferedReader br;
        public BufferedWriter bw;



        public Test(Socket socket){
            try{

                this.s = socket;
                this.br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                this.bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

            }catch(IOException e){

            }
        }

        @Override
        public void run() {
            String line;

            while(!s.isClosed()){
                try{

                    while((line = br.readLine()) != null ) {
                        try{
                            if ( line.charAt(0) == '[' ) {
                                line = line.substring( 1, line.length() - 1 ) ;

                            }

                            String line2 = line;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    test.setText(line2);
                                }
                            });


                            JSONObject jsonObj = new JSONObject(line);



                            switch (jsonObj.getString("action")){
                                case "forward" :
                                    robotAPI.motion.remoteControlBody(MotionControl.Direction.Body.FORWARD);
                                    /*
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            test.setText("forward");
                                        }
                                    });*/
                                    break;


                                case "backward":
                                    robotAPI.motion.remoteControlBody(MotionControl.Direction.Body.BACKWARD);
                                    break;
                                case "turn_right":
                                    robotAPI.motion.remoteControlBody(MotionControl.Direction.Body.TURN_RIGHT);
                                    break;
                                case "turn_left":
                                    robotAPI.motion.remoteControlBody(MotionControl.Direction.Body.TURN_LEFT);
                                    break;
                                case "stop":
                                    robotAPI.motion.remoteControlBody(MotionControl.Direction.Body.STOP);
                                    break;
                                case "speak":
                                    robotAPI.robot.speak(jsonObj.getString("text"));
                                    break;

                                case "read_temp":
                                    robotAPI.robot.speak("現在溫度是" + temp + "度");

                                    break;

                                case "get":
                                    temp = jsonObj.getString("temp");
                                    //robotAPI.robot.speak("現在溫度是" + jsonObj.getString("temp") + "度");
                                    break;

                            }

                        }catch(JSONException e){


                        }

                    }

                }catch (IOException e){
                    break;
                }
            }
        }
    }

    public static RobotCallback robotCallback = new RobotCallback() {
        @Override
        public void onResult(int cmd, int serial, RobotErrorCode err_code, Bundle result) {
            super.onResult(cmd, serial, err_code, result);

            Log.d("RobotDevSample", "onResult:"
                    + RobotCommand.getRobotCommand(cmd).name()
                    + ", serial:" + serial + ", err_code:" + err_code
                    + ", result:" + result.getString("RESULT"));
        }

        @Override
        public void onStateChange(int cmd, int serial, RobotErrorCode err_code, RobotCmdState state) {
            super.onStateChange(cmd, serial, err_code, state);
        }

        @Override
        public void initComplete() {
            super.initComplete();

        }
    };


    public static RobotCallback.Listen robotListenCallback = new RobotCallback.Listen() {
        @Override
        public void onFinishRegister() {

        }

        @Override
        public void onVoiceDetect(JSONObject jsonObject) {

        }

        @Override
        public void onSpeakComplete(String s, String s1) {

        }

        @Override
        public void onEventUserUtterance(JSONObject jsonObject) {

        }

        @Override
        public void onResult(JSONObject jsonObject) {

        }

        @Override
        public void onRetry(JSONObject jsonObject) {

        }
    };


    public MainActivity() {
        super(robotCallback, robotListenCallback);
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