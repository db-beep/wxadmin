package com.codeying.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import org.springframework.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.codeying.component.*;
import com.codeying.component.utils.*;
import com.codeying.utils.component.*;
import com.codeying.utils.*;
import com.codeying.entity.*;
import com.codeying.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import java.io.InputStream;
import java.util.*;
import java.io.File;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.FileUtils;
import jakarta.servlet.ServletOutputStream;
import java.math.BigDecimal;

/** 非遗资讯控制器 关于非遗资讯的增删改查操作都在这 */
@Controller
@RequestMapping({"news", "webu/news"})
public class NewsController extends BaseController {

  /** 非遗资讯网站列表页 */
  @RequestMapping("list-web")
  @ResponseBody
  public ApiResult<Map<String, Object>> listweb() {
    Map<String, Object> respMap = new HashMap<>();
    QueryWrapper<News> queryWrapper = getQueryWrapper();
    List<News> newsList = newsService.list(queryWrapper);

    // 循环遍历list数据，统计、获取外键数据
    for (News e : newsList) {

      // 非遗资讯收藏点赞数
      int starCount =
          (int)
              starService.count(
                  new LambdaQueryWrapper<Star>()
                      .eq(Star::getItemid, e.getId())
                      .eq(Star::getType, "收藏"));
      int praiseCount =
          (int)
              starService.count(
                  new LambdaQueryWrapper<Star>()
                      .eq(Star::getItemid, e.getId())
                      .eq(Star::getType, "点赞"));
      e.setStarCount(starCount);
      e.setPraiseCount(praiseCount);
    }
    newsList = newsService.recommend(newsList);
    respMap.put("listData", newsList);
    return successData(respMap);
  }

  // 非遗资讯列表数据
  @RequestMapping("list")
  @ResponseBody
  public ApiResult list(Integer current, Integer size) {
    current = current == null ? 1 : current; // 默认第一页
    size = size == null ? 20 : size; // 分页大小
    // 获取列表数据
    QueryWrapper<News> queryWrapper = getQueryWrapper();
    LoginUser loginUser = tokenService.getLoginInfo();
    IPage<News> pageInfo = new Page<News>().setCurrent(current).setSize(size); // 分页大小
    pageInfo = newsService.page(pageInfo, queryWrapper);

    PagerVO<News> pagerVO = new PagerVO<>(pageInfo); // 可以承载除了page的额外信息

    return successData(pagerVO);
  }

  private QueryWrapper<News> getQueryWrapper() {
    // 用于存储查询的条件
    QueryWrapper<News> paramMap = new QueryWrapper<>();
    String id = req.getParameter("id");
    paramMap.eq(!StringUtils.isEmpty(id), "id", id);
    String showtitle = req.getParameter("showtitle");
    if (!StringUtils.isEmpty(showtitle)) {
      paramMap.like("showtitle", showtitle);
    }
    String publishtimeL = req.getParameter("publishtimeL");
    String publishtimeR = req.getParameter("publishtimeR");
    if (!StringUtils.isEmpty(publishtimeL)) {
      paramMap.ge("publishtime", DateUtil.strToDate(publishtimeL));
    }
    if (!StringUtils.isEmpty(publishtimeR)) {
      paramMap.le("publishtime", DateUtil.strToDate(publishtimeR));
    }

    String orderByStr = "id desc"; // 默认根据id降序排序
    // 默认按照id排序
    paramMap.last("order by " + orderByStr);
    return paramMap;
  }

  // 非遗资讯详情
  @RequestMapping("detail")
  @ResponseBody
  public ApiResult detail(String id) {
    News entity = newsService.getById(id);

    if (req.getRequestURI().contains("/webu")) {
      Map<String, Object> respMap = new HashMap<>();
      // 非遗资讯收藏点赞数
      Long starCount =
          starService.count(
              new LambdaQueryWrapper<Star>()
                  .eq(Star::getItemid, entity.getId())
                  .eq(Star::getType, "收藏"));
      Long praiseCount =
          starService.count(
              new LambdaQueryWrapper<Star>()
                  .eq(Star::getItemid, entity.getId())
                  .eq(Star::getType, "点赞"));
      respMap.put("starCount", starCount);
      respMap.put("praiseCount", praiseCount);
      // 非遗资讯详情页推荐
      List<News> newsHotList = newsService.topN(5);
      respMap.put("recommends", newsHotList);
      respMap.put("entity", entity);
      return successData(respMap);
    } else {
      return successData(entity);
    }
  }

  // 非遗资讯保存
  @RequestMapping("save")
  @ResponseBody
  public ApiResult save(@RequestBody News entityTemp) {
    String id = entityTemp.getId(); // 非遗资讯主键
    String showpic = entityTemp.getShowpic(); // 资讯首图
    String showtitle = entityTemp.getShowtitle(); // 资讯标题
    String showdesc = entityTemp.getShowdesc(); // 资讯描述
    String showdetail = entityTemp.getShowdetail(); // 资讯详情
    Date publishtime = entityTemp.getPublishtime(); // 发布时间
    String vv = entityTemp.getVv(); // 展示

    // 新增或更新
    if (entityTemp.getId() == null || "".equals(entityTemp.getId())) { // 新增
      id = CommonUtils.newId();
      entityTemp.setId(id);
      publishtime = new Date();
      entityTemp.setPublishtime(publishtime);
      // 唯一字段，则在此校验
      QueryWrapper<News> wrappershowtitle = new QueryWrapper();
      wrappershowtitle.eq("showtitle", entityTemp.getShowtitle());
      if (newsService.list(wrappershowtitle).size() > 0) {
        return fail("资讯标题 已存在！");
      }
      // before add

      newsService.save(entityTemp);
    } else {
      // before edit

      newsService.updateById(entityTemp);
    }
    return ApiResult.successMsg("操作成功"); // 返回保存成功
  }

  // 非遗资讯删除
  @RequestMapping("delete")
  @ResponseBody
  public ApiResult delete(String id) {
    News delTemp = newsService.getById(id);
    // before del

    // 根据ID删除记录
    newsService.removeById(id);
    return success();
  }
}

