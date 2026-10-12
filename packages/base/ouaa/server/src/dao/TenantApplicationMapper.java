package com.guhecloud.cube.biz.ouaa.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.ouaa.server.entity.TenantApplication;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * 租户应用关联数据操作接口
 *
 * @author Woody
 * @date 2021/7/16
 */
@Mapper
public interface TenantApplicationMapper extends BaseMapper<TenantApplication> {

    /**
     * 获取租户下的应用
     *
     * @param tenant
     * @param productId
     * @param applicationIds
     *
     * @return
     */
    List<TenantApplication> selectListByTenantProductApplications(@Param("tenant") Integer tenant, @Param("productId") Long productId,
                                                                  @Param("applicationIds") Collection<Long> applicationIds
                                                                 );

}
