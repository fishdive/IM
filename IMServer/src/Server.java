
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 服务器
 *
 * @author leilei
 * @version 1.0
 */
public class Server {
    private static Vector<UserThread> vector;
    public static void main(String[] args) {
        //保存客户端处理的线程
        vector = new Vector<>();
        //固定大小的线程池，用来处理不同客户端
        ExecutorService es = new ThreadPoolExecutor(
                5, 200, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(1024), new ThreadPoolExecutor.AbortPolicy());
        //创建服务器端的Socket
        try {
            ServerSocket server = new ServerSocket(8888);
            //打开数据库连接
            Mysql.open();
            System.out.println("服务器已启动，正在等待连接...");
            while (true) {
                //接受客户端的Socket，若没有，阻塞在这
                Socket socket = server.accept();
                //每来一个客户端，创建一个线程处理它
                UserThread user = new UserThread(socket, vector);
                //开启线程
                es.execute(user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void cliceOff(UserThread userThread){
        //从用户线程中删掉它，并且从先给其他用户新的数组
        vector.remove(userThread);
        for (int i=0;i<vector.size();i++){
            vector.get(i).setVector(vector);
        }
    }
}
