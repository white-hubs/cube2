package com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.api.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import static com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.api.constants.DaHuaApiConstant.SNAP_CAPTURE;

@EqualsAndHashCode(callSuper = true)
@Data
public class DaHuaSnapCaptureRequest extends AbstractDaHuaRequest {

    /**
     * 设备编号
     */
    private String deviceCode;

    /**
     * 固定值 generalJsonTransport
     */
    @Getter
    private String operation = "generalJsonTransport";

    /**
     * 操作对象Operation json字符串
     */
    private String params;

    /**
     * 操作对象Operation
     */
    @JsonIgnore
    private Operation operationObj;

    @Override
    public String getRequestUrl() {
        return getRequestUrl(SNAP_CAPTURE).build();
    }

    @Data
    public static class Operation {

        /**
         * 消息序号,确保唯一
         */
        private Long id;

        /**
         * 固定值 dev.snap
         */
        private final String method = "dev.snap";

        /**
         * 设备信息
         */
        private Device params;
    }

    @Data
    public static class Device {

        /**
         * 设备编号，同deviceCode
         */
        @JsonProperty("DevID")
        private String devID;

        /**
         * 通道编号。默认是0,需要手动指定
         */
        @JsonProperty("DevChannel")
        private Integer devChannel;

        /**
         * 抓图类型， 固定1
         */
        @JsonProperty("SnapType")
        private final Integer snapType = 1;

        /**
         * 命令源， 固定0
         */
        @JsonProperty("CmdSrc")
        private final Integer cmdSrc = 0;
    }
}
