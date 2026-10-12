package com.guhecloud.cube.biz.subsystem.parking.server.Illegal.infrastructure.repository.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.domain.po.VehiclePenaltyRecordPO;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.infrastructure.dao.VehiclePenaltyRecordMapper;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.infrastructure.repository.VehiclePenaltyRecordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/5 16:10
 * @Description: 车牌扣分记录Repository实现
 */
@Slf4j
@Repository
public class VehiclePenaltyRecordRepositoryImpl extends ServiceImpl<VehiclePenaltyRecordMapper, VehiclePenaltyRecordPO> implements VehiclePenaltyRecordRepository {
}