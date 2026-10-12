package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param;

import lombok.Getter;

import jakarta.validation.constraints.NotNull;

import java.util.List;

@Getter
public class InspectStandardToTypeCommandModel {
    //对象分类
    @NotNull(message = "对象分类不能为空")
    private String targetCategory;

    //对象类型
    @NotNull(message = "对象类型不能为空")
    private String targetType;

    /**
     * 巡检标准列表
     */
    //    @NotEmpty(message = "巡检标准不能为空")
    private List<Long> standardIds;

}
