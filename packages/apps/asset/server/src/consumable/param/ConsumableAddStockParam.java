package com.guhecloud.cube.srv.gen.property.management.server.consumable.param;

import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.ConsumableAddRecord;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.enumeration.BusinessTypeEnum;
import com.guhecloud.cube.srv.gen.property.management.server.property.enumeration.FlowResult;
import com.guhecloud.cube.srv.gen.property.management.server.property.enumeration.OperateType;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Appet
 * @Description 入库参数
 * @date 2023-03-16 10:08:36
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsumableAddStockParam {
    //仓库
    private Long storeId;
    //入库日期
    private LocalDateTime addDate;
    //供应商
    private String supplier;
    //处理人
    private List handler;
    //备注
    private String remark;
    //业务类型
    private String businessType;
    private List<Long> files;

    private List<ConsumableStockDetailParam> details;
    /**
     * 状态
     */
    private String status;
    /**
     * 入库单号
     */
    private String depositoryNumbers;
    /**
     * 流程id
     */
    private String processInstanceId;

    public ConsumableAddRecord getConsumableAddRecord() {
        ConsumableAddRecord record = new ConsumableAddRecord();
        record.setAddDate(addDate);
        record.setRemark(remark);
        record.setSupplier(supplier);
        record.setBusinessType(businessType);
        record.setStoreId(storeId);
        record.setStatus(FlowResult.UNDER.name());
        record.setBusinessType(BusinessTypeEnum.APPROVE.name());
        record.setDepositoryNumbers(OperateType.getNumbers(OperateType.CONSUMABLE_ADD));
        if (ObjectUtils.isNotEmpty(handler) && !handler.isEmpty()) {
            Object userId = handler.getFirst();
            record.setHandler(Long.parseLong(userId.toString()));
        }
        return record;
    }

}