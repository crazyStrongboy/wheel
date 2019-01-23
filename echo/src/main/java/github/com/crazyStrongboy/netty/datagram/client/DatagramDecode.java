package github.com.crazyStrongboy.netty.datagram.client;

import github.com.crazyStrongboy.netty.datagram.server.LogEventDatagram;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;

import java.util.List;

/**
 * @author mars_jun
 * @version 2019/1/23 23:12
 */
public class DatagramDecode extends MessageToMessageDecoder<DatagramPacket> {
    @Override
    protected void decode(ChannelHandlerContext ctx, DatagramPacket msg, List<Object> out) throws Exception {
        ByteBuf content = msg.content();
        int idx = content.indexOf(0, content.readableBytes(), LogEventDatagram.separate);
        String fileName = content.slice(0, idx).toString(CharsetUtil.UTF_8);
        String message = content.slice(idx + 1, content.readableBytes()).toString(CharsetUtil.UTF_8);
        LogEventDatagram logEventDatagram = new LogEventDatagram(fileName, message, System.currentTimeMillis(), msg.sender());
        out.add(logEventDatagram);
    }
}
