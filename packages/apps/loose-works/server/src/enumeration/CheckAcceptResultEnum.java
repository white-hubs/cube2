package com.guhecloud.cube.srv.gen.loose.works.enumeration;

import lombok.Getter;

/**
 * @author wql
 * @describe 验收结果
 * @date 2023/9/14 20:00
 */
@Getter
public enum CheckAcceptResultEnum {
    QUALIFIED("验收合格"),
    DISQUALIFICATION("验收不合格");
    final String describe;

    CheckAcceptResultEnum(String describe) {
        this.describe = describe;
    }

    public String getDescribe() {
        return describe;
    }
}
