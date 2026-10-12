package com.guhecloud.cube.biz.todo.dcm.entity;

import com.guhecloud.platform.dcm.Identifiable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DcmTodoTag implements Identifiable {

    /**
     * 编号
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    @Override
    public String getId() {
        return getCode();
    }

    @Override
    public String getFingerPrint() {
        return String.format("%s", name);
    }
}
