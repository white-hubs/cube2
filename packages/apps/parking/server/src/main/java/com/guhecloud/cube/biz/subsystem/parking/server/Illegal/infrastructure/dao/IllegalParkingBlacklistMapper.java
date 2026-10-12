package com.guhecloud.cube.biz.subsystem.parking.server.Illegal.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.domain.po.IllegalParkingBlacklistPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/5 16:10
 * @Description: 违停黑名单Mapper
 */
@Mapper
public interface IllegalParkingBlacklistMapper extends BaseMapper<IllegalParkingBlacklistPO> {
}