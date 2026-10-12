package com.guhecloud.cube.srv.gen.property.management.server.property.enumeration;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum MessageModuleTypeEnum {
    MAINTENANCE_OVERDUE_ABOUT("距离资产维保到期", "距离维保到期还有"),
    MAINTENANCE_OVERDUE("资产维保到期已过", "维保期限已过"),
    OPERATE_OVERDUE_ABOUT("距离资产使用到期", "距离使用期限到期还有"),
    OPERATE_OVERDUE("资产使用期限已过", "使用期限已过"),
    CONSUMABLE_LOWER("耗材库存低于下限", "库存过低"),
    CONSUMABLE_UPPER("耗材库存超过上限", "库存过高"),
    INVENTORY_TASK("盘点任务", "");
    private final String remarks;
    private final String message;

    MessageModuleTypeEnum(String remarks, String message) {
        this.remarks = remarks;
        this.message = message;
    }

    public static List<String> getNameList() {
        List<String> nameList = new ArrayList<>();
        nameList.add(MAINTENANCE_OVERDUE_ABOUT.name());
        nameList.add(MAINTENANCE_OVERDUE.name());
        nameList.add(OPERATE_OVERDUE_ABOUT.name());
        nameList.add(OPERATE_OVERDUE.name());
        return nameList;
    }
}
