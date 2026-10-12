package com.guhecloud.cube.biz.ouaa.server.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author bq
 * @date 2022/11/08
 * @description 职位管理
 */
@Data
public class AppointmentDetailVO {

    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String remark;

    /**
     * 启停用状态
     */
    private Boolean enable;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}