package com.codeying.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.codeying.entity.Institution;
import java.util.List;
/** 非遗机构 mybatisPlus提供接口，自动实现了各种单表操作 */
public interface InstitutionMapper extends BaseMapper<Institution> {

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

