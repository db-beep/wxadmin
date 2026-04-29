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

/** 非遗文化科普控制器 关于非遗文化科普的增删改查操作都在这 */
@Controller
@RequestMapping({"intangibleCultural", "webu/intangibleCultural"})
public class IntangibleCulturalController extends BaseController {

  /** 非遗文化科普网站列表页 */
  @RequestMapping("list-web")
  @ResponseBody
  public ApiResult<Map<String, Object>> listweb() {
    Map<String, Object> respMap = new HashMap<>();
    QueryWrapper<IntangibleCultural> queryWrapper = getQueryWrapper();
    List<IntangibleCultural> intangibleCulturalList = intangibleCulturalService.list(queryWrapper);

    List<Institution> fabjgFrnList = institutionService.list();
    respMap.put("fabjgFrnList", fabjgFrnList); // 外键放入request
    List<CultureType> labelsFrnList = cultureTypeService.list();
    respMap.put("labelsFrnList", labelsFrnList); // 外键放入request
    // 循环遍历list数据，统计、获取外键数据
    for (IntangibleCultural e : intangibleCulturalList) {

      e.setLabelsLabels(cultureTypeService.getLabelValues(e.getLabels()));

      // 非遗文化科普收藏点赞数
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
    intangibleCulturalList = intangibleCulturalService.recommend(intangibleCulturalList);
    respMap.put("listData", intangibleCulturalList);
    return successData(respMap);
  }

  // 非遗文化科普列表数据
  @RequestMapping("list")
  @ResponseBody
  public ApiResult list(Integer current, Integer size) {
    current = current == null ? 1 : current; // 默认第一页
    size = size == null ? 20 : size; // 分页大小
    // 获取列表数据
    QueryWrapper<IntangibleCultural> queryWrapper = getQueryWrapper();
    LoginUser loginUser = tokenService.getLoginInfo();
    // Institution只能看关联了自己的
    if (loginUser != null && loginUser.getRole().equals("institution")) {
      queryWrapper.eq("fabjg", loginUser.getId()); // 只能查看和自己相关的内容
    }
    IPage<IntangibleCultural> pageInfo =
        new Page<IntangibleCultural>().setCurrent(current).setSize(size); // 分页大小
    pageInfo = intangibleCulturalService.page(pageInfo, queryWrapper);

    // 循环遍历list数据获取外键数据
    for (IntangibleCultural e : pageInfo.getRecords()) {
      // 获取外键数据:非遗机构
      e.setFabjgFrn(institutionService.getById(e.getFabjg()));
      e.setLabelsLabels(cultureTypeService.getLabelValues(e.getLabels()));
    }

    PagerVO<IntangibleCultural> pagerVO = new PagerVO<>(pageInfo); // 可以承载除了page的额外信息

    return successData(pagerVO);
  }

  private QueryWrapper<IntangibleCultural> getQueryWrapper() {
    // 用于存储查询的条件
    QueryWrapper<IntangibleCultural> paramMap = new QueryWrapper<>();
    String id = req.getParameter("id");
    paramMap.eq(!StringUtils.isEmpty(id), "id", id);
    String showtitle = req.getParameter("showtitle");
    if (!StringUtils.isEmpty(showtitle)) {
      paramMap.like("showtitle", showtitle);
    }
    String fabjg = req.getParameter("fabjg");
    if (!StringUtils.isEmpty(fabjg)) {
      paramMap.eq("fabjg", fabjg);
    }
    String labels = req.getParameter("labels");
    if (!StringUtils.isEmpty(labels)) {
      paramMap.like("labels", labels);
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

  // 非遗文化科普详情
  @RequestMapping("detail")
  @ResponseBody
  public ApiResult detail(String id) {
    IntangibleCultural entity = intangibleCulturalService.getById(id);
    // 获取外键数据：非遗机构
    entity.setFabjgFrn(institutionService.getById(entity.getFabjg()));
    // 获取外键标签：非遗文化类型
    entity.setLabelsLabels(cultureTypeService.getLabelValues(entity.getLabels()));

    if (req.getRequestURI().contains("/webu")) {
      Map<String, Object> respMap = new HashMap<>();
      // 非遗文化科普收藏点赞数
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
      // 非遗文化科普详情页推荐
      List<IntangibleCultural> newsHotList = intangibleCulturalService.topN(5);
      respMap.put("recommends", newsHotList);
      respMap.put("entity", entity);
      return successData(respMap);
    } else {
      return successData(entity);
    }
  }

  // 非遗文化科普保存
  @RequestMapping("save")
  @ResponseBody
  public ApiResult save(@RequestBody IntangibleCultural entityTemp) {
    String id = entityTemp.getId(); // 非遗文化科普主键
    String showpic = entityTemp.getShowpic(); // 科普首图
    String showtitle = entityTemp.getShowtitle(); // 非遗文化标题
    String fabjg = entityTemp.getFabjg(); // 发布机构
    String labels = entityTemp.getLabels(); // 非遗文化标签
    String showdesc = entityTemp.getShowdesc(); // 非遗文化简述
    String showdetail = entityTemp.getShowdetail(); // 科普内容详情
    Date publishtime = entityTemp.getPublishtime(); // 发布时间
    String vv = entityTemp.getVv(); // 非遗文化展示

    // 新增或更新
    if (entityTemp.getId() == null || "".equals(entityTemp.getId())) { // 新增
      id = CommonUtils.newId();
      entityTemp.setId(id);
      publishtime = new Date();
      entityTemp.setPublishtime(publishtime);
      // 唯一字段，则在此校验
      QueryWrapper<IntangibleCultural> wrappershowtitle = new QueryWrapper();
      wrappershowtitle.eq("showtitle", entityTemp.getShowtitle());
      if (intangibleCulturalService.list(wrappershowtitle).size() > 0) {
        return fail("非遗文化标题 已存在！");
      }
      // before add

      intangibleCulturalService.save(entityTemp);
    } else {
      // before edit

      intangibleCulturalService.updateById(entityTemp);
    }
    return ApiResult.successMsg("操作成功"); // 返回保存成功
  }

  // 非遗文化科普删除
  @RequestMapping("delete")
  @ResponseBody
  public ApiResult delete(String id) {
    IntangibleCultural delTemp = intangibleCulturalService.getById(id);
    // before del

    // 根据ID删除记录
    intangibleCulturalService.removeById(id);
    return success();
  }
}

