package com.ruoyi.common.websocket.client;/*
 *@Author:cq
 *@Date:2024/7/25 15:20
 */

import com.alibaba.fastjson2.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler;

/**
 * @author lsy
 * @description websocket客户端处理器
 * @date 2024/7/25
 */
public class WebSocketClientHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String text = msg.text();
        JSONObject receiveObj = JSONObject.parse(text);
        String message = receiveObj.getString("message");
        String userId = receiveObj.getString("userId");
        System.out.println("收到"+ userId +"的信息：" + message);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (WebSocketClientProtocolHandler.ClientHandshakeStateEvent
                .HANDSHAKE_COMPLETE.equals(evt)) {
            System.out.println("握手成功-" + ctx.channel().remoteAddress());
        }
        super.userEventTriggered(ctx, evt);
    }
}
