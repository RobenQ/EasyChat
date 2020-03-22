package com.easychat;

import android.app.Application;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class MainApplication extends Application {
    //todo:Application单例化
    public static MainApplication mainApplication;
    public static MainApplication getInstance(){
        return mainApplication;
    }
    //todo:声明存放全局属性的map映射
    public Map<String,Object> values = new HashMap<String, Object>();
    public Socket socket;

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mainApplication = this;
    }

    //todo:获取全局属性的方法
    public Object getValue(String key){
        return  values.get(key);
    }

    //todo:存放全局属性的方法
    public void setValue(String key,Object value){
        values.put(key,value);
    }

}
