package com.easychat.com.easychat.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.easychat.Config;
import com.easychat.MainApplication;
import com.easychat.R;
import com.easychat.com.easychat.activity.Login;

public class Manage extends Fragment {

    private TextView my_name;
    private TextView account;
    private TextView introduce;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_manage,null);
        Button loginout = view.findViewById(R.id.loginout);
        TextView app_config = view.findViewById(R.id.app_config);
        TextView editmsg = view.findViewById(R.id.editmsg);
        my_name = view.findViewById(R.id.my_name);
        account = view.findViewById(R.id.accnut);
        introduce = view.findViewById(R.id.mycontext_wrap);
        SharedPreferences sharedPreferences = Manage.this.getActivity().getSharedPreferences(Config.SAVE_FILE,Context.MODE_PRIVATE);
        String nick = sharedPreferences.getString(Config.USER_NICK,"");
        String accoun = sharedPreferences.getString(Config.USER_ID,"");
        String intro = sharedPreferences.getString(Config.USER_INTRODUCE,"");
        if ((nick!=null || nick.equals("")) && (accoun!=null || accoun.equals("")) && (intro!=null || intro.equals(""))){
            my_name.setText(nick);
            account.setText(accoun);
            introduce.setText(intro);
        }else {
            my_name.setText("");
            account.setText("");
            introduce.setText("");
        }
        //todo:退出登录按钮监听
        loginout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = Manage.this.getActivity().getSharedPreferences(Config.SAVE_FILE,Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Config.SIGNED,"0");
                editor.commit();
                MainApplication.getInstance().setValue(Config.GET_MSG_FLAG,"0");
                Chat c = (Chat) Manage.this.getActivity();
                c.getA().cancel(true);
                Intent intent = new Intent(Manage.this.getActivity(), Login.class);
                startActivity(intent);
            }
        });
        app_config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText( view.getContext(),"敬请期待！",Toast.LENGTH_SHORT).show();
            }
        });
        editmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText( view.getContext(),"该功能暂不开放！",Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
