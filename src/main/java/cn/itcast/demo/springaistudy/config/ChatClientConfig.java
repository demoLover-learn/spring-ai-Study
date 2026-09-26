package cn.itcast.demo.springaistudy.config;


import cn.itcast.demo.springaistudy.tools.OrderTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {



    @Bean
    public ChatClient chatClient(ChatClient.Builder builder, OrderTools orderTools
    , ChatMemory chatMemory, VectorStore vectorStore) {
        return builder
                //注册工具的调用
                .defaultTools(orderTools)
                //Advisors顾问/增强器，类似于餐厅的服务员
                .defaultAdvisors(new SimpleLoggerAdvisor(),
                        //历史会话记忆(MessageChatMemoryAdvisor.builder(chatMemory).build()
                        MessageChatMemoryAdvisor.builder(chatMemory).build(),
                        QuestionAnswerAdvisor.builder(vectorStore)
                        //构建了一个 QuestionAnswerAdvisor代表用哪个向量库、怎么搜、搜多少、过滤什么。
                                .searchRequest(SearchRequest.builder()
                                        .topK(5)
                                        //含义：返回相似度最高的 5 个文档片段。
                                        //作用：控制检索结果的数量。太少可能漏掉关键信息，太多会引入噪声并增加 Token 消耗。
                                        .similarityThreshold(0.75)
//                                        含义：相似度阈值，只有相似度 ≥ 0.75 的结果才会被返回。
//                                        作用：过滤掉那些“不太相关”的文档，避免把无关内容塞给大模型。
//                                        .filterExpression("type=='faq'")
                                       // 含义：对检索结果做元数据过滤，只保留元数据中 type 字段等于 'faq' 的文档。
                                       // 作用：如果你的向量库里混有多种类型的文档（比如 FAQ、产品手册、政策文件），可以通过这个表达式限定只搜某一类。
                                       // 前提：你在往向量库写入文档时，必须给文档设置了对应的元数据，例如
                                        .build())
                                .build())//RAG知识库构建
                .build();
    }
}
