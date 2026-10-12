package com.guhecloud.cube.biz.traffic.detection.server.detection.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 人流区域信息返回参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrafficDetectionAreaVO {

    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 区域模型
     */
    private String modelArea;

    /**
     * 备注
     */
    private String remark;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 实时人流量（区域内设备 currentPeopleNum 汇总），仅分页接口等场景填充
     */
    private Integer personFlowCount;
}
