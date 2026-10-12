package com.guhecloud.cube.srv.gen.maintain.dcm.hiddendanger.entity;

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
public class DcmHiddenDangerType implements Identifiable {

    /**
     * 名称
     */
    private String name;

    /**
     * 隐患分类名称
     */
    @JsonIgnore
    private String hiddenDangerCategory;

    @Override
    public String getId() {
        return getHiddenDangerCategory() + ":" + getName();
    }

    @Override
    public String getFingerPrint() {
        return "";
    }
}
