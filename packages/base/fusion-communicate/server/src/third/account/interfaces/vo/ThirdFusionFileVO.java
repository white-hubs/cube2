package com.guhecloud.cube.biz.fusion.communicate.server.third.account.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 文件信息
 *
 * @author lanchun
 * @date 2024/8/5 11:15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThirdFusionFileVO {

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
    private String size;

    /**
     * 文件后缀
     */
    private Integer suffix;

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
