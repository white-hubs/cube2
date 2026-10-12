package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param;

import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotEmpty;

@Getter
public class InspectCronCommandModel {

    /**
     * ID
     */
    private Long id;

    /**
     * 名称
     */
    @NotEmpty(message = "名称不能为空")
    @Length(max = 50)
    private String name;

    /**
     * 表达式
     */

    @NotEmpty(message = "表达式不能为空")
    @Length(max = 50)
    private String cron;

    /**
     * 描述
     */

    @Length(max = 450)
    private String description;

}
