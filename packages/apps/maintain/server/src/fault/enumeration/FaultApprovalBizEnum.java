package com.guhecloud.cube.srv.gen.maintain.server.fault.enumeration;

import com.guhecloud.cube.srv.gen.maintain.server.fault.dto.ComplaintOrderDTO;
import com.guhecloud.cube.srv.gen.maintain.server.fault.dto.FaultOrderDTO;
import lombok.Getter;

/**
 * @author wql
 * @describe 故障单流程表单枚举
 * @date 2023/11/6 11:17
 */
@Getter
public enum FaultApprovalBizEnum {
    FAULT("故障单", FaultOrderDTO.class),
    COMPLAINT("投诉建议", ComplaintOrderDTO.class),
    ;

    final String desc;
    final Class clazz;

    FaultApprovalBizEnum(String desc, Class clazz) {
        this.desc = desc;
        this.clazz = clazz;
    }
}
