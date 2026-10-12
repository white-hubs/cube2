package com.guhecloud.cube.srv.gen.property.management.server.consumable.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.property.management.client.property.vo.property.MoneyNumVO;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.ConsumableAddRecord;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.ConsumableReduceRecord;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.ConsumableStockDetail;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.ConsumableStoreStock;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableInStoreParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.ConsumableStockDetailParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableStoreStockExtendVO;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableStoreStockVO;
import com.guhecloud.platform.base.model.Pagination;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 描述：仓库耗材库存服务层
 *
 * @author Appet
 * @date 2023-03-16 10:08:36
 */
public interface ConsumableStoreStockService extends IService<ConsumableStoreStock> {
    void delete(Long id);

    ConsumableStoreStockVO getById(Long id);

    Pagination<ConsumableStoreStockExtendVO> getPage(ConsumableInStoreParam param);

    /**
     * 统计即时库存总金额
     */
    MoneyNumVO getTotalMoney(String keyword, String type);

    /**
     * 处理仓库库存信息
     *
     * @param addRecord 入库信息
     */
    void insertOrUpdateStock(ConsumableAddRecord addRecord);

    /**
     * 处理仓库库存信息
     *
     * @param reduceRecord 出库信息
     */
    void insertOrUpdateStock(ConsumableReduceRecord reduceRecord, List<ConsumableStockDetail> stockDetail);

    /**
     * key:耗材ID
     * value:均价
     *
     * @param storeId
     * @param details
     *
     * @return
     */
    Map<Long, BigDecimal> getConsumableStorePrice(Long storeId, List<ConsumableStockDetailParam> details);

    /**
     * 查询当前耗材总数
     */
    Integer getConsumableNumber();

    /**
     * 更新库存占用数量
     *
     * @param storeId
     * @param consumableId
     * @param num
     */
    void updateOccupyStock(Long storeId, Long consumableId, Integer num, Boolean add);
}