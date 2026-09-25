package cn.itcast.demo.springaistudy.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class ChatController {

    private final ChatClient chatClient;

    //chatclient.builder由spring ai自动配置注入
    public ChatController(ChatClient.Builder builder){
        this.chatClient = builder.build();
    }

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

}
