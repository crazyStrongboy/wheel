package github.com.crazyStrongboy.netty.datagram.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author mars_jun
 * @version 2019/1/23 22:41
 */
public class DatagramEncode extends MessageToMessageEncoder<LogEventDatagram> {
    private final InetSocketAddress address;

    public DatagramEncode(InetSocketAddress address) {
        this.address = address;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, LogEventDatagram msg, List<Object> out) throws Exception {
        byte[] file = msg.getFileName().getBytes(CharsetUtil.UTF_8);
        byte[] msgs = msg.getMsg().getBytes(CharsetUtil.UTF_8);
        ByteBuf buf = ctx.alloc().buffer(file.length + msgs.length + 1);
        buf.writeLong(msg.getReceived());
        buf.writeBytes(file);
        buf.writeByte(LogEventDatagram.separate);
        buf.writeBytes(msgs);
        out.add(new DatagramPacket(buf, address));
    }
}
