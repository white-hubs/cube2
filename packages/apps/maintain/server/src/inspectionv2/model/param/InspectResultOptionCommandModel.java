package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class InspectResultOptionCommandModel {

    /**
     * 选项名称
     */
    @Length(min = 1, max = 16)
    private String itemName;

    /**
     * 选项值
     */
    @Length(min = 1, max = 16, message = "选项值不能超过16个字符")
    private String itemValue;

    /**
     * 是否异常点
     */
    private Boolean errorFlag;

}
