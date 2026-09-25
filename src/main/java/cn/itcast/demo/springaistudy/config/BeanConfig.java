package cn.itcast.demo.springaistudy.config;


import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {


    @Bean
    public VectorStore vectorStore(EmbeddingModel embeddingModel) {
        // SimpleVectorStore.builder(embeddingModel)：使用建造者模式创建 SimpleVectorStore 实例。
        //      - SimpleVectorStore 是 Spring AI 提供的一个基于内存的简单向量数据库实现。
        //      - builder(embeddingModel) 把“翻译官”传给它，以后存数据、查数据时，它就会用这个模型把文本转成向量。
        //      - .build()：最终构建并返回这个 SimpleVectorStore 对象。
        return SimpleVectorStore.builder(embeddingModel).build();
        //SimpleVectorStore = SQLite（轻量级，本地内存，适合测试和学习）
    }

    @Bean
    public TokenTextSplitter tokenTextSplitter(){
        return  TokenTextSplitter.builder().build();
    }

}

