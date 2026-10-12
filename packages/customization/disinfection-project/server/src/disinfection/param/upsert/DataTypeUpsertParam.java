package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.upsert;

import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DisinfectionDataTypeEnum;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class DataTypeUpsertParam {
    /**
     * 业务类型
     */
    @NotNull
    DisinfectionDataTypeEnum dataType;
    /**
     * 业务名称
     */
    @NotBlank(message = "名称不能为空")
    private String dataName;

    /**
     * 类型图标
     */
    private Long icon;
    /**
     * 备注信息
     */
    private String remark;
}
