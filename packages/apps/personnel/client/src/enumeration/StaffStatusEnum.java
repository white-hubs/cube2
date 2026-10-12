package com.guhecloud.cube.srv.gen.personnel.management.client.enumeration;

import lombok.Getter;

/**
 * @author bq
 * @date 2023/03/06
 * @description 员工状态
 */
@Getter
public enum StaffStatusEnum {

    FORMAL("正式"),
    LEAVE_OFFICE("离职"),
    ON_TRIAL("试用");

    String value;

    StaffStatusEnum(String value) {
        this.value = value;
    }

}
