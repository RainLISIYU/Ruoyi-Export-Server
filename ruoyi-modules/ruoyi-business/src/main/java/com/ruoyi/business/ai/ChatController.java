package com.ruoyi.business.ai;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.common.utils.UuidUtils;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.log.annotation.Log;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.autoconfigure.vectorstore.pgvector.PgVectorStoreAutoConfiguration;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.document.Document;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author lsy
 * @description Ai Chat Controller
 * @date 2025/2/19
 */
@RestController
@RequestMapping("/aiChat")
public class ChatController {

    @Value("${spring.ai.type}")
    private String aiType;

    private ChatClient chatClient;

//    @Resource
//    private VectorStore myVectorStore;

    public ChatController(OpenAiChatModel openAiChatModel, ToolCallbackProvider tools) {
        InMemoryChatMemory inMemoryChatMemory = new InMemoryChatMemory();
//        if ("ollama".equals(aiType)) {
//            this.chatClient = ChatClient.builder(ollamaChatModel)
////                .defaultSystem("You are a teacher.")
//                    .defaultAdvisors(new SimpleLoggerAdvisor())
//                    .defaultAdvisors(new MessageChatMemoryAdvisor(inMemoryChatMemory))
//                    .defaultOptions(
//                            new OllamaOptions.Builder().topP(0.7).build()
//                    )
//                    .defaultSystem("你需要使用中文回答问题。" +
//                            "你只需要根据提供资料回答问题，不相关的问题回复：无法解决该问题")
//                    .build();
//        } else {
        this.chatClient = ChatClient.builder(openAiChatModel)
                .defaultTools(tools)
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .defaultAdvisors(new MessageChatMemoryAdvisor(inMemoryChatMemory))
                .defaultSystem("你需要使用中文回答问题。" +
                        "你只需要根据提供资料回答问题，不相关的问题回复：无法解决该问题")
                .build();
//        }

    }

    /**
     * 简单调用
     *
     * @param chatId 聊天id
     * @param input 输入
     * @return 输出
     */
    @GetMapping("/simple/chat")
    public String chat(@RequestParam(value = "chatId", required = false) String chatId, @RequestParam("input") String input) {
        if (StringUtils.isEmpty(chatId)) {
            chatId = UuidUtils.generateUuid();
        }
        String finalChatId = chatId;
        ChatResponse chatResponse = chatClient
                .prompt()
                .user(input)
                .advisors(advisor ->
                        advisor
                                .param(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, finalChatId)
                                .param(AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY, 100))
                .call()
                .chatResponse();
        assert chatResponse != null;
        String content = chatResponse.getResult().getOutput().getText();
        content = content.replace("\n", "").replace("<think>", "").replace("</think>", "");
        return content;
    }

    /**
     * 流式调用
     * @param input 输入
     * @param response 输出t
     * @return
     */
    @GetMapping(value = "/stream/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamChat(@RequestParam(value = "chatId", required = false) String chatId, @RequestParam("input") String input,  HttpServletResponse response) {
        if (StringUtils.isEmpty(chatId)) {
            chatId = UuidUtils.generateUuid();
        }
        response.setCharacterEncoding("UTF-8");
        String finalChatId = chatId;
        Flux<String> chatResponse = chatClient
                .prompt()
//                .functions("getWeatherFunction")
                .user(input)
//                .advisors(new QuestionAnswerAdvisor(myVectorStore))
                .advisors(advisor ->
                        advisor
                                .param(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, finalChatId)
                                .param(AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .stream()
                .content();
        AtomicReference<String> content = new AtomicReference<>("");
        return chatResponse.map(data -> {
            content.set(content.get() + data);
            Map<String, String> map = new HashMap<>();
            map.put("chatId", finalChatId);
            map.put("data", content.get());
            return JSON.toJSONString(map);
        });
    }

    /**
     * 返回chatResponse
     *
     * @param input 输入内容
     * @return 返回
     */
    @Log(title = "chatResponse")
    @GetMapping("/chatResponse")
    public ChatResponse chatResponse(@RequestParam("input") String input) {
        ChatResponse chatResponse = chatClient.prompt(input).call().chatResponse();
        assert chatResponse != null;
        String content = chatResponse.getResult().getOutput().getText();
        content = content.replace("\n", "").replace("<think>", "");
        return chatResponse;
    }

    record ActorFilms(String actor, List<String> movies) {
    }

    /**
     * 实体类接收响应
     *
     * @return 结果
     */
    @Log(title = "entityChat")
    @GetMapping("/entityChat")
    public String entityChat() {
        ActorFilms actorFilms = chatClient.prompt()
                .user("Generate the filmography for a random actor.")
                .call()
                .entity(ActorFilms.class);
        assert actorFilms != null;
        return actorFilms.toString();
    }

    /**
     * 加入rag后输出
     *
     * @param input 输入
     * @return 输出
     */
    @GetMapping(value = "/rag/chat")
    public String ragChat(@RequestParam("input") String input) {
        // 发起聊天
        return chatClient.prompt()
                .user(input)
//                .advisors(new QuestionAnswerAdvisor(myVectorStore))
                .call()
                .content();
    }

//    @GetMapping("/similar")
//    public List<Document> similar(@RequestParam("input") String input) {
//        return myVectorStore.similaritySearch(SearchRequest.builder()
//                .query(input)
//                .topK(5)
//                .similarityThreshold(0.7)
//                .build());
//    }

}
