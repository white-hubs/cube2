package com.guhecloud.cube.biz.emergency.server.param;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class EmergencyFileTypeModifyParam {

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空")
    @Size(max = 30, message = "名称不能大于30个字")
    private String name;

    /**
     * 备注
     */
    @Size(max = 250, message = "备注不能大于250个字")
    private String remark;
}
