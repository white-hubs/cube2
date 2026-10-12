package com.guhecloud.cube.biz.ouaa.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.ouaa.server.entity.Tenant;
import org.apache.ibatis.annotations.Mapper;

/**
 * 租户数据操作接口
 *
 * @author Woody
 * @date 2021/7/1
 */
@Mapper
public interface TenantMapper extends BaseMapper<Tenant> {
}
