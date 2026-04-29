package com.codeying.service;

import com.codeying.entity.Baom;
import java.util.List;
/** 服务类 */
public interface BaomService extends MyService<Baom> {

  /**
   * 查询
   *
   * @param qo
   * @return
   */
  List<Baom> sqlSelectList(Baom qo);

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
  int sqlUpdate(Baom e);

  /**
   * 保存
   *
   * @param e
   * @return
   */
  int sqlSave(Baom e);
}

