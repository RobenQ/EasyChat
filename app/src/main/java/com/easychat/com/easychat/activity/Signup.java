package com.easychat.com.easychat.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.easychat.Config;
import com.easychat.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Signup extends AppCompatActivity {

    private TextView back;
    private EditText nick;
    private EditText pwd;
    private Button signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup);
        back = findViewById(R.id.back);
        nick = findViewById(R.id.new_account);
        pwd = findViewById(R.id.new_pwd);
        signup = findViewById(R.id.sign_to);
        //todo:返回监听
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //todo:注册按钮监听
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nick.getText().toString().equals("") || pwd.getText().toString().equals("")){
                    Toast.makeText(Signup.this,"账号或密码填写不完整！",Toast.LENGTH_LONG).show();
                }else {
                    //todo:从服务的获取数据（分线程）
                    String url = Config.SINGUP_URL;
                    new asynctask().execute(url);
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }else
            return super.onKeyDown(keyCode, event);
    }

    class asynctask extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(10000);
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setUseCaches(false);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
                writer.write("nick="+nick.getText().toString()+"&pwd="+pwd.getText().toString());
                writer.flush();
                String line;
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine())!=null){
                    sb.append(line);
                }
                reader.close();
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
                Toast.makeText(Signup.this,"连接服务器超时,请重试！",Toast.LENGTH_LONG).show();
            }else {
                JSONObject json = null;
                int success = 0;
                try {
                    json = new JSONObject(s);
                    success = json.getInt("msg");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (success == 1) {
                    nick.setText("");
                    pwd.setText("");
                    Toast.makeText(Signup.this,"注册成功！",Toast.LENGTH_LONG).show();
                    finish();
                }else {
                    Toast.makeText(Signup.this,"该用户名已存在！",Toast.LENGTH_SHORT).show();
                }
                super.onPostExecute(s);
            }
        }
    }
}
