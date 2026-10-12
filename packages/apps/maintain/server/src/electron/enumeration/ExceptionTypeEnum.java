package com.guhecloud.cube.srv.gen.maintain.server.electron.enumeration;

import lombok.Getter;

/**
 * @author wql
 * @describe 异常类型
 * @date 2023/5/29 17:17
 */
@Getter
public enum ExceptionTypeEnum {
    PATROL_ABNORMAL("异常"),
    LEAK_DETECTION("漏巡");
    private String remarks;

    ExceptionTypeEnum(String remarks) {this.remarks = remarks;}
}
