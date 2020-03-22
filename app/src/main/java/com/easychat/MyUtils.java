package com.easychat;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MyUtils {

    public static  int dpToPx(Context context, float dpValue){
        final float sclae = context.getResources().getDisplayMetrics().density;
        return (int)(dpValue*sclae+0.5f);
    }

    public static int pxToDp(Context context, float pxValue){
        final float sclae = context.getResources().getDisplayMetrics().density;
        return (int)(pxValue/sclae+0.5f);
    }

    public static String sHAEncrypt(String password) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA");
        String value = password;
        byte[] inpute = value.getBytes();
        mDigest.update(inpute);        //input可以是字节型或字节型数组
        String key = new BigInteger(mDigest.digest()).toString();
        return key;
    }

    public static void saveIP(final Context context){
        try {
            SocketAddress address = new InetSocketAddress(Config.SERVER_IP,Config.SERVER_PORT);
            DatagramSocket socket = new DatagramSocket(Config.LOCAL_PORT);
            socket.setSoTimeout(5000);
            SharedPreferences sharedPreferences = context.getSharedPreferences(Config.SAVE_FILE, Context.MODE_PRIVATE);
            String from = sharedPreferences.getString(Config.USER_NICK,"0");
            Paket sendPacket2 = new Paket(from,"0",Config.SAVE_IP_TYPE,"");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("from",sendPacket2.getFrom());
            jsonObject.put("to",sendPacket2.getTo());
            jsonObject.put("type",sendPacket2.getType());
            jsonObject.put("data",sendPacket2.getData());
            String data = jsonObject.toString();
            byte by[] = data.getBytes();
            DatagramPacket packet = new DatagramPacket(by,by.length,address);
            socket.send(packet);
            socket.disconnect();
            socket.close();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public static String objectToJsonString(Object o){
        return new Gson().toJson(o);
    }

    public static Object jsonStringToObiect(String jsonString,Class tClass){
        return new Gson().fromJson(jsonString,tClass);
    }
}
