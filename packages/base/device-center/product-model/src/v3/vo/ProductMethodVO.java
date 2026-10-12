package com.guhecloud.cube.base.iot.product.model.v3.vo;

import com.guhecloud.cube.base.iot.product.model.v3.model.params.Params;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 产品方法视图对象
 */
@Data
public class ProductMethodVO {

    /**
     * 方法ID
     */
    private Long id;

    /**
     * 产品ID
     */
    private Long prodId;

    /**
     * 方法名称
     */
    private String name;

    /**
     * 方法编码
     */
    private String methodCode;

    /**
     * 调用类型
     */
    private String callType;

    /**
     * 输入参数定义
     * 内部使用 MethodParams 对象，API 返回时自动序列化为 JSON 字符串
     */
    private Params inputParams;

    /**
     * 输出参数定义
     * 内部使用 MethodParams 对象，API 返回时自动序列化为 JSON 字符串
     */
    private Params outputParams;

    /**
     * 超时时间(秒)
     */
    private Integer timeoutSeconds;

    /**
     * 重试次数
     */
    private Integer retryCount;

    /**
     * 重试间隔
     */
    private Integer retryInterval;

    /**
     * 方法描述
     */
    private String remark;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 排序号
     */
    private Integer sortOrder;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
