package com.codeying.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.codeying.entity.News;
import java.util.List;
/** 非遗资讯 mybatisPlus提供接口，自动实现了各种单表操作 */
public interface NewsMapper extends BaseMapper<News> {

  /**
   * 收藏最多的前几个
   *
   * @param page
   * @return
   */
  List<News> topN(Page<News> page);

  /**
   * 查询
   *
   * @param qo
   * @return
   */
  List<News> sqlSelectList(News qo);

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
  int sqlUpdate(News e);

  /**
   * 保存
   *
   * @param e
   * @return
   */
  int sqlSave(News e);
}

