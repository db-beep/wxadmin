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

/** 反馈与建议控制器 关于反馈与建议的增删改查操作都在这 */
@Controller
@RequestMapping({"fankui", "webu/fankui"})
public class FankuiController extends BaseController {

  // 反馈与建议列表数据
  @RequestMapping("list")
  @ResponseBody
  public ApiResult list(Integer current, Integer size) {
    current = current == null ? 1 : current; // 默认第一页
    size = size == null ? 20 : size; // 分页大小
    // 获取列表数据
    QueryWrapper<Fankui> queryWrapper = getQueryWrapper();
    LoginUser loginUser = tokenService.getLoginInfo();
    // User只能看关联了自己的
    if (loginUser != null && loginUser.getRole().equals("user")) {
      queryWrapper.eq("userid", loginUser.getId()); // 只能查看和自己相关的内容
    }
    // Institution只能看关联了自己的
    if (loginUser != null && loginUser.getRole().equals("institution")) {
      queryWrapper.eq("inid", loginUser.getId()); // 只能查看和自己相关的内容
    }
    IPage<Fankui> pageInfo = new Page<Fankui>().setCurrent(current).setSize(size); // 分页大小
    pageInfo = fankuiService.page(pageInfo, queryWrapper);

    // 循环遍历list数据获取外键数据
    for (Fankui e : pageInfo.getRecords()) {
      // 获取外键数据:用户
      e.setUseridFrn(userService.getById(e.getUserid()));
      // 获取外键数据:非遗机构
      e.setInidFrn(institutionService.getById(e.getInid()));
    }

    PagerVO<Fankui> pagerVO = new PagerVO<>(pageInfo); // 可以承载除了page的额外信息

    return successData(pagerVO);
  }

  private QueryWrapper<Fankui> getQueryWrapper() {
    // 用于存储查询的条件
    QueryWrapper<Fankui> paramMap = new QueryWrapper<>();
    String id = req.getParameter("id");
    paramMap.eq(!StringUtils.isEmpty(id), "id", id);
    String userid = req.getParameter("userid");
    if (!StringUtils.isEmpty(userid)) {
      paramMap.eq("userid", userid);
    }
    String inid = req.getParameter("inid");
    if (!StringUtils.isEmpty(inid)) {
      paramMap.eq("inid", inid);
    }
    String title = req.getParameter("title");
    if (!StringUtils.isEmpty(title)) {
      paramMap.like("title", title);
    }
    String state = req.getParameter("state");
    if (!StringUtils.isEmpty(state)) {
      paramMap.eq("state", state);
    }

    String orderByStr = "id desc"; // 默认根据id降序排序
    // 默认按照id排序
    paramMap.last("order by " + orderByStr);
    return paramMap;
  }

  // 反馈与建议详情
  @RequestMapping("detail")
  @ResponseBody
  public ApiResult detail(String id) {
    Fankui entity = fankuiService.getById(id);
    // 获取外键数据：用户
    entity.setUseridFrn(userService.getById(entity.getUserid()));
    // 获取外键数据：非遗机构
    entity.setInidFrn(institutionService.getById(entity.getInid()));

    return successData(entity);
  }

  // 反馈与建议保存
  @RequestMapping("save")
  @ResponseBody
  public ApiResult save(@RequestBody Fankui entityTemp) {
    String id = entityTemp.getId(); // 反馈与建议主键
    String userid = entityTemp.getUserid(); // 反馈人
    String inid = entityTemp.getInid(); // 机构
    String title = entityTemp.getTitle(); // 反馈内容
    Date createtime = entityTemp.getCreatetime(); // 反馈时间
    String state = entityTemp.getState(); // 回复状态
    String content = entityTemp.getContent(); // 回复内容

    // 新增或更新
    if (entityTemp.getId() == null || "".equals(entityTemp.getId())) { // 新增
      id = CommonUtils.newId();
      entityTemp.setId(id);
      createtime = new Date();
      entityTemp.setCreatetime(createtime);
      state = "待回复";
      entityTemp.setState(state);
      content = "";
      entityTemp.setContent(content);
      // before add

      fankuiService.save(entityTemp);
    } else {
      // before edit

      fankuiService.updateById(entityTemp);
    }
    return ApiResult.successMsg("操作成功"); // 返回保存成功
  }

  // 反馈与建议删除
  @RequestMapping("delete")
  @ResponseBody
  public ApiResult delete(String id) {
    Fankui delTemp = fankuiService.getById(id);
    // before del

    // 根据ID删除记录
    fankuiService.removeById(id);
    return success();
  }
}

