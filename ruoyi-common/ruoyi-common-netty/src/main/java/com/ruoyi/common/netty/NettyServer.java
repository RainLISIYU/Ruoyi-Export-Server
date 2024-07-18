package com.ruoyi.common.netty;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

/**
 * @author lsy
 * @description netty服务端
 * @date 2024/7/16
 */
public class NettyServer {

    public void init() {

        //创建主从线程池
        try (EventLoopGroup master = new NioEventLoopGroup();
        EventLoopGroup slave = new NioEventLoopGroup();){

        }


    }

}
