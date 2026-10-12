package com.guhecloud.cube.srv.gen.device.supervise.server.analysis.interfaces.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author pengjian
 * @date 2022/8/1 17:42
 * @description
 */
@Data
public class HealthValuePageVO {
    private Long id;
    private String code;
    private String name;
    private String productCategoryName;
    private String areaName;
    private Integer healthValue;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
}
