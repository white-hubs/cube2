package com.guhecloud.cube.biz.fusion.communicate.server.third.xunyi.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 讯一文件返回参数
 *
 * @author lanchun
 * @date 2024/7/30 15:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class XunYiFileResponseDTO extends XunYiBaseResponseDTO {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 录音文件名称
     */
    private String recordName;

    /**
     * 录音文件存放子路径
     */
    private String subPath;

    /**
     * 开始录音时间(yyyy-MM-dd HH:mm:ss)
     */
    private LocalDateTime createTime;

    /**
     * 结束录音时间(yyyy-MM-dd HH:mm:ss)
     */
    private LocalDateTime releaseTime;

    /**
     * 录音时长（s）
     */
    private Integer timeLength;

    /**
     * 查听次数
     */
    private Integer readTimes;

    /**
     * 录音设备 ID
     */
    private Integer recordServerId;

    /**
     * 呼叫类型
     */
    private Integer recordType;

    /**
     * 主叫号码
     */
    private String caller;

    /**
     * 被叫号码
     */
    private String called;

    /**
     * 主叫号码名称
     */
    private String callerName;

    /**
     * 被叫号码名称
     */
    private String calledName;

    /**
     * 0 - 录音， 1- 录像
     */
    private Integer fileType;

    /**
     * UUID
     */
    private String thirdUUID;

    /**
     * 省份
     */
    private String province;

    /**
     * 存储路径
     */
    private String webPATH;

    /**
     * 会话唯一标识
     */
    private String cid;
}
