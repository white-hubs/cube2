package com.guhecloud.cube.srv.gen.adapter.information.server.das.qo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * das发送节目参数
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DasSendProgramParam {
    /**
     * 终端设备号，决定节目要发送到哪个终端
     */
    private String deviceId;

    /**
     * 要发送的节目id
     */
    private String programId;

    /**
     * 发送名称，可为空
     */
    private String name;

    /**
     * 优先级，0为一般节目，1为插播节目
     */
    private String priority;

    /**
     * 节目开始时间
     */
    private String beginTime;

    /**
     * 节目结束时间
     */
    private String endTime;

}
