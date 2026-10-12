package com.guhecloud.cube.biz.subsystem.parking.server.Illegal.infrastructure.repository.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.domain.po.IllegalParkingRecordPO;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.infrastructure.dao.IllegalParkingRecordMapper;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.infrastructure.repository.IllegalParkingRecordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/5 15:43
 * @Description:
 */
@Slf4j
@Repository
public class IllegalParkingRecordRepositoryImpl extends ServiceImpl<IllegalParkingRecordMapper, IllegalParkingRecordPO> implements IllegalParkingRecordRepository {
}
