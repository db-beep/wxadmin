package com.codeying.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.codeying.entity.Fankui;
import java.util.List;
/** 反馈与建议 mybatisPlus提供接口，自动实现了各种单表操作 */
public interface FankuiMapper extends BaseMapper<Fankui> {

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

