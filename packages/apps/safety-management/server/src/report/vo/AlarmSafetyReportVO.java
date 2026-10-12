package com.guhecloud.cube.srv.gen.safety.management.report.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: lijie
 * @CreateTime: 2024-04-10  11:38
 * @Description:
 * @Version: 1.0
 */
@Data
public class AlarmSafetyReportVO {
    private Long id;
    /**
     * 标题
     */
    private String name;
    /**
     * 生成时间
     */
    private LocalDateTime createTime;
    /**
     * 安全报告附件id
     */
    private Long fileId;
}
