package com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.api.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 抓图返回信息
 *
 * @author lanchun
 * @date 2025/5/19 15:47
 */
@Data
public class DaHuaSnapCaptureResponse {

    /**
     * 消息序号
     */
    private Long id;

    /**
     * 固定值 dev.snap
     */
    private String method;

    /**
     * 截图信息
     */
    private PicInfo params;

    /**
     * 截图信息
     */
    @Data
    public static class PicInfo {

        /**
         * 通道号
         */
        @JsonProperty("DevChannel")
        private Long devChannel;

        /**
         * 设备编码
         */
        @JsonProperty("DevID")
        private String devID;

        /**
         * 图片相对地址
         */
        @JsonProperty("PicInfo")
        private String picInfo;

        /**
         * 固定 1
         */
        @JsonProperty("PicInfoType")
        private String picInfoType;

        /**
         * 固定 0
         */
        private String cmdSource;
    }
}
