package com.guhecloud.cube.srv.tj.electronic.guide.enumeration;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-08  14:13
 * @Description:
 * @Version: 1.0
 */
public enum LayerStateEnum {
    IN_USE("已使用"),
    NOT_USED("未使用");
    private String remarks;

    LayerStateEnum(String remarks) {
        this.remarks = remarks;
    }
}
