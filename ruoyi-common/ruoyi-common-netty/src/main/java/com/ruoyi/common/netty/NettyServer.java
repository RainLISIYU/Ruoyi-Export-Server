package com.ruoyi.common.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

/**
 * @author lsy
 * @description netty服务端
 * @date 2024/7/16
 */
public class NettyServer {

    private static final int SERVER_PORT = 8181;

    public void init() {

        //创建主从线程池
        EventLoopGroup master = new NioEventLoopGroup();
        EventLoopGroup slave = new NioEventLoopGroup();
        try (master;slave){
            //创建爱你服务器的初始化引导对象
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //配置引导对象
            serverBootstrap
                    //设置当前Netty线程模型
                    .group(master, slave)
                    //设置Channel的类型
                    .channel(NioServerSocketChannel.class)
                    //设置事件处理器
                    .childHandler(new ChannelInitializer<>() {

                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
//                            pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Unpooled.wrappedBuffer("\r\n".getBytes())));
//                            pipeline.addLast(new LineBasedFrameDecoder(8192));
                            pipeline.addLast(new IdleStateHandler(3, 0, 0, TimeUnit.SECONDS))
                                    .addLast(new ServerChannelHandler())
                                    .addLast(new LoggingHandler());
                        }
                    });

            //绑定端口
            ChannelFuture future = serverBootstrap.bind(SERVER_PORT);
            try {
                future.sync();
                LocalDate localDate = LocalDate.parse("2024-06-12");
                System.out.println(localDate.withDayOfMonth(21) + "-Netty Server Started");
                //阻塞接收消息
                future.channel().closeFuture().sync();
            }catch (InterruptedException e) {
                System.err.println("Netty Server Started Error" + e.getMessage());
            } finally {
                future.channel().close();
            }
        } finally {
            master.shutdownGracefully();
            slave.shutdownGracefully();
        }


    }

    public static void main(String[] args) {
        new NettyServer().init();
    }

}
