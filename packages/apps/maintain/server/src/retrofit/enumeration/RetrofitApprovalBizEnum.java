package com.guhecloud.cube.srv.gen.maintain.server.retrofit.enumeration;

import com.guhecloud.cube.srv.gen.maintain.server.retrofit.dto.RetrofitOrderDTO;
import lombok.Getter;

/**
 * 更新改造单流程表单枚举
 *
 * @author lanchun
 * @date 2024/1/8 15:36
 */
@Getter
public enum RetrofitApprovalBizEnum {
    RETROFIT("更新改造单", RetrofitOrderDTO.class);

    final String desc;
    final Class clazz;

    RetrofitApprovalBizEnum(String desc, Class clazz) {
        this.desc = desc;
        this.clazz = clazz;
    }
}
