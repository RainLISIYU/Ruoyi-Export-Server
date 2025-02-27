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
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

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

    private ChatClient chatClient;

    @Resource
    private VectorStore myVectorStore;

    public ChatController(ChatModel chatModel) {
        this.chatClient = ChatClient.builder(chatModel)
//                .defaultSystem("You are a teacher.")
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .defaultAdvisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory()))
                .defaultOptions(
                        new OllamaOptions.Builder().topP(0.7).build()
                )
                .build();
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
        String content = chatResponse.getResult().getOutput().getContent();
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
                .advisors(new QuestionAnswerAdvisor(myVectorStore))
                .advisors(advisor ->
                        advisor
                                .param(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, finalChatId)
                                .param(AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY, 100))
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
        String content = chatResponse.getResult().getOutput().getContent();
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
                .advisors(new QuestionAnswerAdvisor(myVectorStore))
                .call()
                .content();
    }

    @GetMapping("/similar")
    public List<Document> similar(@RequestParam("input") String input) {
        return myVectorStore.similaritySearch(SearchRequest.builder()
                .query(input)
                .topK(5)
                .similarityThreshold(0.7)
                .build());
    }

}
