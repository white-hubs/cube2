package com.guhecloud.cube.srv.gen.conferencemanage.server.third.vo;

import lombok.Data;

/**
 * 会议屏适配配置
 *
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/5/22 15:19
 * @Description:
 */
@Data
public class RoomScreenAdapterConfig {

    private String tenant;

    private String serviceAddr;

    private String manufacturer;

    private ManufacturerJson manufacturerJson;

    @Data
    public static class ManufacturerJson {

        private String authInfo;
    }
}
