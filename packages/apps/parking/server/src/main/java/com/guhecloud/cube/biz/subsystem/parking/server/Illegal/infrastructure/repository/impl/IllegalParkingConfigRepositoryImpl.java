package com.guhecloud.cube.biz.subsystem.parking.server.Illegal.infrastructure.repository.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.domain.po.IllegalParkingConfigPO;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.infrastructure.dao.IllegalParkingConfigMapper;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.infrastructure.repository.IllegalParkingConfigRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/5 16:10
 * @Description: 违停配置Repository实现
 */
@Slf4j
@Repository
public class IllegalParkingConfigRepositoryImpl extends ServiceImpl<IllegalParkingConfigMapper, IllegalParkingConfigPO> implements IllegalParkingConfigRepository {
}