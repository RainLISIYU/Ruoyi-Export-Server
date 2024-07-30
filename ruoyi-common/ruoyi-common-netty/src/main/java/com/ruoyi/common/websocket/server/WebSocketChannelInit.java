package com.ruoyi.common.websocket.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author lsy
 * @description 服务端通道初始化对象
 * @date 2024/7/23
 */
public class WebSocketChannelInit extends ChannelInitializer<Channel> {
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline
                // 心跳处理器
                .addLast(new IdleStateHandler(30, 0, 0))
                // http协议支持
                .addLast(new HttpServerCodec())
                // 大数据流支持
                .addLast(new ChunkedWriteHandler())
                // 将多个信息转化为单一的request或者response对象
                .addLast(new HttpObjectAggregator(8000))
                // http协议升级为ws协议，websocket支持
                .addLast(new WebSocketServerProtocolHandler("/ws", null, true, 65536, false, true))
                // 自定义处理器
                .addLast(new WebSocketHandler());
    }
}
