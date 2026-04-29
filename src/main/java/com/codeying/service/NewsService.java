package com.codeying.service;

import com.codeying.entity.News;
import java.util.List;
/** 服务类 */
public interface NewsService extends MyService<News> {

  /**
   * 推荐
   *
   * @param all
   * @return
   */
  public List<News> recommend(List<News> all);

  /**
   * 查询
   *
   * @param qo
   * @return
   */
  List<News> sqlSelectList(News qo);

  /**
   * 删掉
   *
   * @param id
   * @return
   */
  int sqlDeleteById(String id);

  /**
   * 更新
   *
   * @param e
   * @return
   */
  int sqlUpdate(News e);

  /**
   * 保存
   *
   * @param e
   * @return
   */
  int sqlSave(News e);

  List<News> topN(int n);
}

