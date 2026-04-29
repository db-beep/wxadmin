package com.codeying.component.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.codeying.component.interceptor.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/** 配置类 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

  @Autowired LoginInterceptor loginInterceptor;
  @Autowired WebSiteInterceptor webSiteInterceptor;

  /**
   * 除了登录页面，所有页面都要验证是否登录
   *
   * @param registry
   */
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    // 需要登录才可访问
    String includePatterns =
        "/admin/**,/institution/**,/user/**,/intangibleCultural/**,/news/**,/culActivi/**,/baom/**,/cultureType/**,/fankui/**,/notice/**,/userComment/**,";
    registry.addInterceptor(loginInterceptor).addPathPatterns(includePatterns.split(","));

    String includePatterns2 =
        "/webu/personal,/webu/admin/**,/webu/institution/**,/webu/user/**,/webu/intangibleCultural/**,/webu/news/**,/webu/culActivi/**,/webu/baom/**,/webu/cultureType/**,/webu/fankui/**,/webu/notice/**,/webu/userComment/**";
    registry.addInterceptor(webSiteInterceptor).addPathPatterns(includePatterns2.split(","));
  }

  @Value("${spring.datasource.url}")
  String url;

  private String sqlserver = "sqlserver";
  private String mysql = "mysql";

  @Bean
  public MybatisPlusInterceptor mybatisPlusInterceptor() {
    MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
    String type = url.contains(sqlserver) ? sqlserver : mysql;
    interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.getDbType(type)));
    return interceptor;
  }
}

