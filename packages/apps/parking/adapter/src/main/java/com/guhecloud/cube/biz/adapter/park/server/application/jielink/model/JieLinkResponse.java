package com.guhecloud.cube.biz.adapter.park.server.application.jielink.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JieLinkResponse {
    private String code;// 0为成功 其他为失败
    private String msg;

    //授权信息返回
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthResponse extends JieLinkResponse {
        private List<AuthData> data;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthData {
        private String appId;
        private String key;
        private List<ParkingInfo> parkInfo;
    }

    @Data
    public static class ParkingInfo {
        private String projectCode;//项目编号
        private String parkNo;//车场编号
        private String parkName;//车场名称
        private String parkId;//车场ID
    }
}
