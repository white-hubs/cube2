package com.guhecloud.cube.srv.gen.securitymanagement.server.boiler.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class BoilerResultVo {
    private List<String> xtitle;
    private Map<String, List<BoilerDataVo>> dataMap;
}
