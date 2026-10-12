package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.enumeration;

import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2023-09-22  15:33
 * @Description:
 * @Version: 1.0
 */
@Getter
public enum ClockInAuthorityEnum {
    // 打卡类型（pc可以进行操作权限）
    REVOKE_CLOCK("撤销", null),
    PC_CLOCK("pc新增打卡", ClockInEnum.REVOKE_CLOCK),
    DISCARD_CLOCK("作废", ClockInEnum.MY_CLOCK),
    MY_CLOCK("员工打卡", ClockInEnum.DISCARD_CLOCK),
    ;
    private final String remarks;
    private final ClockInEnum authority;
    public static final List<String> clockType = new ArrayList<>();

    ClockInAuthorityEnum(String remarks, ClockInEnum authority) {
        this.remarks = remarks;
        this.authority = authority;
    }

    public static String getNext(String name) {
        String nextName = "";
        if (ObjectUtils.isEmpty(name)) {
            return nextName;
        }
        ClockInAuthorityEnum[] clock = ClockInAuthorityEnum.values();
        for (ClockInAuthorityEnum ex : clock) {
            if (name.equals(ex.name())) {
                return ex.getAuthority().name();
            }
        }
        return nextName;
    }
}
