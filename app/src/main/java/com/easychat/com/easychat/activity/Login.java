package com.easychat.com.easychat.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.easychat.Config;
import com.easychat.MainApplication;
import com.easychat.MyUtils;
import com.easychat.R;
import com.easychat.com.easychat.fragment.Chat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;

public class Login extends AppCompatActivity {

    //声明页面组件
    private EditText account;
    private  EditText password;
    private CheckBox save;
    private Button loginBtn;
    private TextView signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //todo:没有登录则进行正常的登录逻辑
        //todo:获取页面组件
        account = findViewById(R.id.account);
        password = findViewById(R.id.password);
        save = findViewById(R.id.save);
        loginBtn = findViewById(R.id.login);
        signup = findViewById(R.id.sign_to);
        //todo:判断用户是否登录
        SharedPreferences sharedPreferencesall = getSharedPreferences(Config.SAVE_FILE, Context.MODE_PRIVATE);
        String signed = sharedPreferencesall.getString(Config.SIGNED,"0");
        account.setText(sharedPreferencesall.getString(Config.USER_NICK,""));
        password.setText(sharedPreferencesall.getString(Config.USER_PASSWORD,""));

        //ToDo:登录按钮监听事件
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo:从服务的获取数据（分线程）
                String url = Config.LOGIN_URL;
                new asynctask().execute(url);
            }
        });

        //ToDo:新用户Textview账号注册的点击事件
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Signup.class);
                startActivity(intent);
            }
        });

        //ToDo:记住密码
        account.addTextChangedListener(new Edittext());
        password.addTextChangedListener(new Edittext());
        //ToDo:记住密码checkbox选择改变事件
        save.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    String accountString = account.getText().toString();
                    String passwordString = password.getText().toString();
                    SharedPreferences sharedPreferences = getSharedPreferences(Config.SAVE_FILE, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(Config.USER_NICK,accountString);
                    editor.putString(Config.USER_PASSWORD,passwordString);
                    editor.commit();
                }else {
                    SharedPreferences sharedPreferences = getSharedPreferences(Config.SAVE_FILE, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove(Config.USER_NICK);
                    editor.remove(Config.USER_PASSWORD);
                    editor.commit();
                }
            }
        });

        if (signed.equals("1")){
            //todo:已经登录，直接进入聊天界面
            Intent intent = new Intent(Login.this, Chat.class);
            startActivity(intent);
        }
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

    //todo:输入框监听
    class Edittext implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (save.isChecked()){
                SharedPreferences sharedPreferences = getSharedPreferences(Config.SAVE_FILE, Context.MODE_PRIVATE);
                String accountString = account.getText().toString();
                String passwordString = password.getText().toString();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Config.USER_NICK,accountString);
                editor.putString(Config.USER_PASSWORD,passwordString);
                editor.commit();
            }
        }
    }

    class asynctask extends AsyncTask<String,Void,String> {
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
                String pwd = MyUtils.sHAEncrypt(password.getText().toString());
                writer.write("nick="+account.getText().toString()+"&pwd="+pwd);
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
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            //todo:
            if (s==null){
                Toast.makeText(Login.this,"连接服务器超时,请重试！",Toast.LENGTH_LONG).show();
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
                    SharedPreferences sharedPreferences = getSharedPreferences(Config.SAVE_FILE, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    try {
                        editor.putString(Config.USER_NICK,json.getString("nick"));
                        editor.putString(Config.USER_ID,json.getString("id"));
                        editor.putString(Config.USER_PASSWORD,password.getText().toString());
                        editor.putString(Config.USER_INTRODUCE,json.getString("introduce"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    editor.putString(Config.SIGNED,"1");
                    editor.commit();
                    MainApplication.getInstance().setValue(Config.USER_NICK,account.getText().toString());
                    Intent intent = new Intent(Login.this, Chat.class);
                    //todo:解析好友列表
//                    try {
//                        String friendJson = json.getString("friends");
//                        Toast.makeText(Login.this,json.toString(),Toast.LENGTH_LONG).show();
//                        System.out.println(json.toString());
//                        Bundle bundle = new Bundle();
//                        bundle.putString("friends",friendJson);
//                        intent.putExtras(bundle);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                    startActivity(intent);
                }else {
                    Toast.makeText(Login.this,"账号或密码错误！",Toast.LENGTH_SHORT).show();
                }
                super.onPostExecute(s);
            }
        }
    }

}
