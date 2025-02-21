package com.ruoyi.business.controller.ai;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lsy
 * @description chat模型控制器
 * @date 2025/2/21
 */
@RestController
@RequestMapping("/chatModel")
public class ChatModelController {

    private final ChatModel chatModel;

    public ChatModelController(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    /**
     * 模型对话
     *
     * @param input 输入
     * @return 输出
     */
    @RequestMapping("/chat")
    public String chat(String input) {
        ChatResponse call = chatModel.call(new Prompt(input));
        return call.getResult().getOutput().getContent();
    }

}
