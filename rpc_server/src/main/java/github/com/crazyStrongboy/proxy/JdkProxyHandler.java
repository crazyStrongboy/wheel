package github.com.crazyStrongboy.proxy;

import github.com.crazyStrongboy.bean.RpcRequest;
import github.com.crazyStrongboy.client.RpcClientHandler;
import github.com.crazyStrongboy.discovery.ServiceDiscovery;
import github.com.crazyStrongboy.proto.RRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author mars_jun
 * @version 2019/1/1 15:07
 */
public class JdkProxyHandler implements InvocationHandler {
    private Class<?> object;
    private ServiceDiscovery serviceDiscovery;

    public JdkProxyHandler(Class<?> object, ServiceDiscovery serviceDiscovery) {
        this.object = object;
        this.serviceDiscovery = serviceDiscovery;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?>[] parameterTypes = method.getParameterTypes();
//        RpcRequest request = new RpcRequest();
//        request.setClassName(method.getDeclaringClass().getName());
//        request.setMethodName(method.getName());
//        request.setParams(args);
//        request.setTypes(parameterTypes);
        RRequest.Request.Builder builder = RRequest.Request.newBuilder();
        builder.setClassName(method.getDeclaringClass().getName());
        builder.setMethodName(method.getName());
        for (int i = 0; i < args.length; i++) {
            builder.addParams(String.valueOf(args[i]));
            builder.addTypes(parameterTypes[i].getTypeName());
        }
        RRequest.Request request = builder.build();
        String address = this.serviceDiscovery.discovery(object.getName());
        System.err.println("discovery service ...." + address);
        if (address == null) {
            throw new Exception("没有可以调用的服务！");
        }

        RpcClientHandler rpcClientHandler = new RpcClientHandler();
        nettyInvoke(address, request, rpcClientHandler);
        return rpcClientHandler.getResponse();
    }

    private void nettyInvoke(String address, RRequest.Request request, final RpcClientHandler rpcClientHandler) {

        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.TCP_NODELAY, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast("encode", new ObjectEncoder());
                    pipeline.addLast("decode", new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
                    pipeline.addLast(rpcClientHandler);
                }
            });

            String[] ipPort = address.split(":");
            // Start the client.
            ChannelFuture f = b.connect(ipPort[0], Integer.parseInt(ipPort[1])).sync();

            // 将请求写过去
            f.channel().writeAndFlush(request);
            // Wait until the connection is closed.（阻塞方法）
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
