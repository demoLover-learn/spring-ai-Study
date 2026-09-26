package cn.itcast.demo.springaistudy.controller;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
public class ChatController {

    @Autowired
    private ChatClient chatClient;


    @GetMapping("/chat")
    public String chat(@RequestParam String message){
        return chatClient.prompt()//创建第一次对话
                .user(message)//用户输入
                .call()//同步调用
                .content();//取文本结果
    }

    @GetMapping(value = "/stream",produces = "text/html;charset=UTF-8")
    public Flux<String> stream(@RequestParam String message){
        return  chatClient.prompt()
                .user(message)
                .stream()//流式调用
                .content();
    }
    //prompt模板，参数化提示词
    @GetMapping("/poet")
    public String poet(@RequestParam String topic){
        return chatClient.prompt()//创建第一次对话
                .system("你是一位java技术的专家，文本幽默")
                .user(u->u.text("写一首关于{topic}的五言打油诗")
                        .param("topic",topic))//模板参数
                //等价于下面这个
                //.user(u -> {
                //    u.text("写一首关于{topic}的五言打油诗");
                //    u.param("topic", topic);
                //})
                .call()//同步调用
                .content();//取文本结果
    }

    //结构化输出AI直接返回java 对象
    //先定义纪录类
    public record  BookInfo(String title, String author, double price, List<String> tags) {}
    @GetMapping("/book")
    public BookInfo book(@RequestParam String keyword){
        return chatClient.prompt()//创建第一次对话
                .user("推荐一本关于"+keyword+"的经典书，返回书名，作者，大致价格和3个标签")//用户输入
                .call()//同步调用
                .entity(BookInfo.class);//直接映射为java对象
    }

    //工具调用@Tools
    @GetMapping("/agent")
    public String agent(@RequestParam String message){
        return  chatClient.prompt()
                .user(message)
                .call()
                .content();
    }


    //记忆功能的接口
    @GetMapping("/memory")
    public String memory(@RequestParam String sessionId,@RequestParam String message){
        return chatClient.prompt()
                .user(message)
                .advisors(a->a.param(ChatMemory.CONVERSATION_ID,sessionId))
                .call()
                .content();
    }
}
