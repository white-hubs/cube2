package com.guhecloud.cube.base.iot.product.model.v3.param;

import com.guhecloud.cube.base.iot.product.model.v3.dto.event.EventConfig;
import lombok.Data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 事件保存参数
 */
@Data
public class EventSaveParam {

    /**
     * 产品ID
     */
    @NotNull(message = "产品ID不能为空")
    private Long prodId;

    /**
     * 事件名称
     */
    @NotBlank(message = "事件名称不能为空")
    @Size(max = 100, message = "事件名称长度不能超过100")
    private String name;

    /**
     * 事件编码,产品下唯一
     */
    @NotBlank(message = "事件编码不能为空")
    @Size(max = 50, message = "事件编码长度不能超过50")
    private String eventCode;

    /**
     * 事件类别ID
     */
    private Long eventCategoryId;

    /**
     * 事件配置
     * 包含触发类型、关联属性、触发规则等
     * 系统内部使用实体类，对外暴露时会转换为JSON字符串
     */
    @Valid
    private EventConfig config;

    /**
     * 描述
     */
    @Size(max = 500, message = "描述长度不能超过500")
    private String remark;

    /**
     * 状态: 1启用 0禁用
     */
    private Integer status =1;

    /**
     * 排序号
     */
    private Integer sortOrder = 0;
}
