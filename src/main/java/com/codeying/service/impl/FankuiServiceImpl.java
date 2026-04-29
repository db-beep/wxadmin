package com.codeying.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.codeying.component.TokenService;
import com.codeying.entity.*;
import com.codeying.mapper.FankuiMapper;
import com.codeying.service.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/** 服务实现类 */
@Service
public class FankuiServiceImpl extends AbsServiceImpl<FankuiMapper, Fankui>
    implements FankuiService {

  @Override
  public List<Fankui> sqlSelectList(Fankui qo) {
    return baseMapper.sqlSelectList(qo);
  }

  @Override
  public int sqlDeleteById(String id) {
    return baseMapper.sqlDeleteById(id);
  }

  @Override
  public int sqlUpdate(Fankui e) {
    return baseMapper.sqlUpdate(e);
  }

  @Override
  public int sqlSave(Fankui e) {
    return baseMapper.sqlSave(e);
  }
}

