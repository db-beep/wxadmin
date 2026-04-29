package com.codeying.service;

import com.codeying.entity.Fankui;
import java.util.List;
/** 服务类 */
public interface FankuiService extends MyService<Fankui> {

  /**
   * 查询
   *
   * @param qo
   * @return
   */
  List<Fankui> sqlSelectList(Fankui qo);

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
  int sqlUpdate(Fankui e);

  /**
   * 保存
   *
   * @param e
   * @return
   */
  int sqlSave(Fankui e);
}

