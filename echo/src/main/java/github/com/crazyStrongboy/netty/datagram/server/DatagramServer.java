package github.com.crazyStrongboy.netty.datagram.server;

import github.com.crazyStrongboy.utils.Global;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;

/**
 * @author mars_jun
 * @version 2019/1/23 22:14
 */
public class DatagramServer {
    private static String filePath = "C:\\Users\\Administrator\\Desktop\\0123\\1.txt";

    public static void main(String[] args) throws Exception {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new DatagramEncode(new InetSocketAddress("255.255.255.255",Global.PORT)));
            Channel channel = bootstrap.bind(0).sync().channel();
            File file = new File(filePath);
            long pointer = 0;
            while (true) {
                long length = file.length();
                System.out.println("============size: "+file.length()+",====pointer: "+pointer);
                if (length < pointer) {
                    pointer = length;
                } else if (length > pointer) {
                    RandomAccessFile accessFile = new RandomAccessFile(file, "r");
                    accessFile.seek(pointer);
                    String line;
                    while ((line = accessFile.readLine()) != null) {
                        channel.writeAndFlush(new LogEventDatagram(file.getAbsolutePath(), line));
                    }
                    pointer = accessFile.getFilePointer();
                    accessFile.close();
                }
                Thread.sleep(1000);
            }
        } finally {
            group.shutdownGracefully();
        }

    }

}
