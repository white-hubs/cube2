package com.guhecloud.cube.biz.subsystem.parking.server.Illegal.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.domain.po.BlacklistConfigPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/5 16:20
 * @Description: 黑名单阈值配置Mapper
 */
@Mapper
public interface BlacklistConfigMapper extends BaseMapper<BlacklistConfigPO> {
}