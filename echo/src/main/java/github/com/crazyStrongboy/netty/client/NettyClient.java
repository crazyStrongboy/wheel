package github.com.crazyStrongboy.netty.client;

import github.com.crazyStrongboy.utils.Global;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workGroup).channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {

                        }
                    })
                    .option(ChannelOption.TCP_NODELAY, true);
            ChannelFuture future = bootstrap.connect(Global.CONNECT_HOST, Global.PORT).sync();
            ByteBuf buf =Unpooled.buffer();
            buf.writeBytes("dada".getBytes());
            future.channel().writeAndFlush(buf);
            future.channel().closeFuture().sync();
        }finally {
            // 资源必须放在finally中关闭掉
            workGroup.shutdownGracefully();
        }

    }
}
