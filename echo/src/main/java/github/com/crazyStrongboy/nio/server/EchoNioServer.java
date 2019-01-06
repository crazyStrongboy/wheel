package github.com.crazyStrongboy.nio.server;

import github.com.crazyStrongboy.utils.Global;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
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
        System.err.println("服务启动，监听端口：" + Global.PORT);
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
                        registerChannel(serverSocketChannel, selector);
                    }
                    if (selectionKey.isValid() && selectionKey.isReadable()) {
                        System.out.println("readddddddddddddddd");
//                        executorService.execute(new ChannelHandler(selectionKey));
                        ByteBuffer buffer = ByteBuffer.allocate(50);
                        SocketChannel clientSocketChannel = (SocketChannel) selectionKey.channel();
                        // 通过测试发现，这里必须要将channel缓冲中的数据读出来了。
                        //这里猜测是selector.selectedKeys();这里会遍历所有通道中是否有缓存的数据来觉得read事件
                        int readCount = clientSocketChannel.read(buffer);
                        if (readCount <= 0) {
                            clientSocketChannel.close();
                            selectionKey.cancel();
                            return;
                        }
                        buffer.flip();
                        String readMsg = new String(buffer.array(), 0, readCount).trim();
                        String writeMsg = "NIO: " + readMsg;
                        if ("byebye".equalsIgnoreCase(readMsg)) {
                            //这里必须要把这个channel从selector中移除，因为客户端已经关闭掉了
                            selectionKey.cancel();
                            writeMsg = "byebye!";
                        }
                        buffer.clear();
                        buffer.put(writeMsg.getBytes());
                        buffer.flip();
                        clientSocketChannel.write(buffer);
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

    private static void registerChannel(ServerSocketChannel serverSocketChannel, Selector selector) throws IOException {
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        //这里可以注册一个属性进去
        socketChannel.register(selector, SelectionKey.OP_READ);
        System.out.println("Accept request from " + socketChannel.getRemoteAddress());
    }
}
