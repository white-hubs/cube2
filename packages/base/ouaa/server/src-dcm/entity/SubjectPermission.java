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
public class SubjectPermission implements Identifiable {
    @JsonIgnore
    private String subjectType; /* ROLE, CLIENT */

    @JsonIgnore
    private String subjectCode;  /* 为后续兼容处理，此处的code实际为 product code + 分隔符 + subject Name */

    /**
     * 权限编号
     */
    private String permissionCode;

    /**
     * 扩展配置
     */
    private String extentConfig;

    @Override
    @JsonIgnore
    public String getId() {
        return String.format("%s:%s:%s", subjectType, subjectCode, permissionCode);
    }

    @Override
    @JsonIgnore
    public String getFingerPrint() {
        return (extentConfig == null ? "" : extentConfig);
    }
}
