package com.guhecloud.cube.biz.ouaa.dcm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.guhecloud.platform.dcm.Identifiable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Permission implements Identifiable {
    /**
     * 权限类型
     */
    private String type;

    /**
     * 权限编号
     */
    private String code;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 权限描述
     */
    private String description;

    @Override
    @JsonIgnore
    public String getId() {
        return getCode();
    }

    @Override
    @JsonIgnore
    public String getFingerPrint() {
        return String.format("%s:%s:%s", type, name, description == null ? "" : description);
    }
}
