package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param;

import lombok.Data;

import java.util.List;

@Data
public class RiskIdentificationSubmitParam {
    /**
     * 本次提交的风险对象集合；新增、修改、删除都必须显式放入该集合。
     */
    private List<RiskIdentificationObjectParam> objects;
}
