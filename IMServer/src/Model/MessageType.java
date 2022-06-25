package Model;

/**
 * 消息包类型
 *
 * @author leilei
 * @version 1.0
 */
public class MessageType {

    /**
     * 登录消息类型
     */
    public static final int TYPE_LOGIN = 0x1;

    /**
     * 注册消息类型
     */
    public static final int TYPE_REGISTER = 0x2;

    /**
     * 发送消息类型
     */
    public static final int TYPE_SEND = 0x3;

    /**
     * 退出客户端类型
     */
    public static final int TYPE_OFF = 0x4;

    /**
     * 查询好友类型
     */
    public static final int TYPE_SELECT = 0x5;

    /**
     * 删除类型
     */
    public static final int TYPE_DELETE = 0x6;

    /**
     * 添加好友类型
     */
    public static final int TYPE_INSERT = 0x7;

    /**
     * 查询消息记录类型
     */
    public static final int TYPE_MESSAGE = 0x8;
}
