package com.guhecloud.cube.biz.ouaa.client.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author lijie
 * @date 2023/11/07
 * @description 职位管理
 */
@Data
public class AppointmentDetailDTO {

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