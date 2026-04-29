package com.codeying.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.codeying.entity.CulActivi;
import java.util.List;
/** 非遗传承活动 mybatisPlus提供接口，自动实现了各种单表操作 */
public interface CulActiviMapper extends BaseMapper<CulActivi> {

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

