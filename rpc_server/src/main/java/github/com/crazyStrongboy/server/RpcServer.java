package github.com.crazyStrongboy.server;

import github.com.crazyStrongboy.annotation.RpcAnnotation;
import github.com.crazyStrongboy.registry.RegistryCenter;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mars_jun
 * @version 2019/1/1 13:16
 */
public class RpcServer {

    private RegistryCenter registryCenter;
    private String serverAddress;
    private Map<String, Object> handlers = new HashMap<String, Object>();

    public RpcServer(RegistryCenter registryCenter, String serverAddresss) {
        this.registryCenter = registryCenter;
        this.serverAddress = serverAddresss;
    }

    /**
     * 用来发布注册信息并启动监听
     */
    public void publish() {
        //发布服务
        for (String serviceName : handlers.keySet()) {
            registryCenter.register(serviceName, serverAddress);
        }
        //服务端启动
        startNettyServer(serverAddress, handlers);
    }

    private void startNettyServer(String serverAddress, final Map<String, Object> handlers) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("encode",new ObjectEncoder());
                            pipeline.addLast("decode",new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
                            pipeline.addLast(new RpcHandler(handlers));
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            // Bind and start to accept incoming connections.
            String[] ipPort = serverAddress.split(":");
            ChannelFuture f = b.bind(ipPort[0], Integer.parseInt(ipPort[1])).sync();
            System.out.println("netty 服务启动成功！！！");
            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public void bind(Object... services) {
        for (Object service : services) {
            String serviceName = service.getClass().getAnnotation(RpcAnnotation.class).value().getName();
            handlers.put(serviceName, service);
        }
    }
}
