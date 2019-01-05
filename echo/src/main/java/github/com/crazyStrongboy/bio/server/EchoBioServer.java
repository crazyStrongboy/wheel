package github.com.crazyStrongboy.bio.server;

import github.com.crazyStrongboy.utils.Global;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * BIO 服务
 *
 * @author mars_jun
 * @version 2019/1/5 18:27
 */
public class EchoBioServer {
    public static void main(String[] args) throws Exception {
        //监听一个端口
        ServerSocket serverSocket = new ServerSocket(Global.PORT);
        System.err.println("开始监听：" + Global.PORT);
        //开启一个处理的线程池
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        try {
            while (true) {
                //获取连接
                Socket clientSocket = serverSocket.accept();
                //将任务丢到线程池中去处理
                executorService.execute(new ClientHandler(clientSocket));
            }
        } finally {
            //最后进行释放资源
            serverSocket.close();
            executorService.shutdown();
        }

    }
}
