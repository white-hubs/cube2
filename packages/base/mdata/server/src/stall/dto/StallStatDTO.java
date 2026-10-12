package com.guhecloud.cube.biz.mdata.server.stall.dto;

import lombok.Data;

/**
 * 统计区域下的档位数量
 *
 * @author lzy
 * @date 2021/10/19
 */
@Data
public class StallStatDTO {
    private Long areaId;
    private String areaName;
    private Integer stallNum;
}
