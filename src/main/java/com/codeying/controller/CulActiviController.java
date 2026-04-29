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

/** 非遗传承活动控制器 关于非遗传承活动的增删改查操作都在这 */
@Controller
@RequestMapping({"culActivi", "webu/culActivi"})
public class CulActiviController extends BaseController {

  // 前后端分离，做为外键下拉
  @RequestMapping("list/select")
  @ResponseBody
  public ApiResult select() {
    ApiResult res = list(1, 500);
    PagerVO<CulActivi> pagerVO = (PagerVO<CulActivi>) res.getData();
    return successData(pagerVO.getRecords());
  }

  // 非遗传承活动列表数据
  @RequestMapping("list")
  @ResponseBody
  public ApiResult list(Integer current, Integer size) {
    current = current == null ? 1 : current; // 默认第一页
    size = size == null ? 20 : size; // 分页大小
    // 获取列表数据
    QueryWrapper<CulActivi> queryWrapper = getQueryWrapper();
    LoginUser loginUser = tokenService.getLoginInfo();
    // Institution只能看关联了自己的
    if (loginUser != null && loginUser.getRole().equals("institution")) {
      queryWrapper.eq("zhubf", loginUser.getId()); // 只能查看和自己相关的内容
    }
    IPage<CulActivi> pageInfo = new Page<CulActivi>().setCurrent(current).setSize(size); // 分页大小
    pageInfo = culActiviService.page(pageInfo, queryWrapper);

    // 循环遍历list数据获取外键数据
    for (CulActivi e : pageInfo.getRecords()) {
      // 获取外键数据:非遗机构
      e.setZhubfFrn(institutionService.getById(e.getZhubf()));
    }

    PagerVO<CulActivi> pagerVO = new PagerVO<>(pageInfo); // 可以承载除了page的额外信息

    return successData(pagerVO);
  }

  private QueryWrapper<CulActivi> getQueryWrapper() {
    // 用于存储查询的条件
    QueryWrapper<CulActivi> paramMap = new QueryWrapper<>();
    String id = req.getParameter("id");
    paramMap.eq(!StringUtils.isEmpty(id), "id", id);
    String name = req.getParameter("name");
    if (!StringUtils.isEmpty(name)) {
      paramMap.like("name", name);
    }
    String zhubf = req.getParameter("zhubf");
    if (!StringUtils.isEmpty(zhubf)) {
      paramMap.eq("zhubf", zhubf);
    }

    String orderByStr = "id desc"; // 默认根据id降序排序
    // 默认按照id排序
    paramMap.last("order by " + orderByStr);
    return paramMap;
  }

  // 非遗传承活动详情
  @RequestMapping("detail")
  @ResponseBody
  public ApiResult detail(String id) {
    CulActivi entity = culActiviService.getById(id);
    // 获取外键数据：非遗机构
    entity.setZhubfFrn(institutionService.getById(entity.getZhubf()));

    return successData(entity);
  }

  // 非遗传承活动保存
  @RequestMapping("save")
  @ResponseBody
  public ApiResult save(@RequestBody CulActivi entityTemp) {
    String id = entityTemp.getId(); // 非遗传承活动主键
    String name = entityTemp.getName(); // 活动名称
    String zhubf = entityTemp.getZhubf(); // 主办机构
    String content = entityTemp.getContent(); // 活动内容
    String baomsm = entityTemp.getBaomsm(); // 报名说明
    Date starttime = entityTemp.getStarttime(); // 活动开始时间
    Date endtime = entityTemp.getEndtime(); // 结束时间
    Integer count0 = entityTemp.getCount0(); // 限制报名人数
    Integer count = entityTemp.getCount(); // 已报名人数

    // 新增或更新
    if (entityTemp.getId() == null || "".equals(entityTemp.getId())) { // 新增
      id = CommonUtils.newId();
      entityTemp.setId(id);
      count = 0;
      entityTemp.setCount(count);
      // 唯一字段，则在此校验
      QueryWrapper<CulActivi> wrappername = new QueryWrapper();
      wrappername.eq("name", entityTemp.getName());
      if (culActiviService.list(wrappername).size() > 0) {
        return fail("活动名称 已存在！");
      }
      // before add

      culActiviService.save(entityTemp);
    } else {
      // before edit

      culActiviService.updateById(entityTemp);
    }
    return ApiResult.successMsg("操作成功"); // 返回保存成功
  }

  // 非遗传承活动删除
  @RequestMapping("delete")
  @ResponseBody
  public ApiResult delete(String id) {
    CulActivi delTemp = culActiviService.getById(id);
    // before del

    // 根据ID删除记录
    culActiviService.removeById(id);
    return success();
  }
}

