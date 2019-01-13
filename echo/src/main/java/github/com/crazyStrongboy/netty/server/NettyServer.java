package github.com.crazyStrongboy.netty.server;

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
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<Channel>() {
                        @Override
                        public void initChannel(final Channel ch) throws Exception {
                            final ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new ServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture future = bootstrap.bind(Global.PORT).sync();
            System.err.println("服务端开始监听。。。。" + Global.PORT);
            // 测试terminationListener
            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Iterator<EventExecutor> boosIterator = bossGroup.iterator();
                while (boosIterator.hasNext()) {
                    EventExecutor next = boosIterator.next();
                    next.shutdownGracefully();
                }
            }).start();
            Future<?> terminationFuture = bossGroup.terminationFuture();
            System.err.println(terminationFuture.get());
            ChannelFuture sync = future.channel().closeFuture().sync();
            System.err.println(sync.get());
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
