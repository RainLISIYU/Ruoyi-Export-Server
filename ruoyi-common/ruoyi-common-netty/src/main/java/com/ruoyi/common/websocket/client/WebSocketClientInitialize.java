package com.ruoyi.common.websocket.client;/*
 *@Author:cq
 *@Date:2024/7/25 15:14
 */

import io.netty.channel.*;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.net.URI;

/**
 * @author lsy
 * @description WebSocket客户端初始化连接
 * @date 2024/7/25
 */
public class WebSocketClientInitialize extends ChannelInitializer<Channel> {

    private final URI webSocketURL;

    private final HttpHeaders headers;

    public WebSocketClientInitialize(URI webSocketURL, HttpHeaders headers) {
        this.webSocketURL = webSocketURL;
        this.headers = headers;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new HttpClientCodec())
                .addLast(new ChunkedWriteHandler())
                .addLast(new HttpObjectAggregator(64 * 1024))
                .addLast(new WebSocketClientProtocolHandler(WebSocketClientHandshakerFactory
                        .newHandshaker(webSocketURL, WebSocketVersion.V13, null, false, headers)))
                .addLast(new WebSocketClientHandler());
    }

}
