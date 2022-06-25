package Model;

import java.io.Serializable;

/**
 * 消息序列化包
 *
 * @author leilei
 * @version 1.0
 */
public class Message implements Serializable {
    /**
     * 发送者
     */
    private String from;
    /**
     * 接收者
     */
    private String to;
    /**
     * 消息类型
     */
    private int type;

    /**
     * 消息包
     */
    private String info;

    /**
     * 用户名
     */
    private String userId;
    /**
     * 密码
     */
    private String userPassword;
    /**
     * 昵称
     */
    private String userName;

    /**
     * 请求目的结果
     */
    private boolean flag;

    /**
     * 登录消息实例化包
     */
    public Message(String userId, String userPassword, int type) {
        this.userId = userId;
        this.type = type;
        if (type==MessageType.TYPE_LOGIN){
            this.userPassword = userPassword;
        }else if(type==MessageType.TYPE_MESSAGE){
            //借着实现序列化下查找聊天记录对象
            this.to=userPassword;
        }
    }

    /**
     * 注册消息实例化包
     */
    public Message(String userId, String userPassword, String userName, int type) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.userName = userName;
        this.type = type;
    }

    /**
     * 发送消息实例化包
     */
    public Message(String from, String to, int type, String info) {
        this.from = from;
        this.to = to;
        this.info = info;
        this.type = type;
    }

    /**
     * 查询、删除、添加好友实例化包
     */
    public Message(String userId, int type) {
        this.userId = userId;
        this.type = type;
    }

    /**
     * 客户端退出登录
     */
    public Message(int type) {
        this.type = type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
