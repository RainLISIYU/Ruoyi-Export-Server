package com.ruoyi.common.websocket.server;/*
 *@Author:cq
 *@Date:2024/7/23 11:10
 */

import com.alibaba.fastjson2.JSONObject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.util.concurrent.GlobalEventExecutor;
import io.netty.util.internal.StringUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lsy
 * @description WebSocket自定义处理器
 * @date 2024/7/23
 */
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    /**
     * channel池
     */
    public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * channel映射，用于根据用户id获取channel
     */
    public static ConcurrentHashMap<String, Channel> channelMap = new ConcurrentHashMap<>();

    /**
     * 通道绑定
     */
    public static ConcurrentHashMap<String, String> bindMap = new ConcurrentHashMap<>(16);
    public static List<Channel> channelList = Collections.synchronizedList(new ArrayList<>());

    /**
     * 通道就绪
     *
     * @param ctx 通道实体
     * @throws Exception 异常
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //放入通道队列
        channels.add(channel);
        //打印
        System.out.println(channel.remoteAddress() + "-接入通道");
    }

    /**
     * 消息接收事件
     *
     * @param ctx           the {@link ChannelHandlerContext} which this {@link SimpleChannelInboundHandler}
     *                      belongs to
     * @param textWebSocketFrame           the message to handle
     * @throws Exception    异常
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame textWebSocketFrame) throws Exception {
        String msg = textWebSocketFrame.text();
        System.out.println("通道：" + ctx.channel().remoteAddress() + "发送消息-" + msg);
        //消息处理
        JSONObject parse = JSONObject.parse(msg);
        //获取来源用户id
        String userId = parse.getString("userId");
        String toUserId = parse.getString("toUserId");
        if (userId != null && toUserId != null && channelMap.containsKey(toUserId)) {
            //获取目标用户channel
            Channel channel = channelMap.get(toUserId);
            //发送消息
            channel.writeAndFlush(new TextWebSocketFrame(msg));
        }
    }

    /**
     * 客户端事件触发
     *
     * @param ctx ChannelHandlerContext
     * @param evt event
     * @throws Exception 异常
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        System.out.println("用户事件触发-" + evt.getClass().getName());
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete handshakeComplete) {
            //鉴权处理
            HttpHeaders headers = handshakeComplete.requestHeaders();
            //设置channel映射
            String userId = headers.get("userId");
            if (StringUtil.isNullOrEmpty(userId)) {
                ctx.close();
            }
            channelMap.put(userId, ctx.channel());
        }
        super.userEventTriggered(ctx, evt);
    }

    /**
     * 异常关闭
     *
     * @param ctx ChannelHandlerContext
     * @param cause 异常
     * @throws Exception 异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        channels.remove(ctx.channel());
        channelMap.entrySet().removeIf(next -> {
            boolean flag = next.getValue().equals(ctx.channel());
            if (flag) {
                System.out.println("移除用户" + next.getKey() + "的通道");
            }
            return flag;
        });
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println(ctx.channel().remoteAddress() + "-断开连接");

    }

    /**
     * 异常或断开连接时移除通道
     *
     * @param ctx
     */
    private void removeChannel(ChannelHandlerContext ctx) {
        channels.remove(ctx.channel());
        channelMap.entrySet().removeIf(next -> {
            boolean flag = next.getValue().equals(ctx.channel());
            if (flag) {
                System.out.println("移除用户" + next.getKey() + "的通道");
            }
            return flag;
        });
        ctx.close();
    }
}
