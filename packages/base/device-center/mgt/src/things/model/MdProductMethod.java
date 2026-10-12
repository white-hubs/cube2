package com.guhecloud.cube.iot.mgt.things.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 产品方法表
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "iot_md_product_method")
@Data
public class MdProductMethod extends BasicEnhancedTenantEntity<Long> {

    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 产品ID
     */
    @TableField(value = "prod_id")
    private Long prodId;

    /**
     * 方法名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 方法编码（产品内唯一，用于跨环境映射）
     */
    @TableField(value = "method_code")
    private String methodCode;

    /**
     * 方法描述
     */
    @TableField(value = "description")
    private String description;

    /**
     * 方法类型：sync-同步方法、async-异步方法
     */
    @TableField(value = "method_type")
    private String methodType;

    /**
     * 输入参数JSON
     */
    @TableField(value = "input_params")
    private String inputParams;

    /**
     * 输出参数JSON
     */
    @TableField(value = "output_params")
    private String outputParams;

    /**
     * 执行超时时间（毫秒）
     */
    @TableField(value = "timeout_ms")
    private Integer timeoutMs;

    /**
     * 是否支持重试：0-否 1-是
     */
    @TableField(value = "retryable")
    private Integer retryable;

    /**
     * 最大重试次数
     */
    @TableField(value = "max_retries")
    private Integer maxRetries;

    /**
     * 状态：0-启用 1-禁用
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 方法扩展配置（JSON字符串）
     */
    @TableField(value = "extend_config")
    private String extendConfig;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;
}
