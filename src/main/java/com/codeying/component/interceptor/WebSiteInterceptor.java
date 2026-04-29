package com.codeying.component.interceptor;

import com.codeying.entity.LoginUser;
import com.codeying.utils.component.ApiResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import com.codeying.component.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
/** 网站拦截器 防止没有登录而访问 */
@Component
public class WebSiteInterceptor implements HandlerInterceptor {
  @Autowired protected TokenService tokenService;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    LoginUser user = tokenService.getLoginInfo();
    // 如果用户为空则叫他去登录
    if (user != null && user.getIsWuser()) {
      // 用户不为空，则放行
      return true;
    }
    // 网站列表页是通过这个地址访问的
    if (request.getRequestURI().contains("/webu/") && request.getRequestURI().endsWith("-web")) {
      return true;
    }
    // 下拉框外键数据
    if (request.getRequestURI().contains("/webu/") && request.getRequestURI().endsWith("/select")) {
      return true;
    }
    // 图文，非遗文化科普详情页
    if (request.getRequestURI().contains("/webu/intangibleCultural/detail")) {
      return true;
    }
    // 图文，非遗资讯详情页
    if (request.getRequestURI().contains("/webu/news/detail")) {
      return true;
    }
    // 网站的公共列表 非遗传承活动
    if (request.getRequestURI().contains("/webu/culActivi/list")
        || request.getRequestURI().contains("/webu/culActivi/detail")
        || request.getRequestURI().contains("/webu/culActivi/excel")) {
      return true;
    }
    // 网站的公共列表 非遗文化类型
    if (request.getRequestURI().contains("/webu/cultureType/list")
        || request.getRequestURI().contains("/webu/cultureType/detail")
        || request.getRequestURI().contains("/webu/cultureType/excel")) {
      return true;
    }
    // 否则校验不通过
    unauthorized(response);
    return false;
  }

  ObjectMapper objectMapper = new ObjectMapper();

  private void unauthorized(HttpServletResponse response) throws IOException {
    response.setContentType("application/json; charset=utf-8");
    PrintWriter out = response.getWriter();
    ApiResult<String> apiResult = ApiResult.fail("请先登录！");
    apiResult.setCode(302);
    out.print(objectMapper.writeValueAsString(apiResult));
    out.close();
    out.flush();
  }
}

