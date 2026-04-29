package com.codeying.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.codeying.mapper.SqlMapper;
import com.codeying.utils.component.ApiResult;
import com.codeying.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class IndexWebController extends BaseController {

  @Autowired SqlMapper sqlMapper;
  /**
   * 个人中心
   *
   * @return
   */
  @RequestMapping("/webu/personal")
  @ResponseBody
  public ApiResult personal() {
    LoginUser user = getCurrentUser();
    Map<String, Object> respData = new HashMap<>();
    List<Star> stars = starService.userStar(user.getId(), user.getRole());
    respData.put("stars", stars);
    if (user.getRole().equalsIgnoreCase("user")) {
      User loginUser = userService.getById(user.getId());
      // 获取标签：非遗文化类型
      loginUser.setLabelsLabels(cultureTypeService.getLabelValues(loginUser.getLabels()));
      respData.put("user", loginUser);
    }
    return successData(respData);
  }

  /**
   * 收藏相关
   *
   * @return
   */
  @PostMapping("/web/star")
  @ResponseBody
  public ApiResult star(@RequestBody Map<String, String> map) {
    LoginUser user = getCurrentUser();
    if (user == null) {
      return fail("未登录");
    }
    String action = map.get("action");
    String id = map.get("id");
    String entityName = map.get("entityName");
    String op = map.get("op");
    if (action.equals("hasStar")) {
      // 是否收藏
      String starId = starService.hasStar(user.getId(), user.getRole(), id, entityName, op);
      return ApiResult.successData(starId); // 返回收藏id,收藏ID不为空就是收藏了
    } else if (action.equals("star")) {
      // 收藏
      boolean res = starService.star(user.getId(), user.getRole(), id, entityName, op);
      if (res) {
        return ApiResult.successMsg(op + "成功"); // 标记成功
      } else {
        return ApiResult.fail("已经" + op + "过了！");
      }
    } else if (action.equals("cancelStar")) {
      starService.remove(
          new LambdaQueryWrapper<Star>()
              .eq(Star::getItemid, id)
              .eq(Star::getType, op)
              .eq(Star::getUserid, user.getId()));
      return ApiResult.successMsg("已取消"); // 标记成功
    }
    return fail("请先登录");
  }
}

