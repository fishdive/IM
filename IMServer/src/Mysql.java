
import Model.MyException;

import java.sql.*;

/**
 * 数据库操作类
 *
 * @author leilei
 * @version 1.0
 * @value JDBC_DRIVER
 * @value DB_URL
 * @value USER
 * @value PASS
 */
public class Mysql {

    // MySQL 8.0 以上版本 - JDBC 驱动名及数据库 URL
    /**
     * JDBC驱动名，常量
     */
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

    /**
     * 数据库 URL，常量
     */
    private static final String DB_URL = "jdbc:mysql://localhost:3306/im?useSSL=false&allowPublicKeyRetrieval=true" +
            "&serverTimezone=UTC&useServerPrepStmts=true";


    // 数据库的用户名与密码，需要根据自己的设置
    /**
     * 数据库用户名，常量
     */
    private static final String USER = "root";

    /**
     * 数据库密码，常量
     */
    private static final String PASS = "123456";


    //数据库连接对象和执行SQL语句对象
    /**
     * 数据库连接对象
     */
    protected static Connection conn = null;

    /**
     * 执行SQL语句对象
     */
    protected static Statement stmt = null;


    /**
     * 数据库连接状态
     */
    private static boolean flag = false;

    public static boolean isFlag() {
        return flag;
    }

    /**
     * PreparedStatement对象,防SQL，注入执行登录请求
     */

    private static PreparedStatement preparedStatement;
    /**
     * 连接数据库
     */
    public static void open() throws MyException, SQLException, Exception {
        if (flag) {
            throw new MyException("\"Error:数据库已连接，不需要重复连接！(2.1)\"");
        } else {
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);

            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // 建立执行SQL语句对象1
            System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();
            flag = true;

            String sql = "SELECT id,name FROM users where id=? and password=?";
            //获取PreparedStatement对象
            preparedStatement = conn.prepareStatement(sql);
        }
    }

    /**
     * 断开数据库
     */
    public static void close() throws MyException, SQLException, Exception {
        if (!flag) {
            throw new MyException("Error:数据库还未连接，请先连接数据库！(2.2)");
        } else {
            System.out.println("关闭数据库...");
            stmt.close();
            conn.close();
            flag = false;
        }
    }
    public static ResultSet login( String id, String password) throws SQLException {
        //设置？值
        preparedStatement.setString(1, id);
        preparedStatement.setString(2, password);

        return preparedStatement.executeQuery();
    }

    /**
     * 通过SQL语句返回查询到的数据集
     *
     * @return ResultSet
     */
    public static ResultSet select(String sql) throws SQLException {
        return stmt.executeQuery(sql);
    }

    /**
     * 通过SQL语句返回查询是否成功
     *
     * @return true已存在，false不存在
     */
    public static boolean isSelect(String sql) throws SQLException {
        return stmt.executeQuery(sql).next();
    }

    /**
     * 通过SQL语句返回受影响的行数
     *
     * @return int
     */
    public static int update(String sql) throws SQLException {
        int result = 0;
        try {
            //开启事务
            conn.setAutoCommit(false);
            result = stmt.executeUpdate(sql);
            //提交事务
            conn.commit();
            return result;
        } catch (SQLException s) {
            //事务回滚
            conn.rollback();
            throw s;
        }
    }

    /**
     * 通过SQL语句返回受影响结果
     *
     * @return boolean
     */
    public static boolean isUpdate(String sql) throws SQLException {
        int result = 0;
        try {
            //开启事务
            conn.setAutoCommit(false);
            result = stmt.executeUpdate(sql);
            //提交事务
            conn.commit();
            return result != 0;
        } catch (SQLException s) {
            //事务回滚
            conn.rollback();
            throw s;
        }
    }
}
