package com.ruoyi.business.controller.ai.model;

import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.model.Model;

/**
 * @author lsy
 * @description 嵌入模型
 * @date 2025/2/21
 */
public interface EmbeddingModel extends Model<EmbeddingRequest, EmbeddingResponse> {



}
