package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe 故障单明细
 * @date 2023/4/17 10:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class TroubleListDetailVO {
    /**
     * 提交时间
     */
    private String submitTime;
    /**
     * 故障单名称
     */
    private String name;
    /**
     * 故障设备
     */
    private String equipment;
    /**
     * 故障来源
     */
    private String source;
    /**
     * 提交人
     */
    private String submitStaff;
    /**
     * 状态
     */
    private String state;
    /**
     * 处理结果
     */
    private String result;
}
