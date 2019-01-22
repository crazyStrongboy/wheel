package github.com.crazyStrongboy.netty.client;

import github.com.crazyStrongboy.netty.handler.HttpsCodecInitializer;
import github.com.crazyStrongboy.utils.Global;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;

public class NettyClient {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workGroup).channel(NioSocketChannel.class)
                    .handler(new HttpsCodecInitializer(null, true))
                    .option(ChannelOption.TCP_NODELAY, true);
            ChannelFuture future = bootstrap.connect(Global.CONNECT_HOST, Global.PORT).sync();
            ByteBuf buf = Unpooled.buffer();
            buf.writeBytes("Are you Ok?".getBytes());
            DefaultFullHttpRequest  request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, "/aa", buf);
            // 构建http请求
            request.headers().set(HttpHeaderNames.HOST, Global.CONNECT_HOST);
            request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            request.headers().set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());
            future.channel().pipeline().writeAndFlush(request);
            future.channel().closeFuture().sync();
        } finally {
            // 资源必须放在finally中关闭掉
            workGroup.shutdownGracefully();
        }

    }
}
