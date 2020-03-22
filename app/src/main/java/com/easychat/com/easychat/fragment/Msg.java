package com.easychat.com.easychat.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.easychat.MainApplication;
import com.easychat.R;
import com.easychat.User;
import com.easychat.com.easychat.activity.ChatWhitFriend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Msg extends Fragment implements AdapterView.OnItemClickListener {

    private TextView tip;
    PopupWindow popupWindow;
    ListView listView;
    List<Map<String,?>> data = new ArrayList<>();
    SimpleAdapter simpleAdapter;
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //todo:设置要返回的fragment的视图
        final View view = inflater.inflate(R.layout.fragment_msg,null);
        tip = view.findViewById(R.id.tip);
        listView = view.findViewById(R.id.msg_list);
        listView.setOnItemClickListener(Msg.this);
        //todo:map中的key
        String[] from = {"image","nick","id","introduce"};
        //todo:列表iten中的组件id，需要与from数组中的顺序一致
        int[] to = {R.id.head_image,R.id.name,R.id.id,R.id.context_wrap};
        simpleAdapter = new SimpleAdapter(Msg.this.getActivity(),data,R.layout.msg_list,from,to);
        listView.setAdapter(simpleAdapter);
        new asyctask().doInBackground();


        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 0){
                    tip.setVisibility(View.VISIBLE);
//                    Toast.makeText(Msg.this.getActivity(),"网络检查0",Toast.LENGTH_SHORT).show();
                }else {
                    tip.setVisibility(View.GONE);
//                    Toast.makeText(Msg.this.getActivity(),"网络检查1",Toast.LENGTH_SHORT).show();
                }
            }
        };

        new Thread(){
            @Override
            public void run() {
                ConnectivityManager net;
                NetworkInfo networkInfo;
                Message message;
                while (true){
                    message = handler.obtainMessage();
                    net = (ConnectivityManager) MainApplication.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                    networkInfo = net.getActiveNetworkInfo();
                    try {
                        sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (networkInfo != null && networkInfo.isConnected()){
                        message.what = 1;
                    }else {
                        message.what = 0;
                    }
                    handler.sendMessage(message);
                }
            }
        }.start();

        //todo:点击左上角加号弹出的视图
        final TextView add = view.findViewById(R.id.addflag);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view1 = inflater.inflate(R.layout.menu1,null,false);
                popupWindow=new PopupWindow(view1, 620,LinearLayout.LayoutParams.WRAP_CONTENT,true);
                popupWindow.showAsDropDown(add,0,0);
            }
        });
        add.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (popupWindow!=null&&popupWindow.isShowing()){
                    popupWindow.dismiss();
                    popupWindow=null;
                }
                return false;
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Toast.makeText(this.getContext(),"你点击了第"+(id+1)+"个列表项",Toast.LENGTH_SHORT).show();
        TextView userid = view.findViewById(R.id.id);
        TextView username = view.findViewById(R.id.name);
        String idstring = userid.getText().toString();
        String namestring = username.getText().toString();
        Intent intent = new Intent(this.getActivity(), ChatWhitFriend.class);
        Bundle bundle = new Bundle();
        bundle.putString("id",idstring);
        bundle.putString("name",namestring);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    class asyctask extends AsyncTask<Void,List<Map<String,?>>,List<Map<String,?>>>{

        @Override
        protected List<Map<String, ?>> doInBackground(Void... voids) {
            //todo:准备消息列表数据
            Bundle bundle = Msg.this.getArguments();
            ArrayList<User> users = (ArrayList<User>) bundle.getSerializable("friends");
            data.removeAll(data);
            if (users!=null){
                for (User user:users) {
                    Map<String,Object> map = new HashMap<>();
                    map.put("image",R.drawable.lianxiren);
                    map.put("nick",user.getNick());
                    map.put("id",user.getId());
                    map.put("introduce",user.getIntroduce());
                    data.add(map);
                }
                return data;
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Map<String, ?>> maps) {
            data = maps;
            simpleAdapter.notifyDataSetChanged();
            super.onPostExecute(maps);
        }
    }

}
