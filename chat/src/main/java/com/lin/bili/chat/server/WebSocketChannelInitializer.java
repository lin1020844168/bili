package com.lin.bili.chat.server;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 初始化器
 */
@Component
@ChannelHandler.Sharable
public class WebSocketChannelInitializer extends ChannelInitializer<SocketChannel>{
    @Autowired
    private WebSocketHandler textWebSocketFrameHandler;

    public WebSocketChannelInitializer(WebSocketHandler textWebSocketFrameHandler) {
        this.textWebSocketFrameHandler = textWebSocketFrameHandler;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        //websocket协议本身是基于http协议的，所以这边也要使用http解编码器
        pipeline.addLast(new HttpServerCodec());
        //以块的方式来写的处理器
        pipeline.addLast(new ChunkedWriteHandler());
        //netty是基于分段请求的，HttpObjectAggregator的作用是将请求分段再聚合,参数是聚合字节的最大长度
        pipeline.addLast(new HttpObjectAggregator(8192));

        //websocket定义了传递数据的6中frame类型
        pipeline.addLast(textWebSocketFrameHandler);
        //ws://server:port/context_path
        //ws://localhost:9999/ws
        //参数指的是contex_path
        //必须放最后 大坑
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));

    }
}