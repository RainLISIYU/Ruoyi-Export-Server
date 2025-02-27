package com.ruoyi.business.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lsy
 * @description 文档检索控制器
 * @date 2025/2/26
 */
@RestController
public class RetrieverController {

    private DocumentRetriever documentRetriever;

    private ChatModel chatModel;

    @Autowired
    public void RestController(VectorStore vectorStore, ChatModel chatModel) {
        this.documentRetriever = VectorStoreDocumentRetriever.builder().vectorStore(vectorStore).build();
        this.chatModel = chatModel;
    }

    @GetMapping("/ai/retrieve")
    public List<Document> retrieve(@RequestParam(value = "input") String input) {
        return documentRetriever.retrieve(new Query(input));
    }

    @GetMapping("/ai/chatForm")
    public String chatForm(@RequestParam(value = "input") String input) {
        ActorsFilms actorsFilms = ChatClient.create(chatModel).prompt()
                .user(u -> u.text("Generate the filmography of 5 movies for {actor}.")
                        .param("actor", "Tom Hanks"))
                .call()
                .entity(ActorsFilms.class);
        assert actorsFilms != null;
        return actorsFilms.toString();
    }

}

record ActorsFilms(String actor, List<String> movies){}
