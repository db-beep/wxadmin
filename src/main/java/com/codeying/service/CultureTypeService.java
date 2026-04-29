package com.codeying.service;

import com.codeying.entity.CultureType;
import java.util.List;
/** 服务类 */
public interface CultureTypeService extends MyService<CultureType> {

  /**
   * 查询
   *
   * @param qo
   * @return
   */
  List<CultureType> sqlSelectList(CultureType qo);

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
  int sqlUpdate(CultureType e);

  /**
   * 保存
   *
   * @param e
   * @return
   */
  int sqlSave(CultureType e);

  /**
   * 获取标签值的列表
   *
   * @param labels
   * @return
   */
  List<CultureType> getLabelValues(String labels);
}

