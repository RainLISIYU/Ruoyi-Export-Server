package com.ruoyi.common.websocket.server;/*
 *@Author:cq
 *@Date:2024/7/25 11:12
 */

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * @author lsy
 * @description WebSocket服务端
 * @date 2024/7/25
 */
public class NettyWebSocketServer implements ApplicationListener<ContextRefreshedEvent> {

    /**
     * 主线程池
     */
    private EventLoopGroup masterGroup = new NioEventLoopGroup(1);

    /**
     * 从线程池
     */
    private EventLoopGroup slaveGroup = new NioEventLoopGroup();

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        this.init();
    }

    /**
     * 初始化服务端
     */
    public void init() {
        try {
            // 创建服务端
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap
                    // 设置线程组
                    .group(masterGroup, slaveGroup)
                    // 设置channel
                    .channel(NioServerSocketChannel.class)
                    // 设置日志级别
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    // 设置自定义处理器
                    .childHandler(new WebSocketChannelInit());
            // 绑定端口
            ChannelFuture channelFuture = serverBootstrap.bind(8182).sync();
            // 启动服务
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            masterGroup.shutdownGracefully();
            slaveGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        NettyWebSocketServer nettyWebSocketServer = new NettyWebSocketServer();
        nettyWebSocketServer.init();
    }

}
