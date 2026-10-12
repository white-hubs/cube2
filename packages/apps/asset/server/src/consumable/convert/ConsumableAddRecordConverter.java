package com.guhecloud.cube.srv.gen.property.management.server.consumable.convert;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.srv.gen.property.management.server.common.entity.Storehouse;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.ConsumableAddRecord;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.enumeration.BusinessTypeEnum;
import com.guhecloud.cube.srv.gen.property.management.server.property.enumeration.FlowResult;
import com.guhecloud.cube.srv.gen.property.management.server.property.enumeration.OperateType;
import com.guhecloud.platform.base.util.DateUtils;

public class ConsumableAddRecordConverter {
    public static ConsumableAddRecord convertToConsumableAddRecord(Storehouse store) {
        //添加入库记录
        var consumableAddRecord = new ConsumableAddRecord();
        consumableAddRecord.setStoreId(store.getId());
        consumableAddRecord.setAddDate(DateUtils.now());
        consumableAddRecord.setStatus(FlowResult.PASS.name());
        consumableAddRecord.setId(IdWorker.getId());
        consumableAddRecord.setDepositoryNumbers(OperateType.getNumbers(OperateType.CONSUMABLE_ADD));
        consumableAddRecord.setBusinessType(BusinessTypeEnum.IMPORT.name());
        consumableAddRecord.setHandler(null);
        consumableAddRecord.setSupplier(null);
        consumableAddRecord.setRemark(null);
        consumableAddRecord.setProcessInstanceId(null);
        return consumableAddRecord;
    }
}
