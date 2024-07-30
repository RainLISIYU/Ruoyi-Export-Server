package com.ruoyi.common.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.ChannelInputShutdownReadComplete;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lsy
 * @description Netty事件处理器
 * @date 2024/7/18
 */

@ChannelHandler.Sharable
public class ServerChannelHandler extends SimpleChannelInboundHandler<ByteBuf> {

    //通道列表
    List<Channel> channels = Collections.synchronizedList(new ArrayList<>());

    //通道超时次数
    Map<String, AtomicInteger> timeMap = new ConcurrentHashMap<>();

    //最多超时次数
    private final int maxTime = 3;

    @Override
    public void channelRegistered(ChannelHandlerContext channelHandlerContext) {
        System.out.println(channelHandlerContext.channel().remoteAddress() + "-客户端连接了服务器！");
        channels.add(channelHandlerContext.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        StringBuilder readString = new StringBuilder(byteBuf.toString(StandardCharsets.UTF_8));
        System.out.println("接收到客户端消息:" + readString);

        //返回值
//        readString.append(" accept#");
        //消息群发
        for (Channel channel : channels) {
            if (channel == channelHandlerContext.channel()) {
                ByteBuf buf = Unpooled.copiedBuffer(readString.toString().getBytes(StandardCharsets.UTF_8));
                channel.writeAndFlush(buf);
            }
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent event) {
            switch (event.state()) {
                case READER_IDLE:
                    System.out.println(ctx.channel().remoteAddress() + "--读超时！");
                    String remoteAddress = ctx.channel().remoteAddress().toString();
                    if (! timeMap.containsKey(remoteAddress)) {
                        timeMap.put(remoteAddress, new AtomicInteger(1));
                    }
                    int andIncrement = timeMap.get(remoteAddress).getAndIncrement();
                    //超时次数过多关闭通道
                    if (andIncrement >= maxTime) {
                        ctx.channel().close();
                    }
                    break;
                case WRITER_IDLE:
                    System.out.println(ctx.channel().remoteAddress() + "--写超时！");
                    break;
                case ALL_IDLE:
                    System.out.println(ctx.channel().remoteAddress() + "--读写超时！");
                    break;
            }
        } else if (evt instanceof ChannelInputShutdownReadComplete) {
            System.out.println(ctx.channel().remoteAddress() + "--客户端关闭了连接！");
            channels.remove(ctx.channel());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //异常关闭通道
        System.err.println(cause.getMessage());
        ctx.close();
    }

}
