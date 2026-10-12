package com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.command;

import lombok.Data;

@Data
public class ModifyQrRouteCommand {

    private String targetUrl;
    private String targetParams;
}
