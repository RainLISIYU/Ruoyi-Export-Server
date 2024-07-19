package com.ruoyi.common.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * @author lsy
 * @description 客户端事件处理器
 * @date 2024/7/19
 */
public class ClientChannelHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private final NettyClient client;

    public ClientChannelHandler(NettyClient client) {
        this.client = client;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        String receiveString = byteBuf.toString(StandardCharsets.UTF_8);
        System.out.println("接收服务端消息：" + receiveString);
        // 设置结束符
        if (receiveString.endsWith("#")) {
            client.bootstrap.config().group().shutdownGracefully();
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws InterruptedException {
        System.out.println(ctx.channel().remoteAddress() + "-断开链接");
        ctx.close();
        System.out.println("5s后尝试重连");
        TimeUnit.SECONDS.sleep(5);
        client.connect();
//                TimeUnit.SECONDS.sleep(5);
//                connect(host, port);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "-异常:" + cause.getMessage());
//                                        System.out.println("异常重连");
//                                        connect(host, port);
    }

}
