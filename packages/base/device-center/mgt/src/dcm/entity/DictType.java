package com.guhecloud.cube.iot.mgt.dcm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.guhecloud.platform.dcm.Identifiable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_DEFAULT )
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DictType implements Identifiable {

    /**  字典项名称 */
    private String name;

    /**
     * 字典编码别名(用于查询)
     */
    private String code;

    /**
     * 状态0正常 1停用
     */
    @Builder.Default
    private Integer status = 0;

    /**
     * 字典类型中类型:0,系统字典;1业务字典
     */
    @Builder.Default
    private Integer type = 0;

    /**
     * 备注
     */
    @Builder.Default
    private String remark = "";

    @Override
    @JsonIgnore
    public String getId() {
        return getCode();
    }

    @Override
    @JsonIgnore
    public String getFingerPrint() {
        return String.format("%s:%s:%s:%s", name, status, type, remark == null ? "": remark);
    }
}
