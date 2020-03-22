package com.easychat.com.easychat.fragment;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.easychat.Config;
import com.easychat.MainApplication;
import com.easychat.MyUtils;
import com.easychat.Paket;
import com.easychat.R;
import com.easychat.User;
import com.easychat.com.easychat.activity.Login;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Chat extends FragmentActivity {

    private TextView xiaoxi_tv;
    private TextView cloudfile_tv;
    private  TextView manage_tv;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    private Msg msg = new Msg();
    private CloudFile cloudFile = new CloudFile();
    private Manage manage = new Manage();
    public static ArrayList<User> users = new ArrayList<>();
    public asynctask a;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        MainApplication.getInstance().setValue(Config.SAVE_IP_FLAG,"1");
        MainApplication.getInstance().setValue(Config.GET_MSG_FLAG,"1");
        xiaoxi_tv = findViewById(R.id.xiaoxi_tv);
        cloudfile_tv = findViewById(R.id.cloudfile_tv);
        manage_tv = findViewById(R.id.manage_tv);
        xiaoxi_tv.setTextColor(Color.parseColor("#3399ff"));
        xiaoxi_tv.setOnClickListener(new Tv_click());
        cloudfile_tv.setOnClickListener(new Tv_click());
        manage_tv.setOnClickListener(new Tv_click());
        fragmentTransaction.replace(R.id.fl_replace, msg);
        MyUtils.saveIP(this);
        new asynctask2().execute(Config.FRIEND_LIST_URL);
        a = new asynctask();
        a.execute();
    }

    public asynctask getA() {
        return a;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MainApplication.getInstance().setValue(Config.SAVE_IP_FLAG,"0");
        MainApplication.getInstance().setValue(Config.GET_MSG_FLAG,"0");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            SharedPreferences sharedPreferences = getSharedPreferences(Config.SAVE_FILE, Context.MODE_PRIVATE);
            String signed = sharedPreferences.getString(Config.SIGNED,"0");
            if (signed.equals("1")){
                Intent home = new Intent(Intent.ACTION_MAIN);
                home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                home.addCategory(Intent.CATEGORY_HOME);
                startActivity(home);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    class Tv_click implements OnClickListener {

        @Override
        public void onClick(View v) {
            int viewId = v.getId();
            switch (viewId) {
                case R.id.xiaoxi_tv: {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fl_replace, msg);
                    fragmentTransaction.commit();
                    xiaoxi_tv.setTextColor(Color.parseColor("#3399ff"));
                    cloudfile_tv.setTextColor(Color.parseColor("#000000"));
                    manage_tv.setTextColor(Color.parseColor("#000000"));
                    break;
                }
                case R.id.cloudfile_tv:{
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fl_replace,cloudFile);
                    fragmentTransaction.commit();
                    xiaoxi_tv.setTextColor(Color.parseColor("#000000"));
                    cloudfile_tv.setTextColor(Color.parseColor("#3399ff"));
                    manage_tv.setTextColor(Color.parseColor("#000000"));
                    break;
                }
                case R.id.manage_tv:{
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fl_replace,manage);
                    fragmentTransaction.commit();
                    xiaoxi_tv.setTextColor(Color.parseColor("#000000"));
                    cloudfile_tv.setTextColor(Color.parseColor("#000000"));
                    manage_tv.setTextColor(Color.parseColor("#3399ff"));
                    break;
                }
            }
        }
    }

    class asynctask extends AsyncTask<Void,String,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            while (true && MainApplication.getInstance().getValue(Config.GET_MSG_FLAG).equals("1")){
                DatagramSocket socket = null;
                try {
                    socket = new DatagramSocket(Config.LOCAL_PORT);
                    SharedPreferences sharedPreferences = getSharedPreferences(Config.SAVE_FILE, Context.MODE_PRIVATE);
                    String from = sharedPreferences.getString(Config.USER_NICK,"0");
//                    publishProgress("接收消息中。。。");
                    byte by[] = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(by,by.length);
                    socket.setSoTimeout(3000);
                    socket.receive(packet);
                    String dataString = new String(by,0,by.length).trim();
                    publishProgress(dataString);
//                    socket.close();
                } catch (IOException e) {
//                    e.printStackTrace();
//                    socket.close();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            Toast.makeText(Chat.this,values[0],Toast.LENGTH_LONG).show();
            super.onProgressUpdate(values);
        }
    }

    class asynctask2 extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.setConnectTimeout(5000);
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setUseCaches(false);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
                SharedPreferences sharedPreferences = getSharedPreferences(Config.SAVE_FILE, Context.MODE_PRIVATE);
                String id = sharedPreferences.getString(Config.USER_ID,"error");
                writer.write("id="+id);
                writer.flush();
                String line;
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine())!=null){
                    sb.append(line);
                }
                reader.close();
                writer.close();
                return sb.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            //todo:
            if (s==null){
                Toast.makeText(Chat.this,"获取好友列表失败，请退出重新登录！",Toast.LENGTH_LONG).show();
            }else {
                JSONObject json = null;
                try {
                    json = new JSONObject(s);
                    String msg2 = json.getString("msg");
                    if (msg2.equals("error")||msg2==null){
                        Toast.makeText(Chat.this,"获取好友列表失败，请退出重新登录！",Toast.LENGTH_LONG).show();
                    }else {
                        String friend = json.getString("friends");
                        users = new Gson().fromJson(friend,new TypeToken<List<User>>() {}.getType());
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("friends",users);
                        msg.setArguments(bundle);
                        fragmentTransaction.commit();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                super.onPostExecute(s);
            }
        }
    }
}
