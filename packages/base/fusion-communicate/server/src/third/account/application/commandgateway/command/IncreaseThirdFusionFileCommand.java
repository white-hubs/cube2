package com.guhecloud.cube.biz.fusion.communicate.server.third.account.application.commandgateway.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IncreaseThirdFusionFileCommand {

    private Long id;

    /**
     * 上传时间
     */
    private LocalDateTime time;

    /**
     * 数据类型 视频 还是 语音
     */
    private String type;

    /**
     * 文件大小
     */
    private Integer size;

    /**
     * 文件后缀
     */
    private String suffix;

    /**
     * 文件路径
     */
    private String url;

    /**
     * 主叫用户主键
     */
    private Long callerUserId;

    /**
     * 主叫用户名称
     */
    private String callerName;

    /**
     * 被叫用户主键
     */
    private Long calleeUserId;

    /**
     * 被叫用户名称
     */
    private String calleeName;

    /**
     * 时长 秒
     */
    private Long fileTimeLength;

    /**
     * 创建时间
     */
    private LocalDateTime fileCreateTime;

    /**
     * 结束时间
     */
    private LocalDateTime fileEndTime;
}
