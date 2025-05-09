package com.ruoyi.chat;

import com.ruoyi.common.security.annotation.EnableCustomConfig;
import com.ruoyi.common.security.annotation.EnableRyFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author lsy
 * @description 聊天应用启动类
 * @date 2024/7/19
 */
@EnableAsync
@EnableCustomConfig
@EnableRyFeignClients
@SpringBootApplication
public class RuoYiChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(RuoYiChatApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  MCP模块启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                " .-------.       ____     __        \n" +
                " |  _ _   \\      \\   \\   /  /    \n" +
                " | ( ' )  |       \\  _. /  '       \n" +
                " |(_ o _) /        _( )_ .'         \n" +
                " | (_,_).' __  ___(_ o _)'          \n" +
                " |  |\\ \\  |  ||   |(_,_)'         \n" +
                " |  | \\ `'   /|   `-'  /           \n" +
                " |  |  \\    /  \\      /           \n" +
                " ''-'   `'-'    `-..-'              ");
    }

}
