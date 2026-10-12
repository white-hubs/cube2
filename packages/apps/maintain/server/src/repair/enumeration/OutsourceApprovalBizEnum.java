package com.guhecloud.cube.srv.gen.maintain.server.repair.enumeration;

import com.guhecloud.cube.srv.gen.maintain.server.repair.dto.OutsourceWorkOrderDTO;
import lombok.Getter;

/**
 * @author wql
 * @describe 委外维修流程表单枚举
 * @date 2024/1/8 14:09
 */
@Getter
public enum OutsourceApprovalBizEnum {
    OUTSOURCE_REPAIR("委外维修", OutsourceWorkOrderDTO.class);

    final String desc;
    final Class clazz;

    OutsourceApprovalBizEnum(String desc, Class clazz) {
        this.desc = desc;
        this.clazz = clazz;
    }
}
