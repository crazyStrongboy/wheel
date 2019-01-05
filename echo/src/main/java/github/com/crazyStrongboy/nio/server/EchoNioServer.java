package github.com.crazyStrongboy.nio.server;

import github.com.crazyStrongboy.utils.Global;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author mars_jun
 * @version 2019/1/5 18:27
 */
public class EchoNioServer {
    public static void main(String[] args) throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(Global.PORT));
        System.err.println("服务启动，监听端口："+Global.PORT);
        //设置非阻塞模式
        serverSocketChannel.configureBlocking(false);
        //设置一个选择器
        Selector selector = Selector.open();
        //注册接收事件（用来处理监听客户端的连接）
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        try {
            while (selector.select() > 0) {
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    if (selectionKey.isAcceptable()) {
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        executorService.execute(new ChannelHandler(socketChannel));
                    }
                }
                iterator.remove();
            }
        } finally {
            executorService.shutdown();
            serverSocketChannel.close();
            selector.close();
        }

    }
}
