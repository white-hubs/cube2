package com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.param;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

import java.util.List;

@Data
public class CollectConfigureParam {
    //周期
    @NotNull(message = "周期不能为空")
    private Integer timeCycle;
    //采集时间
    @NotNull(message = "时间不能为空")
    private String readeTime;
    //应用
    private String applicationCode;

    private List<TransformerParam> transformers;
}
