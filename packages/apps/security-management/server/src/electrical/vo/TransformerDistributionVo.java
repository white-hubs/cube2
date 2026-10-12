package com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.vo;

import lombok.Data;

@Data
public class TransformerDistributionVo {
    private Long id;
    //变压器id
    private Long transformer;
    //变压器名称
    private String transformerName;
    //高压配电
    private Long highDistribution;
    //高压配电名称
    private String highDistributionName;
}
