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
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DcmCard implements Identifiable {

    @JsonIgnore
    private String prodCode;

    @JsonIgnore
    private String terminalCode;

    @JsonIgnore
    private String appCode;

    /**
     * 卡片编号
     */
    private String code;

    /**
     * 卡片名称
     */
    private String name;

    /**
     * 排序数字
     */
    private Integer sort;

    /**
     * 卡片备注
     */
    private String remark;

    /**
     * 是否启用
     */
    private Boolean enabled;

    @Override
    @JsonIgnore
    public String getId() {
        return String.format("%s:%s:%s:%s", prodCode, terminalCode, appCode, code);
    }

    @Override
    @JsonIgnore
    public String getFingerPrint() {
        return String.format("%s:%s:%s:%s", name, remark == null ? "" : remark, sort, enabled);
    }
}
