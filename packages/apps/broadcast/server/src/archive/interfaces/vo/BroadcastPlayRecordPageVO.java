package com.guhecloud.cube.biz.subsystem.broadcast.server.archive.interfaces.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 广播播放记录分页返回参数
 *
 * @author lanchun
 * @date 2022/12/13 9:34
 */
@Data
public class BroadcastPlayRecordPageVO {

    private Long id;

    /**
     * 广播时间
     */
    private LocalDateTime broadcastTime;

    /**
     * 广播模板名称
     */
    private String broadcastTemplateName;

    /**
     * 广播模板原内容
     */
    private String broadcastTemplateContent;

    /**
     * 广播模板参数
     */
    private String broadcastTemplateContentParam;

    /**
     * 广播内容
     * 变量替换后的内容
     */
    private String broadcastPlayContent;

    /**
     * 广播次数
     */
    private Integer broadcastPlayNum;

    /**
     * 广播终端
     * 广播设备,多个设备之间以逗号分隔返回
     */
    private String broadcastPlayDevices;

    /**
     * 操作人名称
     */
    private String operatorByName;

    /**
     * 操作人部门名称
     */
    private String operatorByDeptName;
}
