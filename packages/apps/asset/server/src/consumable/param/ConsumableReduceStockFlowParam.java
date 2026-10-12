package com.guhecloud.cube.srv.gen.property.management.server.consumable.param;

import com.guhecloud.cube.srv.gen.property.management.server.consumable.enumeration.BusinessTypeEnum;
import com.guhecloud.platform.base.util.ObjectUtils;
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
public class ConsumableReduceStockFlowParam {
    //仓库ID
    private Object storeId;

    private Object deptId;
    //申领人
    private Object claimant;
    //出库日期
    private LocalDateTime reduceDate;

    //处理人
    private Object handler;
    //备注
    private String remark;
    //业务类型
    private String businessType;

    private List<ConsumableStockDetailParam> details;

    public ConsumableReduceStockParam getConsumableReduceStockParam() {
        ConsumableReduceStockParam reduceRecord = new ConsumableReduceStockParam();
        reduceRecord.setDetails(details);
        reduceRecord.setReduceDate(reduceDate);
        reduceRecord.setRemark(remark);
        reduceRecord.setBusinessType(BusinessTypeEnum.APPROVE.name());
        if (ObjectUtils.isNotEmpty(deptId)) {
            if (deptId instanceof List<?> deptIdList) {
                reduceRecord.setDeptId(Long.parseLong(deptIdList.getFirst().toString()));
            } else {
                reduceRecord.setDeptId(Long.parseLong(deptId.toString()));
            }
        }
        if (ObjectUtils.isNotEmpty(storeId)) {
            if (storeId instanceof List<?> storeIdList) {
                reduceRecord.setStoreId(Long.parseLong(storeIdList.getFirst().toString()));
            } else {
                reduceRecord.setStoreId(Long.parseLong(storeId.toString()));
            }
        }
        if (ObjectUtils.isNotEmpty(handler)) {
            if (handler instanceof List<?> handlerList) {
                reduceRecord.setHandler(Long.parseLong(handlerList.getFirst().toString()));
            } else {
                Object userId = handler;
                reduceRecord.setHandler(Long.parseLong(userId.toString()));
            }
        }
        if (ObjectUtils.isNotEmpty(claimant)) {
            if (claimant instanceof List<?> claimantList) {
                reduceRecord.setClaimant(Long.parseLong(claimantList.getFirst().toString()));
            } else {
                Object userId = claimant;
                reduceRecord.setClaimant(Long.parseLong(userId.toString()));
            }
        }

        return reduceRecord;
    }

}