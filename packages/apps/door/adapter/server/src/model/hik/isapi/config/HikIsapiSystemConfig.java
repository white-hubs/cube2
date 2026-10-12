package com.guhecloud.cube.srv.gen.adapter.access.control.server.model.hik.isapi.config;

import lombok.Data;

@Data
public class HikIsapiSystemConfig {

    private String code;

    private boolean enabled = true;

    private String host;

    private String account;

    private String pwd;

    private String doorName;
}
