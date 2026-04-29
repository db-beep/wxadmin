package com.codeying.langchain4j;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.loader.ClassPathDocumentLoader;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import dev.langchain4j.store.memory.chat.InMemoryChatMemoryStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class Langchain4jConfig {
    @Autowired
    private EmbeddingModel embeddingModel;

    /**
     * 构建会话记忆对象
     * @return
     */
    @Bean
    public ChatMemory chatMemory(){
        MessageWindowChatMemory memory = MessageWindowChatMemory.builder()
                .maxMessages(20)
                .build();
        return memory;
    }

    /**
     * 内存存储
     * @return
     */
    @Bean
    public ChatMemoryStore chatMemoryStore() {
        return new InMemoryChatMemoryStore(); // 默认内存实现
    }
    /**
     * 构建ChatMemoryProvider对象
     * @return
     */
    @Bean
    public ChatMemoryProvider chatMemoryProvider(ChatMemoryStore chatMemoryStore){
        ChatMemoryProvider chatMemoryProvider = new ChatMemoryProvider() {
            @Override
            public ChatMemory get(Object memoryId) {
                return MessageWindowChatMemory.builder()
                        .id(memoryId)
                        .maxMessages(20)
                        .chatMemoryStore(chatMemoryStore)
                        .build();
            }
        };
        return chatMemoryProvider;
    }

//    /**
//     * 构建向量数据库操作对象
//     * Springboot默认构建了embeddingStore的对象, 这个对象的名字不能重复,所以这里使用store
//     * @return
//     */
//    @Bean
//    public EmbeddingStore store(){
//        //ApachePdfBoxDocumentParser 解析pdf 已经引入Maven依赖
//        //ApachePoiDocumentParser 解析doc、PPT、xls
//        //TextDocumentParser 是纯本文
//        //不过默认提供的解析器几乎可以解析全部
//        //1.加载文档进内存
//        List<Document> documents = ClassPathDocumentLoader.loadDocuments("content",new ApachePdfBoxDocumentParser());
//        //2.构建向量数据库操作对象  操作的是内存版本的向量数据库
//        InMemoryEmbeddingStore store = new InMemoryEmbeddingStore();
//        //构建文档分割器对象
//        DocumentSplitter ds = DocumentSplitters.recursive(500,100);
//        //3.构建一个EmbeddingStoreIngestor对象,完成文本数据切割,向量化, 存储
//        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
//                .embeddingStore(store)
//                .documentSplitter(ds)
//                .embeddingModel(embeddingModel)
//                .build();
//        ingestor.ingest(documents);
//        return store;
//    }
//
//    /**
//     * 构建向量数据库检索对象
//     * @param store
//     * @return
//     */
//    @Bean
//    public ContentRetriever contentRetriever(EmbeddingStore store){
//        return EmbeddingStoreContentRetriever.builder()
//                .embeddingStore(store)
//                .minScore(0.5)//最低分数
//                .maxResults(3) // 仅发余弦相似度匹配程度最高的那几条
//                .embeddingModel(embeddingModel)
//                .build();
//    }
}
