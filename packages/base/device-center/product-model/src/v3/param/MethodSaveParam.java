package com.guhecloud.cube.base.iot.product.model.v3.param;

import com.guhecloud.cube.base.iot.product.model.v3.model.params.Params;
import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 方法保存参数
 */
@Data
public class MethodSaveParam {

    /**
     * 产品ID
     */
    @NotNull(message = "产品ID不能为空")
    private Long prodId;

    /**
     * 方法名称
     */
    @NotBlank(message = "方法名称不能为空")
    @Size(max = 100, message = "方法名称长度不能超过100")
    private String name;

    /**
     * 方法编码,产品下唯一
     */
    @NotBlank(message = "方法编码不能为空")
    @Size(max = 50, message = "方法编码长度不能超过50")
    private String methodCode;

    /**
     * 调用类型: sync同步 async异步
     */
    @NotBlank(message = "调用类型不能为空")
    private String callType;

    /**
     * 输入参数定义
     * 接收 JSON 字符串并自动反序列化为 MethodParams 对象
     */
//    @JsonDeserialize(using = MethodParamsDeserializer.class)
    private Params inputParams;

    /**
     * 输出参数定义
     * 接收 JSON 字符串并自动反序列化为 MethodParams 对象
     */
//    @JsonDeserialize(using = MethodParamsDeserializer.class)
    private Params outputParams;

    /**
     * 超时时间(秒)
     */
    @Min(value = 1, message = "超时时间必须大于0")
    private Integer timeoutSeconds;

    /**
     * 重试次数
     */
    @Min(value = 0, message = "重试次数不能为负数")
    private Integer retryCount;

    /**
     * 重试间隔(秒)
     */
    @Min(value = 0, message = "重试间隔不能为负数")
    private Integer retryInterval;

    /**
     * 方法描述
     */
    @Size(max = 500, message = "方法描述长度不能超过500")
    private String remark;

    /**
     * 状态: 1启用 0禁用
     */
    private Integer status = 1;

    /**
     * 排序号
     */
    private Integer sortOrder = 0;
}
