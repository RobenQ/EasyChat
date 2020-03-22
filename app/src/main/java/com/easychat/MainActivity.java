package com.easychat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.easychat.com.easychat.activity.Login;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager leader;
    List<View> leaders = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //todo:获取viewpager
        leader = findViewById(R.id.leader);

        //todo:判断是否安装过该软件
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SAVE_FILE, Context.MODE_PRIVATE);
        String installed = sharedPreferences.getString(Config.INSTALLED,"0");
        if (installed.equals("0")){
            //todo:没有安装过
            {
                View view = LayoutInflater.from(this).inflate(R.layout.activity_main2,null);
                ImageView imageView = view.findViewById(R.id.leader_img);
                leaders.add(view);
            }
            {
                View view = LayoutInflater.from(this).inflate(R.layout.activity_main2,null);
                ImageView imageView = view.findViewById(R.id.leader_img);
                leaders.add(view);
            }
            {
                View view = LayoutInflater.from(this).inflate(R.layout.activity_main2,null);
                ImageView imageView = view.findViewById(R.id.leader_img);
                leaders.add(view);
            }
        }else {
            //todo:安装过
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }


        //todo:设置适配器
        leader.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return leaders.size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
                return view==o;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                container.addView(leaders.get(position));
                return leaders.get(position);
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                if (position==0){
                    findViewById(R.id.right_flag).setVisibility(View.INVISIBLE);
                    findViewById(R.id.into).setVisibility(View.VISIBLE);
                    findViewById(R.id.into).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences(Config.SAVE_FILE, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(Config.INSTALLED,"1");
                            editor.commit();
                            Intent intent = new Intent(MainActivity.this,Login.class);
                            startActivity(intent);
                        }
                    });
                }
                container.removeView(leaders.get(position));
            }
        });
    }
}
