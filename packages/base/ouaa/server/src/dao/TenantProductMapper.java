package com.guhecloud.cube.biz.ouaa.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.ouaa.server.entity.TenantProduct;
import org.apache.ibatis.annotations.Mapper;

/**
 * 租户产品关联数据操作接口
 *
 * @author Woody
 * @date 2021/7/17
 */
@Mapper
public interface TenantProductMapper extends BaseMapper<TenantProduct> {
}
