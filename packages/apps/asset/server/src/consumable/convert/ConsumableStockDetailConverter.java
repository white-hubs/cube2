package com.guhecloud.cube.srv.gen.property.management.server.consumable.convert;

import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.Consumable;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.ConsumableAddRecord;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.ConsumableStockDetail;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.ConsumableStoreStock;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.enumeration.StockTypeEnum;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.template.ConsumableAddRecordTemplate;

import java.math.BigDecimal;

public class ConsumableStockDetailConverter {
    public static ConsumableStockDetail convertToConsumableStockDetail(ConsumableAddRecordTemplate consumableAddRecordTemplate,
                                                                       ConsumableAddRecord consumableAddRecord,
                                                                       Consumable consumable,
                                                                       ConsumableStoreStock consumableStoreStock
                                                                      ) {
        var num = consumableAddRecordTemplate.getCurrentStock() - consumableAddRecordTemplate.getBeforeStock();

        var consumableStockDetail = new ConsumableStockDetail();
        consumableStockDetail.setStockId(consumableAddRecord.getId());
        consumableStockDetail.setStockFlag(StockTypeEnum.ADD.name());
        consumableStockDetail.setConsumableId(consumable.getId());
        consumableStockDetail.setNum(num);
        consumableStockDetail.setPrice(consumableAddRecordTemplate.getAvgPrice());
        consumableStockDetail.setAmount(new BigDecimal(consumableStockDetail.getNum()).multiply(consumableStockDetail.getPrice()));
        consumableStockDetail.setInitNum(consumableStoreStock != null ? consumableStoreStock.getNum() : 0);
        consumableStockDetail.setInitAmount(consumableStoreStock != null ? consumableStoreStock.getAmount() : BigDecimal.ZERO);
        return consumableStockDetail;
    }

}
