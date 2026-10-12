package com.guhecloud.cube.srv.gen.cabbage.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.cabbage.server.dao.DbcMarketMapper;
import com.guhecloud.cube.srv.gen.cabbage.server.entity.DbcMarketPO;
import com.guhecloud.cube.srv.gen.cabbage.server.service.DbcMarketService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 大白菜市场服务实现
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DbcMarketServiceImpl extends ServiceImpl<DbcMarketMapper, DbcMarketPO> implements DbcMarketService {
}
