package com.guhecloud.cube.srv.gen.adapter.information.server.yozhiled.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 发送节目到终端的返回值
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendProgramToTerminalResponse {
    /**
     * 发送 id，查询发送结果，或者取消的时候需要传入这个
     */
    private String id;

    /**
     * 第三方终端id
     */
    private String terminalId;

    /**
     * 状态 0 表示发送不存在,1:等待下载,2:下载中,3：下载失败,4：播放中,5:播放完成,6：等待播放
     */
    private Integer status;
}
