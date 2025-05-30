package com.ruoyi.business;

import com.ruoyi.common.security.annotation.EnableCustomConfig;
import com.ruoyi.common.security.annotation.EnableRyFeignClients;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * @author lsy
 * @description 启动类
 * @date 2024/3/6
 */
@EnableCustomConfig
@EnableRyFeignClients
@SpringBootApplication(excludeName = {
        "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration",
        "org.springframework.ai.autoconfigure.mcp.client.SseHttpClientTransportAutoConfiguration"})
public class RuoYiBusinessApplication {
    public static void main(String[] args) {
        SpringApplication.run(RuoYiBusinessApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  开发模块启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
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

//    @Bean
//    public CommandLineRunner predefinedQuestions(ChatClient.Builder chatClientBuilder,
//                                                 ToolCallbackProvider tools,
//                                                 ConfigurableApplicationContext context) {
//        return args -> {
//            // 构建ChatClient并注入MCP工具
//            var chatClient = chatClientBuilder
//                    .defaultTools(tools)
//                    .build();
//
//            // 使用ChatClient与LLM交互
//            String userInput = "北京的天气如何？";
//            System.out.println("\n>>> QUESTION: " + userInput);
//            System.out.println("\n>>> ASSISTANT: " + chatClient.prompt(userInput).call().content());
//
//            context.close();
//        };
//    }
}
