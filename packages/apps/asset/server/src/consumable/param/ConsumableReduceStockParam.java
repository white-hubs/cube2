package com.guhecloud.cube.srv.gen.property.management.server.consumable.param;

import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.ConsumableReduceRecord;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.enumeration.BusinessTypeEnum;
import com.guhecloud.cube.srv.gen.property.management.server.property.enumeration.FlowResult;
import com.guhecloud.cube.srv.gen.property.management.server.property.enumeration.OperateType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Appet
 * @Description 出库参数
 * @date 2023-03-16 10:08:36
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsumableReduceStockParam {
    //仓库ID
    private Long storeId;

    private Long deptId;
    //申领人
    private Long claimant;
    //出库日期
    private LocalDateTime reduceDate;

    //处理人
    private Long handler;
    //备注
    private String remark;
    //业务类型
    private String businessType;
    private String processInstanceId;

    private List<ConsumableStockDetailParam> details;

    public ConsumableReduceRecord getConsumableReduceRecord() {
        ConsumableReduceRecord reduceRecord = new ConsumableReduceRecord();
        reduceRecord.setProcessInstanceId(processInstanceId);
        reduceRecord.setDeptId(deptId);
        reduceRecord.setReduceDate(reduceDate);
        reduceRecord.setStoreId(storeId);
        reduceRecord.setRemark(remark);
        reduceRecord.setStatus(FlowResult.UNDER.name());
        reduceRecord.setBusinessType(BusinessTypeEnum.APPROVE.name());
        reduceRecord.setDepositoryNumbers(OperateType.getNumbers(OperateType.CONSUMABLE_REDUCE));
        reduceRecord.setHandler(handler);
        reduceRecord.setClaimant(claimant);
        return reduceRecord;
    }

}