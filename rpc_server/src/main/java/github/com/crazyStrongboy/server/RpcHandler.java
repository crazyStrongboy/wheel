package github.com.crazyStrongboy.server;

import github.com.crazyStrongboy.bean.RpcRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author mars_jun
 * @version 2019/1/1 14:38
 */
public class RpcHandler extends ChannelInboundHandlerAdapter {

    private Map<String, Object> handlers;

    public RpcHandler(Map<String, Object> handlers) {
        this.handlers = handlers;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(cause);
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcRequest request = (RpcRequest) msg;
        Object result;
        String className = request.getClassName();
        if (handlers.containsKey(className)) {
            Object object = handlers.get(className);
            Method method = object.getClass().getMethod(request.getMethodName(), request.getTypes());
            result = method.invoke(object, request.getParams());
        } else {
            result = "has no support method !";
        }
        ctx.write(result);
        ctx.flush();
        ctx.close();
    }
}
