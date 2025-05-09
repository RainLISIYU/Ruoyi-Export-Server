package com.ruoyi.business.ai.configuration;

import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentReader;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author lsy
 * @description rag配置类
 * @date 2025/2/25
 */
@Configuration
public class RagConfig {

    /**
     * 向量数据库
     *
     * @param embeddingModel 嵌入模型
     * @return 向量数据库
     */
    @Bean
    public VectorStore myVectorStore(EmbeddingModel embeddingModel) {
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(embeddingModel).build();

        // 生成文档
        List<Document> documents = List.of(
                new Document("产品说明书：产品名称：智能机器人\n" +
                        "产品描述：智能机器人是一个智能设备，能够自动完成各种任务\n" +
                        "功能：\n" +
                        "1.自动导航：机器人能够自动导航到指定位置\n" +
                        "2.自动抓取：机器人能够自动抓取物品。\n" +
                        "3.自动恢复：机器人能够自动放置物品。\n")
        );

        // 加入向量数据库
        simpleVectorStore.add(documents);
        // 将文档导入向量数据库
//        DocumentReader documentReader = new PagePdfDocumentReader("classpath:/data/spring_ai_alibaba_quickstart.pdf");
//        List<Document> pdfDocuments = documentReader.get();
//        simpleVectorStore.add(pdfDocuments);
        return simpleVectorStore;
    }

}
