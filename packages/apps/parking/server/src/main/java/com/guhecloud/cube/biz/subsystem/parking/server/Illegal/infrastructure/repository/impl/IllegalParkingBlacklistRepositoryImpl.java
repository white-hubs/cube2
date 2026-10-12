package com.guhecloud.cube.biz.subsystem.parking.server.Illegal.infrastructure.repository.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.domain.po.IllegalParkingBlacklistPO;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.infrastructure.dao.IllegalParkingBlacklistMapper;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.infrastructure.repository.IllegalParkingBlacklistRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/5 16:10
 * @Description: 违停黑名单Repository实现
 */
@Slf4j
@Repository
public class IllegalParkingBlacklistRepositoryImpl extends ServiceImpl<IllegalParkingBlacklistMapper, IllegalParkingBlacklistPO> implements IllegalParkingBlacklistRepository {
}