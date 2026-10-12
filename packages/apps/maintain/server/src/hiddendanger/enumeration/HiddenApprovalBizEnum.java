package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.enumeration;

import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.dto.HiddenDangerNewDTO;
import lombok.Getter;

/**
 * @author wql
 * @describe 隐患单
 * @date 2023/11/9 19:28
 */
@Getter
public enum HiddenApprovalBizEnum {
    HIDDENDANGER("隐患单", HiddenDangerNewDTO.class);
    final String desc;
    final Class clazz;

    HiddenApprovalBizEnum(String desc, Class clazz) {
        this.desc = desc;
        this.clazz = clazz;
    }
}
