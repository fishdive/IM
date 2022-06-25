package Model;

/**
 * 用户类，用来保存当前登录用户id与用户名
 *
 * @author leilei
 * @version 1.0
 */
public class User {
    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        User.id = id;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        User.name = name;
    }

    private static String id;
    private static String name;
}
