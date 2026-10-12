package com.guhecloud.cube.base.iot.product.model.v3.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.guhecloud.cube.base.iot.product.model.v3.model.params.Params;
import com.guhecloud.platform.database.entity.BasicTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 产品方法表v3版本(新增功能)
 * 对应表: iot_md_product_method_v3
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "iot_md_product_method_v3", autoResultMap = true)
@Data
public class MdProductMethod extends BasicTenantEntity<Long> {

    /**
     * 方法ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 产品ID
     */
    @TableField(value = "prod_id")
    private Long prodId;

    /**
     * 方法名称
     */
    @TableField(value = "`name`")
    private String name;

    /**
     * 方法编码,产品下唯一
     */
    @TableField(value = "method_code")
    private String methodCode;

    /**
     * 调用类型: sync同步 async异步
     */
    @TableField(value = "call_type")
    private String callType;

    /**
     * 输入参数定义
     * 内部使用 MethodParams 对象，存储时自动转换为 JSON 字符串
     */
    @TableField(value = "input_params", typeHandler = JacksonTypeHandler.class)
    private Params inputParams;

    /**
     * 输出参数定义
     * 内部使用 MethodParams 对象，存储时自动转换为 JSON 字符串
     */
    @TableField(value = "output_params", typeHandler = JacksonTypeHandler.class)
    private Params outputParams;

    /**
     * 超时时间(秒)
     */
    @TableField(value = "timeout_seconds")
    private Integer timeoutSeconds;

    /**
     * 重试次数
     */
    @TableField(value = "retry_count")
    private Integer retryCount;

    /**
     * 重试间隔(秒)
     */
    @TableField(value = "retry_interval")
    private Integer retryInterval;

    /**
     * 方法描述
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 状态: 1启用 0禁用
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 排序号
     */
    @TableField(value = "sort_order")
    private Integer sortOrder;
}
