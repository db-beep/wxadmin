package com.codeying.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.codeying.entity.IntangibleCultural;
import java.util.List;
/** 非遗文化科普 mybatisPlus提供接口，自动实现了各种单表操作 */
public interface IntangibleCulturalMapper extends BaseMapper<IntangibleCultural> {

  /**
   * 收藏最多的前几个
   *
   * @param page
   * @return
   */
  List<IntangibleCultural> topN(Page<IntangibleCultural> page);

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
}

