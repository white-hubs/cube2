package com.guhecloud.cube.biz.work.center.server.vo;

import com.guhecloud.cube.biz.work.center.server.enumeration.HandoverStatusEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/10/30 17:03
 * @Description:
 */
@Data
public class HandoverRecordVO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 记录名称
     */
    private String recordName;

    /**
     * 交接任务数
     */
    private Integer taskNum;

    /**
     * 交班人
     */
    private String shiftOverUserName;

    /**
     * 交班人ID
     */
    private Long shiftOverUserId;

    /**
     * 交班时间
     */
    private LocalDateTime shiftOverTime;

    /**
     * 接班人
     */
    private String takeOverUserName;

    /**
     * 接班人ID
     */
    private Long takeOverUserId;

    /**
     * 接班时间
     */
    private LocalDateTime takeOverTime;

    /**
     * 交班状态
     */
    private HandoverStatusEnum status;

    /**
     * 撤销权限
     */
    private Boolean revoke;

}
