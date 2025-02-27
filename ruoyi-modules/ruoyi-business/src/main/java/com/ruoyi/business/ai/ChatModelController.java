package com.ruoyi.business.ai;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

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
    public String chat(@RequestParam("input") String input) {
        ChatResponse call = chatModel.call(new Prompt(input));
        return call.getResult().getOutput().getContent();
    }

    /**
     * 流式输出
     * @param input 输入
     * @return 输出
     */
    @RequestMapping("/stream/chat")
    public Flux<String> streamChat(@RequestParam("input") String input) {
        return chatModel.stream(new Prompt(input)).map(response -> response.getResult().getOutput().getContent());
    }

}
