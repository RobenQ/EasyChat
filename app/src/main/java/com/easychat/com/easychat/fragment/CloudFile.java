package com.easychat.com.easychat.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.easychat.R;

public class CloudFile extends Fragment {

    PopupWindow popupWindow;
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_cloud_file,null);
        final TextView creatDir = view.findViewById(R.id.creatDir);
        TextView pc_tv = view.findViewById(R.id.pc_tv);
        //todo:pc监听
        pc_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText( view.getContext(),"简聊PC版正在路上，敬请期待！",Toast.LENGTH_SHORT).show();
            }
        });
        //todo:popwindow监听
        creatDir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view1 = inflater.inflate(R.layout.menu2,null,false);

                popupWindow=new PopupWindow(view1, 620, LinearLayout.LayoutParams.WRAP_CONTENT,true);
                popupWindow.showAsDropDown(creatDir,0,0);
            }
        });
        creatDir.setOnTouchListener(new View.OnTouchListener() {
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
}
