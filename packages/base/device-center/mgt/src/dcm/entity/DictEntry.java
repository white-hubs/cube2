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
public class DictEntry implements Identifiable  {

    /**
     * 字典类型
     */
    @JsonIgnore
    private String typeCode;

    /**
     * 字典上级
     */
    @JsonIgnore
    private String parentCode;

    /**
     * 字典排序
     */
    private Integer itemSort = 1;

    /**
     * 字典键
     */
    private String key;

    /**
     * 字典key的可读标签
     */
    private String label;

    /** 字典value, 保留字段， 目前没有映射 */
    @Builder.Default
    private String value = "";

    /**
     * 状态 0正常 1停用
     */
    @Builder.Default
    private int status = 0;

    /**
     * 备注
     */
    @Builder.Default
    private String remark = "";

    @Override
    @JsonIgnore
    public String getId() {
        return String.format("%s:%s:%s", typeCode, parentCode, key);
    }

    @Override
    @JsonIgnore
    public String getFingerPrint() {
        return String.format("%s:%s:%s:%s", label, status, remark == null ? "": remark, itemSort == null ? 0L : itemSort);
    }
}
