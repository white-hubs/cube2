package com.guhecloud.cube.srv.gen.loose.works.dto.enumeration;

import lombok.Getter;

/**
 * @author wql
 * @describe 工程状态枚举
 * @date 2023/9/14 19:27
 */
@Getter
public enum ProjectStateEnum {
    APPLYING("申请中"),
    SIGNING("签约中"),
    UNDERWAY("进行中"),
    COMPLETED("已完成"),
    ABNORMAL("异常"),
    WAIT_ACCEPT("待验收");
    private final String describe;

    ProjectStateEnum(String describe) {
        this.describe = describe;
    }
}
