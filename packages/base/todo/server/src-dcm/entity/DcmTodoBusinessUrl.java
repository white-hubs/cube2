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
public class DcmTodoBusinessUrl implements Identifiable {

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 业务类型名称
     */
    private String businessTypeName;

    /**
     * 访问端
     */
    private String terminal;

    /**
     * 跳转地址
     */
    private String url;

    @Override
    public String getId() {
        return getBusinessType() + ":" + getTerminal();
    }

    @Override
    public String getFingerPrint() {
        return String.format("%s:%s", businessTypeName, url);
    }
}
