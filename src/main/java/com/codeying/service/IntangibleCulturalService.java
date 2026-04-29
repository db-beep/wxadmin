package com.codeying.service;

import com.codeying.entity.IntangibleCultural;
import java.util.List;
/** 服务类 */
public interface IntangibleCulturalService extends MyService<IntangibleCultural> {

  /**
   * 推荐
   *
   * @param all
   * @return
   */
  public List<IntangibleCultural> recommend(List<IntangibleCultural> all);

  /**
   * 查询
   *
   * @param qo
   * @return
   */
  List<IntangibleCultural> sqlSelectList(IntangibleCultural qo);

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
  int sqlUpdate(IntangibleCultural e);

  /**
   * 保存
   *
   * @param e
   * @return
   */
  int sqlSave(IntangibleCultural e);

  List<IntangibleCultural> topN(int n);
}

