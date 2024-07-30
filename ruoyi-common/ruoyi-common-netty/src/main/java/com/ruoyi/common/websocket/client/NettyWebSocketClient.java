package com.ruoyi.common.websocket.client;/*
 *@Author:cq
 *@Date:2024/7/25 15:26
 */

import com.alibaba.fastjson2.JSON;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.Headers;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * @author lsy
 * @description WebSocket客户端
 * @date 2024/7/25
 */
public class NettyWebSocketClient {

    private static final String WEBSOCKET_URL = "ws://127.0.0.1:8182/ws";

    private Bootstrap bootstrap = new Bootstrap();

    /**
     * 客户端初始化
     */
    public Channel init() {

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            // WebSocket服务端地址
            URI uri = new URI(WEBSOCKET_URL);
            int port = uri.getPort();
            // 自定义请求头
            HttpHeaders headers = new DefaultHttpHeaders();
            headers.add("userId", "0001");
            WebSocketClientInitialize initialize = new WebSocketClientInitialize(uri, headers);
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(initialize);
            return bootstrap.connect(uri.getHost(), port).sync().channel();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            group.shutdownGracefully();
        }
        return null;
    }

    public static void main(String[] args) {
        NettyWebSocketClient client = new NettyWebSocketClient();
        Channel channel = client.init();
        if (channel != null) {
            for (;;) {
                Scanner sc = new Scanner(System.in);
                String message = sc.nextLine();
                if (message.equals("exit")) {
                    client.bootstrap.config().group().shutdownGracefully();
                    break;
                }
                Map<String, String> chatMap = new HashMap<>();
                chatMap.put("userId", "0001");
                chatMap.put("message", message);
                chatMap.put("toUserId", "0002");
                channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(chatMap)));
            }

        }
    }

}
