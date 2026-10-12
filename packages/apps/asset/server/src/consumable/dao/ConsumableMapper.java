package com.guhecloud.cube.srv.gen.property.management.server.consumable.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.Consumable;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableQueryParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 描述：耗材管理 Repository接口
 *
 * @author Appet
 * @date 2023-03-16 10:08:36
 */
@Repository
public interface ConsumableMapper extends BaseMapper<Consumable> {

    Integer updateConsumableStock(@Param("param") Consumable consumable);

    Integer getUseLimit(@Param("claimant") Long claimant, @Param("consumableId") Long consumableId, @Param("reduceDate") String reduceDate,
                        @Param("flowResult") String flowResult
                       );

    Page<Consumable> getPage(@Param("page") Page page, @Param("param") ConsumableQueryParam param);
}