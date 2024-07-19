package com.ruoyi.common.netty;/*
 *@Author:cq
 *@Date:2024/7/18 13:51
 */

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * @author lsy
 * @description Netty客户端
 * @date 2024/7/18
 */
public class NettyClient {

    public volatile Bootstrap bootstrap;

    private ChannelFuture connect;

    private final String host;

    private final int port;

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * 创建单例bootstrap
     */
    private void initBoostrap() {
        //创建bootstrap
        if (bootstrap == null) {
            synchronized (NettyClient.class) {
                if (bootstrap == null) {
                    bootstrap = new Bootstrap();
                    //设置线程模型
                    bootstrap
                            .group(new NioEventLoopGroup())
                            .channel(NioSocketChannel.class)
                            .handler(new ChannelInitializer<>() {
                                @Override
                                protected void initChannel(Channel ch) {
                                    ChannelPipeline pipeline = ch.pipeline();
                                    pipeline.addLast(new ClientChannelHandler(NettyClient.this));
                                }
                            });
                }
            }
        }
    }

    /**
     * 连接服务端
     */
    public void connect() {

        initBoostrap();
        //连接服务器
        ChannelFuture connect = bootstrap.connect(host, port).addListener((ChannelFuture future) -> {
            if (future.isSuccess()) {
                System.out.println("连接成功");
            } else {

                System.out.println("连接失败，5s后尝试重新连接");
                TimeUnit.SECONDS.sleep(5);
                connect();

            }
        });
        try {
            connect.sync();
        } catch (InterruptedException e) {
            System.out.println("sync异常");
        }
        this.connect = connect;
    }

    /**
     * 客户端发送消息
     *
     * @param message 消息内容 - String
     */
    public void sendMessage(String message) {
        try {
            //发送消息
            Channel channel = connect.channel();
            byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
            ByteBuf byteBuf = Unpooled.buffer(bytes.length);
            byteBuf.writeBytes(bytes);
            channel.writeAndFlush(byteBuf);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }


    }

    public static void main(String[] args) throws InterruptedException {
        NettyClient client = new NettyClient("127.0.0.1", 8181);
        client.connect();
        client.sendMessage("hello world 1\r\n");
        TimeUnit.SECONDS.sleep(5);
        client.sendMessage("hello world 2");
        client.sendMessage("hello world 3#");
    }

}
