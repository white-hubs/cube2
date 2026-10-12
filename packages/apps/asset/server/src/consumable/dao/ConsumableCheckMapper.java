package com.guhecloud.cube.srv.gen.property.management.server.consumable.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.property.management.client.common.vo.KeyValueVO;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.ConsumableCheck;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableCheckDetailParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableStoreStockExtendVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：耗材盘点 Repository接口
 *
 * @author Appet
 * @date 2023-03-16 10:08:36
 */
@Repository
public interface ConsumableCheckMapper extends BaseMapper<ConsumableCheck> {

    Page<ConsumableStoreStockExtendVO> getCheckDetail(Page page, @Param("param") ConsumableCheckDetailParam param);

    List<KeyValueVO> getCheckDetailStat(@Param("checkId") Long checkId);
}