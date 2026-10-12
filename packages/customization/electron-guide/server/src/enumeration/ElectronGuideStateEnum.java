package com.guhecloud.cube.srv.electron.guide.enumeration;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-31  11:16
 * @Description:
 * @Version: 1.0
 */
public enum ElectronGuideStateEnum {
    ENABLE("启用"),
    DISABLE("禁用");
    private String remarks;

    ElectronGuideStateEnum(String remarks) {
        this.remarks = remarks;
    }
}
