package com.example.name.mysocket;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {


    private ServerSocket serverSocket;

    Handler myHandler;//更新UI界面

    Thread serverThread = null;//

    private TextView myText;

    public static final int SERVERPORT = 6000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myText = (TextView) findViewById(R.id.text);

    }

    //开户服务
    public void startServer(View view){

        myHandler = new Handler();
        this.serverThread = new Thread(new ServerThread());
        this.serverThread.start();
    }


    //服务器线程
    class ServerThread implements Runnable{

        Socket socket = null;

        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(SERVERPORT);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //如果这个线程没有被中断的话
            while (!Thread.currentThread().isInterrupted()){

                try {

                    socket = serverSocket.accept(); //没数据就在这等 如果有数据就将数据给socket

                    CommunicatonThread commThread = new CommunicatonThread(socket); //将接收到的数据 传给这个类
                    new Thread(commThread).start(); //然后开启这个线程做数据处理

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }



    //数据处理
    class CommunicatonThread implements Runnable{

        //客户端 数据
        private Socket clientSocket;

        private BufferedReader input;

        public CommunicatonThread(Socket clientSocket){

            this.clientSocket = clientSocket;

            try {
                this.input = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {

            while (!Thread.currentThread().isInterrupted()){

                try {
                    String read = input.readLine();
                    myHandler.post(new updateUIThread(read));
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

        }
    }

    class updateUIThread implements Runnable{

        private  String msg;

        public updateUIThread(String str){
            this.msg = str;
        }

        @Override
        public void run() {

            myText.setText(myText.getText().toString()+"Client Says: "+msg + "\n");

        }
    }


}
