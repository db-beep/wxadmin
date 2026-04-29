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

/** 非遗活动报名控制器 关于非遗活动报名的增删改查操作都在这 */
@Controller
@RequestMapping({"baom", "webu/baom"})
public class BaomController extends BaseController {

  // 非遗活动报名列表数据
  @RequestMapping("list")
  @ResponseBody
  public ApiResult list(Integer current, Integer size) {
    current = current == null ? 1 : current; // 默认第一页
    size = size == null ? 20 : size; // 分页大小
    // 获取列表数据
    QueryWrapper<Baom> queryWrapper = getQueryWrapper();
    LoginUser loginUser = tokenService.getLoginInfo();
    // User只能看关联了自己的
    if (loginUser != null && loginUser.getRole().equals("user")) {
      queryWrapper.eq("userid", loginUser.getId()); // 只能查看和自己相关的内容
    }
    // Institution只能看关联了自己的
    if (loginUser != null && loginUser.getRole().equals("institution")) {
      queryWrapper.eq("jig", loginUser.getId()); // 只能查看和自己相关的内容
    }
    IPage<Baom> pageInfo = new Page<Baom>().setCurrent(current).setSize(size); // 分页大小
    pageInfo = baomService.page(pageInfo, queryWrapper);

    // 循环遍历list数据获取外键数据
    for (Baom e : pageInfo.getRecords()) {
      // 获取外键数据:用户
      e.setUseridFrn(userService.getById(e.getUserid()));
      // 获取外键数据:非遗传承活动
      e.setAcidFrn(culActiviService.getById(e.getAcid()));
      // 获取外键数据:非遗机构
      e.setJigFrn(institutionService.getById(e.getJig()));
    }

    PagerVO<Baom> pagerVO = new PagerVO<>(pageInfo); // 可以承载除了page的额外信息

    return successData(pagerVO);
  }

  private QueryWrapper<Baom> getQueryWrapper() {
    // 用于存储查询的条件
    QueryWrapper<Baom> paramMap = new QueryWrapper<>();
    String id = req.getParameter("id");
    paramMap.eq(!StringUtils.isEmpty(id), "id", id);
    String userid = req.getParameter("userid");
    if (!StringUtils.isEmpty(userid)) {
      paramMap.eq("userid", userid);
    }
    String acid = req.getParameter("acid");
    if (!StringUtils.isEmpty(acid)) {
      paramMap.eq("acid", acid);
    }
    String jig = req.getParameter("jig");
    if (!StringUtils.isEmpty(jig)) {
      paramMap.eq("jig", jig);
    }
    String remark = req.getParameter("remark");
    if (!StringUtils.isEmpty(remark)) {
      paramMap.like("remark", remark);
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

  // 非遗活动报名详情
  @RequestMapping("detail")
  @ResponseBody
  public ApiResult detail(String id) {
    Baom entity = baomService.getById(id);
    // 获取外键数据：用户
    entity.setUseridFrn(userService.getById(entity.getUserid()));
    // 获取外键数据：非遗传承活动
    entity.setAcidFrn(culActiviService.getById(entity.getAcid()));
    // 获取外键数据：非遗机构
    entity.setJigFrn(institutionService.getById(entity.getJig()));

    return successData(entity);
  }

  // 非遗活动报名保存
  @RequestMapping("save")
  @ResponseBody
  public ApiResult save(@RequestBody Baom entityTemp) {
    String id = entityTemp.getId(); // 非遗活动报名主键
    String userid = entityTemp.getUserid(); // 报名人
    String acid = entityTemp.getAcid(); // 报名活动
    String jig = entityTemp.getJig(); // 非遗机构
    String remark = entityTemp.getRemark(); // 报名备注
    Date createtime = entityTemp.getCreatetime(); // 报名时间
    String state = entityTemp.getState(); // 审核

    // 新增或更新
    if (entityTemp.getId() == null || "".equals(entityTemp.getId())) { // 新增
      id = CommonUtils.newId();
      entityTemp.setId(id);
      jig = culActiviService.getById(acid).getZhubf();
      entityTemp.setJig(jig);
      createtime = new Date();
      entityTemp.setCreatetime(createtime);
      state = "待审核";
      entityTemp.setState(state);
      // before add
      CulActivi cu = culActiviService.getById(acid);
      if(cu.getStarttime().before(new Date())){
        return fail("活动已开始，无法报名！");
      }

      cu.setCount(cu.getCount() + 1);
      if (cu.getCount() > cu.getCount0()) {
        return fail("报名人数已满！");
      }
      culActiviService.updateById(cu);
      baomService.save(entityTemp);
    } else {
      // before edit

      baomService.updateById(entityTemp);
    }
    return ApiResult.successMsg("操作成功"); // 返回保存成功
  }

  // 非遗活动报名删除
  @RequestMapping("delete")
  @ResponseBody
  public ApiResult delete(String id) {
    Baom delTemp = baomService.getById(id);
    // before del
    CulActivi cu = culActiviService.getById(delTemp.getAcid());
    cu.setCount(cu.getCount() - 1);
    culActiviService.updateById(cu);
    // 根据ID删除记录
    baomService.removeById(id);
    return success();
  }
}

