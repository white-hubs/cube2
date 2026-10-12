package com.guhecloud.cube.srv.gen.maintain.server.electron.enumeration;

import lombok.Getter;

/**
 * @author wql
 * @describe 巡更结果
 * @date 2023/5/29 10:52
 */
@Getter
public enum PatrolResultEnum {
    NORMAL("正常"),
    ABNORMAL("异常");
    private String remarks;

    PatrolResultEnum(String remarks) {
        this.remarks = remarks;
    }
}
