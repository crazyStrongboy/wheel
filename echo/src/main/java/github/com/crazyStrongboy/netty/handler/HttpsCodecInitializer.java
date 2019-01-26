package github.com.crazyStrongboy.netty.handler;

import github.com.crazyStrongboy.netty.client.ClientOutBoundHandler;
import github.com.crazyStrongboy.netty.server.ClientInBoundHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.concurrent.Future;

import javax.net.ssl.SSLEngine;


public class HttpsCodecInitializer extends ChannelInitializer<Channel> {
    private final SslContext sslContext;
    private final boolean isClient;

    public HttpsCodecInitializer(SslContext sslContext, boolean isClient) {
        this.sslContext = sslContext;
        this.isClient = isClient;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 进行ssl协议加密
        if (sslContext != null) {
            SSLEngine sslEngine = sslContext.newEngine(ch.alloc());
            pipeline.addFirst("sslEngine", new MySslHandler(sslEngine));
        }
        if (isClient) {
            pipeline.addLast("codec", new HttpClientCodec());
            pipeline.addLast(new ClientOutBoundHandler());
        } else {
            pipeline.addLast("codec", new HttpServerCodec());
            pipeline.addLast(new ClientInBoundHandler());
        }
    }


    public static class MySslHandler extends SslHandler {

        public MySslHandler(SSLEngine engine) {
            super(engine);
        }

        @Override
        public Future<Channel> handshakeFuture() {
            return super.handshakeFuture();
        }
    }
}
