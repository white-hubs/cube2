package com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.vo;

import com.guhecloud.cube.base.flow.client.dto.ProcessInstanceDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author wql
 * @describe 故障单列表
 * @date 2023/11/6 11:31
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FaultOrderVO extends ProcessInstanceDTO {
    /**
     * 故障单编号
     */
    private String code;
    /**
     * 故障单名称
     */
    private String name;
    /**
     * 故障对象名称
     */
    private String objName;
    /**
     * 网格id
     */
    private Long areaId;
    /**
     * 故障类型
     */
    private String faultType;
    /**
     * 故障来源
     */
    private String source;
    /**
     * 紧急程度
     */
    private String urgencyLevel;
    /**
     * 处理结果
     */
    private String result;

    /**
     * 确认时间
     */
    private LocalDateTime confirmTime;

    /**
     * 完成时间
     */
    private LocalDateTime completeTime;

    /**
     * 耗时(分钟)
     */
    private Long timeConsuming;
}
