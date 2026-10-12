package com.guhecloud.cube.biz.adapter.park.server.application.jslife.param;

import lombok.Getter;

@Getter
public class JSLifeParkConfig {

    /**
     * 商户号 "000000008036417111"
     */
    private String cid;

    /**
     * 开放平台分配的应用id 910978440435326977
     */
    private Long app_id;

    /**
     * 项目编号，在具体接口中查看 "p210232374"
     */
    private String projectCode;

    /**
     * 开放服务编码，在应用中，已开通服务的服务名下有显示，在具体接口中查看 "kfpt_syfw"
     */
    private String abilityCode;

    /**
     * 密钥
     */
    private String secret;

    /**
     * 停车场code（逗号分隔）
     */
    private String parkCodes;
}
