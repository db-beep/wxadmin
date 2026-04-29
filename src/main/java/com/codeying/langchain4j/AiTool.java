package com.codeying.langchain4j;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.codeying.controller.BaseController;
import com.codeying.entity.Baom;
import com.codeying.entity.CulActivi;
import com.codeying.utils.CommonUtils;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class AiTool extends BaseController {

    @Autowired
    HttpServletRequest request;

    @Tool("报名非遗活动")
    public String func1(
            @P("用户ID") String userid,
            @P("非遗活动ID") String acid,
            @P("报名备注") String remark
    ){
        Baom baom = new Baom();
        baom.setId(CommonUtils.newId());
        baom.setUserid(userid);
        baom.setAcid(acid);
        CulActivi culActivi = culActiviService.getById(acid);
        baom.setJig(culActivi.getZhubf());
        baom.setRemark(remark);
        baom.setCreatetime(new Date());
        baom.setState("待审核");
        baomService.save(baom);
        return "报名失败，先提示用户先登录再预约！";//一定不能返回空的内容
    }

    @Tool("查询所有正在报名中的非遗传承活动")
    public  Object func2(){
        List<CulActivi> culActivis = culActiviService.list(new LambdaQueryWrapper<CulActivi>().ge(CulActivi::getStarttime, new Date()));
        if(culActivis.size()==0){
            return "没有正在报名中的非遗传承活动！";//一定不能返回空的内容
        }
        StringBuilder s = new StringBuilder();
        for (CulActivi culActivi : culActivis) {
            s.append(culActivi.getName())
                    .append(" \n开始时间：")
                    .append(culActivi.getStarttime())
                    .append(" \n结束时间:")
                    .append(culActivi.getEndtime())
                    .append(" \n活动内容:")
                    .append(culActivi.getContent())
                    .append("\n 报名说明:")
                    .append(culActivi.getBaomsm())
                    .append("\n 限制报名人数:")
                    .append(culActivi.getCount0())
                    .append("\n 已报名人数:")
                    .append(culActivi.getCount())
                    .append("\n");
        }
        return s.toString();
    }
}
