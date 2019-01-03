package github.com.crazyStrongboy.server;

import github.com.crazyStrongboy.bean.RpcRequest;
import github.com.crazyStrongboy.proto.RRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author mars_jun
 * @version 2019/1/1 14:38
 */
public class RpcProtoHandler extends ChannelInboundHandlerAdapter {

    private Map<String, Object> handlers;

    public RpcProtoHandler(Map<String, Object> handlers) {
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
        RRequest.Request request = (RRequest.Request) msg;
        Object result;
        String className = request.getClassName();
        if (handlers.containsKey(className)) {
            Object object = handlers.get(className);
            Class<?>[] parameterTypes = new Class[request.getTypesCount()];
            int i = 0;
            for (String type : request.getTypesList()) {
                parameterTypes[i] = Class.forName(type);
                i++;
            }
            Method method = object.getClass().getMethod(request.getMethodName(), parameterTypes);
            result = method.invoke(object, request.getParamsList().toArray());
        } else {
            result = "has no support method !";
        }
        ctx.write(result);
        ctx.flush();
        ctx.close();
    }
}
