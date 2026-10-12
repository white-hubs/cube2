package com.guhecloud.cube.srv.gen.property.management.server.consumable.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.property.management.client.property.vo.property.MoneyNumVO;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.ConsumableStoreStock;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableInStoreParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableStoreStockExtendVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：仓库耗材库存 Repository接口
 *
 * @author Appet
 * @date 2023-03-16 10:08:36
 */
@Repository
public interface ConsumableStoreStockMapper extends BaseMapper<ConsumableStoreStock> {

    int insertStock(@Param("datas") List<ConsumableStoreStock> datas);

    int updateStockForReduce(@Param("param") ConsumableStoreStock param);

    Page<ConsumableStoreStockExtendVO> getStackInStore(Page page, @Param("param") ConsumableInStoreParam param);

    List<ConsumableStoreStockExtendVO> getStackInStore(@Param("param") ConsumableInStoreParam param);

    MoneyNumVO getTotalMoney(@Param("keyword") String keyword, @Param("type") String type);

    Integer getConsumableNumber(@Param("consumableId") Long consumableId);

    List<ConsumableStoreStock> getConsumable(@Param("storeId") Long storeId);

}