package com.guhecloud.cube.iot.mgt.dcm.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.guhecloud.cube.iot.mgt.things.vo.EventConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 事件模型
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class ProdEvent {
    /** v2添加字段;事件编码 */
    private String code;

    /** 事件名称 */
    private String name;

    /** 事件类别 */
    private String category;

    /**
     * 事件配置<br/>
     * {@link EventConfig}
     */
    private EventConfig config;

    /** 描述 */
    private String remark = "";

    /** 状态: 1 启用 0禁用 */
    private int status = 1;
}
