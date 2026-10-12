package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param;

import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotEmpty;

@Getter
public class InspectTypeCommandModel {

    /**
     * 名称
     */
    @NotEmpty(message = "名称不能为空")
    @Length(max = 255)
    private String name;

}
