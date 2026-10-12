package com.guhecloud.cube.biz.ouaa.server.permission.common.request;

import lombok.Data;

import java.util.Objects;

@Data
public class SubjectPermissionConfigModify {

    /**
     * 权限编号
     */
    private String code;

    /**
     * 权限配置扩充信息
     */
    private String extentConfig;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SubjectPermissionConfigModify that = (SubjectPermissionConfigModify) o;
        return Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}
