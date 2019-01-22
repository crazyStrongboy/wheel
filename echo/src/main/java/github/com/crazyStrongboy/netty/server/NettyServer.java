package github.com.crazyStrongboy.netty.server;

import github.com.crazyStrongboy.netty.handler.HttpsCodecInitializer;
import github.com.crazyStrongboy.utils.Global;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;

import java.util.Iterator;

public class NettyServer {
    public static void main(String[] args) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    //设置创建channel的工厂，后面会调用NioServerSocketChannel的无参构造进行初始化，此处不初始化NioServerSocketChannel
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new HttpsCodecInitializer(null, false))
                    //服务端将不能处理的客户端连接请求放在队列中等待处理，backlog参数指定了队列的大小
                    .option(ChannelOption.SO_BACKLOG, 128)
                    //当设置该选项以后，如果在两小时内没有数据的通信时，TCP会自动发送一个活动探测数据报文。
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture future = bootstrap.bind(Global.PORT).sync();
            System.err.println("服务端开始监听。。。。" + Global.PORT);
            // 测试terminationListener
//            new Thread(() -> {
//                try {
//                    Thread.sleep(2000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                Iterator<EventExecutor> boosIterator = bossGroup.iterator();
//                while (boosIterator.hasNext()) {
//                    EventExecutor next = boosIterator.next();
//                    next.shutdownGracefully();
//                }
//            }).start();
//            Future<?> terminationFuture = bossGroup.terminationFuture();
//            System.err.println(terminationFuture.get());
            ChannelFuture sync = future.channel().closeFuture().sync();
            System.err.println(sync.get());
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
