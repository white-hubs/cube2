package com.guhecloud.cube.biz.emergency.server.param;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Data
public class EmergencyEventTypeModifyParam {

    /**
     * 分类名称
     */
    @NotEmpty(message = "分类名称不能大于50个字符")
    private String name;

    /**
     * 备注说明
     */
    @Size(max = 200, message = "备注说明不能大于200个字符")
    private String remark;
}
