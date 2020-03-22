package com.easychat.com.easychat.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.easychat.Config;
import com.easychat.MainApplication;
import com.easychat.Paket;
import com.easychat.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class ChatWhitFriend extends AppCompatActivity {

    private TextView back2;
    private TextView name;
    private TextView xiaoxi;
    private EditText send_msg;
    private Button send;
    private Socket socket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_whit_friend);
        name = findViewById(R.id.name2);
        back2 = findViewById(R.id.fanhui);
        send_msg = findViewById(R.id.send_msg);
        send = findViewById(R.id.send);
        xiaoxi = findViewById(R.id.list_msg);
        back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("=======");
                finish();
            }
        });
        Bundle bundle = this.getIntent().getExtras();
        final String username = bundle.getString("name");
        String userid = bundle.getString("id");
        name.setText(username);
        connect();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (send_msg.getText().toString().equals("")||send_msg.getText().toString()==null){
                    Toast.makeText(v.getContext(),"要发送的消息不能为空！",Toast.LENGTH_SHORT).show();
                }else {
                    sendmsg();
                }
            }
        });
    }
    BufferedReader br = null;
    BufferedWriter bf = null;
    public void connect() {
         new asynctask().execute();
    }

    public void sendmsg(){
        new Thread(){
            @Override
            public void run() {
                try {
                    SocketAddress address = new InetSocketAddress(Config.SERVER_IP,Config.SERVER_PORT);
                    DatagramSocket socket = new DatagramSocket(Config.LOCAL_PORT2);
                    socket.setSoTimeout(5000);
                    SharedPreferences sharedPreferences = getSharedPreferences(Config.SAVE_FILE, Context.MODE_PRIVATE);
                    String from = sharedPreferences.getString(Config.USER_NICK,"0");
                    Paket sendPacket = new Paket(from,name.getText().toString(),Config.SEND_MSG_TYPE,send_msg.getText().toString());
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("from",sendPacket.getFrom());
                    jsonObject.put("to",sendPacket.getTo());
                    jsonObject.put("type",sendPacket.getType());
                    jsonObject.put("data",sendPacket.getData());
                    String data = jsonObject.toString();
                    System.out.println(data);
                    byte by[] = data.getBytes();
                    DatagramPacket packet = new DatagramPacket(by,by.length,address);
                    socket.send(packet);
                    socket.disconnect();
                    socket.close();
                    sleep(1000*3);
                } catch (IOException | JSONException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        xiaoxi.append("我："+send_msg.getText().toString()+"\n");
//        send_msg.setText("");
//        Toast.makeText(ChatWhitFriend.this,"消息发送成功!",Toast.LENGTH_SHORT);
    }

    class asynctask extends AsyncTask<Void,String,Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                socket = MainApplication.getInstance().getSocket();
                if (socket==null||( !socket.isConnected() ) ){
                    socket = new Socket(Config.SERVER_IP,Config.SERVER_PORT);
                    MainApplication.getInstance().setSocket(socket);
                }
                br = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
                bf = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"));
                publishProgress("success");
                String line = null;
                while ((line = br.readLine())!=null){
                    publishProgress(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            if (values[0].equals("success")){
                xiaoxi.append(values[0]+"\n");
            }
            xiaoxi.append("别人："+values[0]+"\n");
            super.onProgressUpdate(values);
        }
    }
}
