package com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.vo;

import lombok.Data;

import java.util.List;

@Data
public class CollectConfigureVo {
    private Long id;
    //周期
    private Integer timeCycle;
    //采集时间
    private String readeTime;
    //应用
    private String applicationCode;

    List<TransformerDistributionVo> transformerDistributionVos;
}
