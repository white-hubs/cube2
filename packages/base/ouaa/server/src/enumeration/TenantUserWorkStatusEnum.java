package com.guhecloud.cube.biz.ouaa.server.enumeration;

import com.guhecloud.cube.biz.ouaa.server.dto.KeyValueDTO;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 租户人员状态
 */
@Getter
public enum TenantUserWorkStatusEnum {

    REST("休息"),

    WORKING("在岗"),

    LEAVE_POST("离岗"),

    AWAY_BUSINESS("出差"),

    ASK_LEAVE("请假");

    final String value;

    TenantUserWorkStatusEnum(String value) {
        this.value = value;
    }

    public static List<KeyValueDTO> getAllUserTotal(List<KeyValueDTO> statusList) {
        List<KeyValueDTO> status = new ArrayList<>();
        for (TenantUserWorkStatusEnum ex : TenantUserWorkStatusEnum.values()) {
            boolean bool = true;
            if (ObjectUtils.isNotEmpty(statusList)) {
                for (KeyValueDTO total : statusList) {
                    if (ex.name().equals(total.getName())) {
                        status.add(total);
                        bool = false;
                    }
                }
            }
            if (bool) {
                status.add(new KeyValueDTO(ex.name(), 0));
            }
        }
        if (ObjectUtils.isNotEmpty(statusList)) {
            for (KeyValueDTO ex : statusList) {
                if (ObjectUtils.isEmpty(ex.getName())) {
                    status.add(new KeyValueDTO("其他", ex.getValue()));
                    break;
                }
            }
        }
        return status;
    }

}
