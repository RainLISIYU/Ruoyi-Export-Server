package com.ruoyi;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/*
 *@Author:cq
 *@Date:2024/7/16 15:51
 */
public class Main {
    public static void main(String[] args) {
        try (FileInputStream fileInputStream = new FileInputStream("D:\\ruoy\\ruoyi-common\\ruoyi-common-netty\\src\\main\\java\\com\\ruoyi\\common\\netty\\NettyClient.java");
             FileOutputStream fileOutputStream = new FileOutputStream("D:\\dyfile\\i哦哦hi叫哦.txt")){
            FileChannel channel = fileInputStream.getChannel();
            FileChannel outChannel = fileOutputStream.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (channel.read(buffer) != -1) {
                buffer.flip();
                byte[] bytes = new byte[buffer.limit()];
                buffer.get(bytes);
                System.out.println(new String(bytes));
                outChannel.write(buffer);
                buffer.clear();
            }
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}