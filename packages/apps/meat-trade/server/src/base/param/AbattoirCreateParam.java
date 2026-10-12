package com.guhecloud.cube.biz.meattrade.server.base.param;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

/**
 * 屠宰场新增信息
 */
@Data
public class AbattoirCreateParam {

    /**
     * 屠宰场名称
     */
    @NotEmpty(message = "名称不能为空")
    @Size(max = 100, message = "屠宰场名称不能大于100")
    private String name;

    /**
     * 屠宰场编号
     */
    @NotEmpty(message = "屠宰场编号不能为空")
    @Size(max = 100, message = "屠宰场编号不能大于100")
    private String code;

    /**
     * 屠宰场简称
     */
    @NotEmpty(message = "屠宰场简称不能为空")
    @Size(max = 100, message = "屠宰场简称不能大于100")
    private String abbr;

    /**
     * 屠宰场地址
     */
    @Size(max = 100, message = "屠宰场地址不能大于100")
    private String address;

    /**
     * 备注信息
     */
    @Size(max = 200, message = "备注不能大于200")
    private String remark;

    /**
     * 是否启用
     */
    private Boolean enabled;

}
