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

/** 非遗文化类型控制器 关于非遗文化类型的增删改查操作都在这 */
@Controller
@RequestMapping({"cultureType", "webu/cultureType"})
public class CultureTypeController extends BaseController {

  // 前后端分离，做为外键下拉
  @RequestMapping("list/select")
  @ResponseBody
  public ApiResult select() {
    ApiResult res = list(1, 500);
    PagerVO<CultureType> pagerVO = (PagerVO<CultureType>) res.getData();
    return successData(pagerVO.getRecords());
  }

  // 非遗文化类型列表数据
  @RequestMapping("list")
  @ResponseBody
  public ApiResult list(Integer current, Integer size) {
    current = current == null ? 1 : current; // 默认第一页
    size = size == null ? 20 : size; // 分页大小
    // 获取列表数据
    QueryWrapper<CultureType> queryWrapper = getQueryWrapper();
    LoginUser loginUser = tokenService.getLoginInfo();
    IPage<CultureType> pageInfo = new Page<CultureType>().setCurrent(current).setSize(size); // 分页大小
    pageInfo = cultureTypeService.page(pageInfo, queryWrapper);

    PagerVO<CultureType> pagerVO = new PagerVO<>(pageInfo); // 可以承载除了page的额外信息

    return successData(pagerVO);
  }

  private QueryWrapper<CultureType> getQueryWrapper() {
    // 用于存储查询的条件
    QueryWrapper<CultureType> paramMap = new QueryWrapper<>();
    String id = req.getParameter("id");
    paramMap.eq(!StringUtils.isEmpty(id), "id", id);
    String name = req.getParameter("name");
    if (!StringUtils.isEmpty(name)) {
      paramMap.like("name", name);
    }

    String orderByStr = "id desc"; // 默认根据id降序排序
    // 默认按照id排序
    paramMap.last("order by " + orderByStr);
    return paramMap;
  }

  // 非遗文化类型详情
  @RequestMapping("detail")
  @ResponseBody
  public ApiResult detail(String id) {
    CultureType entity = cultureTypeService.getById(id);

    return successData(entity);
  }

  // 非遗文化类型保存
  @RequestMapping("save")
  @ResponseBody
  public ApiResult save(@RequestBody CultureType entityTemp) {
    String id = entityTemp.getId(); // 非遗文化类型主键
    String name = entityTemp.getName(); // 非遗类型名
    String description = entityTemp.getDescription(); // 类型简介

    // 新增或更新
    if (entityTemp.getId() == null || "".equals(entityTemp.getId())) { // 新增
      id = CommonUtils.newId();
      entityTemp.setId(id);
      // 唯一字段，则在此校验
      QueryWrapper<CultureType> wrappername = new QueryWrapper();
      wrappername.eq("name", entityTemp.getName());
      if (cultureTypeService.list(wrappername).size() > 0) {
        return fail("非遗类型名 已存在！");
      }
      // before add

      cultureTypeService.save(entityTemp);
    } else {
      // before edit

      cultureTypeService.updateById(entityTemp);
    }
    return ApiResult.successMsg("操作成功"); // 返回保存成功
  }

  // 非遗文化类型删除
  @RequestMapping("delete")
  @ResponseBody
  public ApiResult delete(String id) {
    CultureType delTemp = cultureTypeService.getById(id);
    // before del

    // 根据ID删除记录
    cultureTypeService.removeById(id);
    return success();
  }
}

