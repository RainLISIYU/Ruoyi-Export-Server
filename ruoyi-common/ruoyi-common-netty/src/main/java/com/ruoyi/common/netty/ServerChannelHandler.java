package com.ruoyi.common.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lsy
 * @description Netty事件处理器
 * @date 2024/7/18
 */

@ChannelHandler.Sharable
public class ServerChannelHandler extends SimpleChannelInboundHandler<ByteBuf> {

    List<Channel> channels = new ArrayList<>();

    @Override
    public void channelRegistered(ChannelHandlerContext channelHandlerContext) {
        System.out.println("客户端连接了服务器！");
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
        System.out.println("客户端断开连接了服务器！");
        channels.remove(ctx.channel());
    }
}
