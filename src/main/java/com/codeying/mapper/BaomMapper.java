package com.codeying.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.codeying.entity.Baom;
import java.util.List;
/** 非遗活动报名 mybatisPlus提供接口，自动实现了各种单表操作 */
public interface BaomMapper extends BaseMapper<Baom> {

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

