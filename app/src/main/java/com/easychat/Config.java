package com.easychat;

public class Config {

    public static final String TEST_LOGIN_URL = "http://192.168.1.129:9000/user/login";
    public static final String TEST_LOGIN_URL2 = "http://192.168.1.129:9000/user/getfriend";

    public static final String SAVE_FILE = "SAVE_FILE";             //todo:保存应用信息的文件名

    public static final String INSTALLED = "INSTALLED";             //todo:保存应用是否被安装过的状态，0为未安装过

    public static final String SIGNED = "SIGNED";                   //todo:保存用户是否已经登录成功的状态，0未未登录成功

    public static final String USER_NICK = "USER_NICK";             //todo:保存的用户名（昵称）

    public static final String USER_PASSWORD = "USER_PASSWORD";     //todo:保存的用户密码

    public static final String USER_ID = "USER_ID";                 //todo:保存的用户ID

    public static final String USER_INTRODUCE = "USER_INTRODUCE";   //todo:保存的用户个性签名

    public static final String SAVE_IP_FLAG = "SAVE_IP_FLAG";       //todo:广播客户端IP的标志

    public static final String GET_MSG_FLAG = "GET_MSG_FLAG";       //todo:接收消息的标志

    public static final String SAVE_IP_TYPE = "saveIP";             //todo:广播客户端IP的消息类型

    public static final String SEND_MSG_TYPE = "msg";               //todo:发送聊天消息的消息类型

    public static final String GET_MSG_TYPE = "getMsg";             //todo:收到聊天消息的消息类型

    public static final String SERVER_IP = "1.71.115.113";          //todo:服务器IP

    public static final int SERVER_PORT = 2617;                     //todo:服务器转发和接收用户聊天消息的端口

    public static final int LOCAL_PORT = 2617;                      //todo:客户端发送消息的端口

    public static final int LOCAL_PORT2 = 2620;                    //todo:客户端接收消息的端口

    public static final String LOGIN_URL = "http://1.71.115.113:9000/user/login";           //todo:用户登录请求的URL

    public static final String SINGUP_URL = "http://1.71.115.113:9000/user/signup";         //todo:用户注册请求的URL

    public static final String FRIEND_LIST_URL = "http://1.71.115.113:9000/user/getfriend"; //todo:获取好友列表请求的URL

}
