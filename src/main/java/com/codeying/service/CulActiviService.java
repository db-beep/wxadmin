package com.codeying.service;

import com.codeying.entity.CulActivi;
import java.util.List;
/** 服务类 */
public interface CulActiviService extends MyService<CulActivi> {

  /**
   * 查询
   *
   * @param qo
   * @return
   */
  List<CulActivi> sqlSelectList(CulActivi qo);

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
  int sqlUpdate(CulActivi e);

  /**
   * 保存
   *
   * @param e
   * @return
   */
  int sqlSave(CulActivi e);
}

