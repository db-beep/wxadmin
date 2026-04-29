package com.codeying.service;

import com.codeying.entity.Institution;
import java.util.List;
/** 服务类 */
public interface InstitutionService extends MyService<Institution> {

  /**
   * 查询
   *
   * @param qo
   * @return
   */
  List<Institution> sqlSelectList(Institution qo);

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
  int sqlUpdate(Institution e);

  /**
   * 保存
   *
   * @param e
   * @return
   */
  int sqlSave(Institution e);
}

