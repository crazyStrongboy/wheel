package github.com.crazyStrongboy.netty.datagram.client;

import github.com.crazyStrongboy.utils.Global;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

/**
 * @author mars_jun
 * @version 2019/1/23 22:15
 */
public class DatagramClient {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new DatagramDecode());
                            pipeline.addLast(new LogEventHandler());
                        }
                    });
            Channel channel = bootstrap.bind(Global.PORT).sync().channel();
            System.err.println("client start..");
            channel.closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }

    }
}
