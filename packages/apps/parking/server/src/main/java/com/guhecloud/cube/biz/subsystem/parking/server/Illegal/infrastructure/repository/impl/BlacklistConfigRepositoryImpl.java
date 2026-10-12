package com.guhecloud.cube.biz.subsystem.parking.server.Illegal.infrastructure.repository.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.domain.po.BlacklistConfigPO;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.infrastructure.dao.BlacklistConfigMapper;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.infrastructure.repository.BlacklistConfigRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/5 16:20
 * @Description: 黑名单阈值配置Repository实现
 */
@Slf4j
@Repository
public class BlacklistConfigRepositoryImpl extends ServiceImpl<BlacklistConfigMapper, BlacklistConfigPO> implements BlacklistConfigRepository {
}