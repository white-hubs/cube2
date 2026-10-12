package com.guhecloud.cube.deliver.project.merchant.entity.store;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 档位评价维度权重占比
 * </p>
 *
 * @author bq
 * @since 2022-06-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("stall_evaluation_ratio")
public class StallEvaluationRatio implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 项目ID
     */
    private String projectId;

    /**
     * 商品质量权重占比
     */
    @NotNull(message = "商品质量权重占比不能为空")
    @Min(value = 0, message = "商品质量权重占比不能小于0")
    @Max(value = 1, message = "商品质量权重占比不能大于1")
    private Float zRatio;

    /**
     * 诚实守信权重占比
     */
    @NotNull(message = "诚实守信权重占比不能为空")
    @Min(value = 0, message = "诚实守信权重占比不能小于0")
    @Max(value = 1, message = "诚实守信权重占比不能大于1")
    private Float sRatio;

    /**
     * 服务态度权重占比
     */
    @NotNull(message = "服务态度权重占比不能为空")
    @Min(value = 0, message = "服务态度权重占比不能小于0")
    @Max(value = 1, message = "服务态度权重占比不能大于1")
    private Float tRatio;

    /**
     * 环境卫生权重占比
     */
    @NotNull(message = "环境卫生权重占比不能为空")
    @Min(value = 0, message = "环境卫生权重占比不能小于0")
    @Max(value = 1, message = "环境卫生权重占比不能大于1")
    private Float wRatio;


}
