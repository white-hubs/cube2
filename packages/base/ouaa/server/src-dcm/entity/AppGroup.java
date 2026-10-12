package com.guhecloud.cube.biz.ouaa.dcm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.guhecloud.platform.dcm.Identifiable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Configuration Model Mapping to ApplicationGroup
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AppGroup implements Identifiable {
    private String name;
    private String remark;

    public static AppGroup from(Map<String, Object> map) {
        String name = map.get("name").toString();
        Object remark = map.get("remark");
        return AppGroup.builder().name(name).remark(remark == null ? "" : remark.toString()).build();
    }

    @Override
    @JsonIgnore
    public String getId() {
        return getName();
    }

    @Override
    @JsonIgnore
    public String getFingerPrint() {
        return getRemark() == null ? "" : getRemark();
    }
}
