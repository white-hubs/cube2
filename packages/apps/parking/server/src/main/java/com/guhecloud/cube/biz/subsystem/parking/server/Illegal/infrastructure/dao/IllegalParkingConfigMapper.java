package com.guhecloud.cube.biz.subsystem.parking.server.Illegal.infrastructure.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.domain.po.IllegalParkingConfigPO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/5 16:10
 * @Description: 违停配置Mapper
 */
@Mapper
public interface IllegalParkingConfigMapper extends BaseMapper<IllegalParkingConfigPO> {
    @InterceptorIgnore(tenantLine = "on")
    List<Integer> getAllTenant();
}