package com.codeying.langchain4j;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;
import reactor.core.publisher.Flux;

@AiService(
        wiringMode = AiServiceWiringMode.EXPLICIT,//手动装配
        chatModel = "openAiChatModel",//指定模型
        streamingChatModel = "openAiStreamingChatModel",
        chatMemory = "chatMemory",//配置会话记忆对象
        chatMemoryProvider = "chatMemoryProvider",//配置会话记忆提供者对象
        contentRetriever = "contentRetriever",//配置向量数据库检索对象
        tools = "aiTool"
)
public interface MyAiService {

    /**
     * 仅仅需要修改这部分代码！
     */
    String SystemMessage = "你是专业的非遗活动传承专家，可以给用户提供如下功能：\n" +
            "1.咨询非遗文化相关知识\n" +
            "2.查询所有正在报名中的非遗传承活动\n" +
            "3.帮助用户报名活动\n" +
            "说明：\n" +
            "    1.如果用户每次问的问题和以上的3个记录没关系，那么提示用户你可以做什么，是否帮ta查询所有正在报名中的非遗活动 报名活动\n" +
            "    2.报名活动之前，询问用户是否填写备注\n" +
            "\n用户ID是{{userid}}";

    //流式
    @SystemMessage(SystemMessage)
    //@UserMessage("xxx{{msg}}")
    Flux<String> chat(/*@V("msg")*/@MemoryId String memoryId, @UserMessage String message,@V("userid")String userid);

    //非流式
    @SystemMessage(SystemMessage)
    //@UserMessage("xxx{{msg}}")
    String chat0(/*@V("msg")*/@MemoryId String memoryId, @UserMessage String message,@V("userid")String userid);
}
