package github.com.crazyStrongboy.netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * @author mars_jun
 * @version 2019/1/19 23:17
 */
public class ClientOutBoundHandler extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        System.err.println("out//////////");
        super.write(ctx, msg, promise);
    }
}
