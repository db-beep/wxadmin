package com.codeying.langchain4j;

import com.codeying.controller.BaseController;
import com.codeying.entity.LoginUser;
import com.codeying.utils.component.ApiResult;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Flux;

import java.util.*;

@Controller
public class Langchain4jController extends BaseController {
    @Autowired
    private MyAiService myAiService;
    @Autowired
    ChatMemoryProvider chatMemoryProvider;
    @Autowired
    HttpServletRequest request;

    /**
     * 聊天，非流式
     * @param message
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/chat0")
    public ApiResult chat0(String message) {
        LoginUser loginUser = getCurrentUser();
        if(loginUser == null){
            return null;
        }
        String res = myAiService.chat0(loginUser.getRole() + loginUser.getUsername(), message,loginUser.getId());//传入用户ID
        return successData(res);
    }

    /**
     * 聊天，流式返回
     * @param message
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/chat", produces = "text/html;charset=utf-8")
    public Flux<String> chat(String message) {
        LoginUser loginUser = getCurrentUser();
        if(loginUser == null){
            return null;
        }
        return myAiService.chat(loginUser.getRole() + loginUser.getUsername(), message,loginUser.getId());//传入用户ID
    }

    /**
     * 返回消息记录
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/messages")
    public ApiResult chat() {
        LoginUser loginUser = getCurrentUser();
        if(loginUser == null){
            return ApiResult.successData(new ArrayList<>());
        }
        ChatMemory chatMemory = chatMemoryProvider.get(loginUser.getRole() + loginUser.getUsername());
        List<ChatMessage> messages = chatMemory.messages();
        List<Map<String,String>> resData = new ArrayList<>();
        for (ChatMessage message : messages) {
            Map<String,String> data = new HashMap<>();
            if (message instanceof UserMessage) {
                data.put("type", "user");
                data.put("msg",((UserMessage) message).singleText());
                resData.add(data);
            }else if (message instanceof AiMessage) {
                data.put("type", "assistant");
                String msg = ((AiMessage) message).text();
                if(msg!=null  && !msg.isEmpty()){
                    data.put("msg",msg);
                    resData.add(data);
                }
            }
        }
        return ApiResult.successData(resData);
    }


    @RequestMapping("/ai")
    public String ai() {
        return "ai";
    }


}
