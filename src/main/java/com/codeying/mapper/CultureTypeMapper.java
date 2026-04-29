package com.codeying.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.codeying.entity.CultureType;
import java.util.List;
/** 非遗文化类型 mybatisPlus提供接口，自动实现了各种单表操作 */
public interface CultureTypeMapper extends BaseMapper<CultureType> {

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
}

