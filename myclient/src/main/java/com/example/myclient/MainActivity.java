package com.example.myclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    private EditText editText;

    private Socket socket;

    private Thread commectThread;//连接服务器线程

    public static final int SERVERPORT = 6000;

    public static final String SERVER_IP = "192.168.0.100";

    private boolean running = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.edittext);
    }

    public void onConnect(View view){
        commectThread = new Thread(new ClientThread());
        commectThread.start();

    }

    //发送
    public void onSend(View view){
        try {
            String str = editText.getText().toString();

            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())),true);
            out.println(str);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    class ClientThread implements Runnable{
        @Override
        public void run() {

            running = true;

            try {
                //InetAddress serverAddr = InetAddress.getByName(SERVER_IP);

                /**
                 * Socket(设备IP,端口)
                 */

                socket = new Socket(SERVER_IP,SERVERPORT);

            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                running = false;
            }

        }
    }

}
