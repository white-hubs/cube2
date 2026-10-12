package com.guhecloud.cube.biz.todo.dcm.entity;

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
public class DcmTodoTagReferBusinessType implements Identifiable {

    /**
     * 标签编号
     */
    @JsonIgnore
    private String todoTag;

    /**
     * 业务类型
     */
    private String businessType;

    @Override
    @JsonIgnore
    public String getId() {
        return getTodoTag() + ":" + getBusinessType();
    }

    @Override
    @JsonIgnore
    public String getFingerPrint() {
        return "";
    }
}
