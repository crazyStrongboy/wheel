package github.com.crazyStrongboy.netty.datagram.client;

import github.com.crazyStrongboy.netty.datagram.server.LogEventDatagram;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author mars_jun
 * @version 2019/1/23 23:25
 */
public class LogEventHandler extends SimpleChannelInboundHandler<LogEventDatagram> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LogEventDatagram msg) throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append(msg.getReceived());
        builder.append("[");
        builder.append(msg.getAddress().toString());
        builder.append("][");
        builder.append(msg.getFileName());
        builder.append("]");
        builder.append(":");
        builder.append(msg.getMsg());
        System.err.println(builder.toString());
    }
}
