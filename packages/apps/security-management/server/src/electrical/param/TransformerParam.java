package com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.param;

import lombok.Data;

@Data
public class TransformerParam {
    private Long transformer;
    private String transformerName;
    private Long highDistribution;
    private String highDistributionName;
}
