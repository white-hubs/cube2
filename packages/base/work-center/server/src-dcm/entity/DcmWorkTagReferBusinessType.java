package com.guhecloud.cube.biz.work.center.dcm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.guhecloud.platform.dcm.Identifiable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DcmWorkTagReferBusinessType implements Identifiable {

    /**
     * 标签编号
     */
    @JsonIgnore
    private String workTag;

    /**
     * 业务类型
     */
    private String businessType;

    @Override
    @JsonIgnore
    public String getId() {
        return getWorkTag() + ":" + getBusinessType();
    }

    @Override
    @JsonIgnore
    public String getFingerPrint() {
        return "";
    }
}
