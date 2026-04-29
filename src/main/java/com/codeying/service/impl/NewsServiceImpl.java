package com.codeying.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.codeying.component.TokenService;
import com.codeying.entity.*;
import com.codeying.mapper.NewsMapper;
import com.codeying.service.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/** 服务实现类 */
@Service
public class NewsServiceImpl extends AbsServiceImpl<NewsMapper, News> implements NewsService {

  @Autowired StarService starService;

  @Autowired protected TokenService tokenService; // token专用

  /** vue获取当前用户的信息缓存 */
  public LoginUser getCurrentUser() {
    return tokenService.getLoginInfo();
  }

  // 我已收藏加分
  double ALREADY_STAR = 0.1;
  // 命中我的标签加分
  double MATCH_MY_LABEL_SCORE = 0.3;
  // 命中收藏标签加分
  double MATCH_MY_STAR_SCORE = 0.2;
  // 推荐
  @Override
  public List<News> recommend(List<News> all) {
    LoginUser loginUser = getCurrentUser();
    if (loginUser == null) {
      return all; // 未登录，不推荐
    }
    final List<News> itemList;
    Map<String, News> collect = all.stream().collect(Collectors.toMap(News::getId, e -> e));
    List<Star> allStar =
        starService.list(
            new LambdaQueryWrapper<Star>().eq(Star::getItemtype, "news").eq(Star::getType, "收藏"));

    // 基于用户的协同推荐
    // 总结其他用户行为
    Map<String, List<String>> listMap = new HashMap<>();
    for (Star star : allStar) {
      if (listMap.containsKey(star.getUserid())) {
        listMap.get(star.getUserid()).add(star.getItemid());
      } else {
        List<String> l = new ArrayList<>();
        l.add(star.getUserid());
        l.add(star.getItemid());
        listMap.put(star.getUserid(), l);
      }
    }
    // 该用户没有收藏任何内容，那么自然无可分析
    if (listMap.get(loginUser.getId()) == null) {
      itemList = all;
    } else {
      List<List<String>> userAndLike = new ArrayList<>(listMap.values());
      String userid = loginUser.getId();
      List<Xietong.Sort> compute = new ArrayList<>();
      if (userAndLike.size() != 0) {
        compute = Xietong.compute(userAndLike, userid);
      }
      itemList = new ArrayList<>();
      // 加入推荐项目
      for (Xietong.Sort s : compute) {
        if (collect.get(s.getId()) != null) {
          collect.get(s.getId()).setRecommendScore(s.getScore());
          if (s.getScore() > 0.8) {
            collect.get(s.getId()).setSmallTip("为您推荐");
          }
          // else if(s.getScore() > 0.7){collect.get(s.getId()).setSmallTip("可能喜欢");}
          itemList.add(collect.get(s.getId()));
        }
      }
      // 加入其他项目
      collect.forEach(
          (e, v) -> {
            if (!itemList.contains(v) && v != null) itemList.add(v);
          });
    }

    return itemList;
  }

  @Override
  public List<News> sqlSelectList(News qo) {
    return baseMapper.sqlSelectList(qo);
  }

  @Override
  public int sqlDeleteById(String id) {
    return baseMapper.sqlDeleteById(id);
  }

  @Override
  public int sqlUpdate(News e) {
    return baseMapper.sqlUpdate(e);
  }

  @Override
  public int sqlSave(News e) {
    return baseMapper.sqlSave(e);
  }

  @Override
  public List<News> topN(int n) {
    Page<News> page = new Page<>(1, n);
    return baseMapper.topN(page);
  }
}

