package com.guhecloud.cube.deliver.project.merchant.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 环境模型配置
 * </p>
 *
 * @author bq
 * @since 2020-12-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("envmodel_config")
public class EnvmodelConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 项目ID
     */
    private String projectId;

    private String x;

    private String y;

    private String z;

    /**
     * 旋转角度
     */
    private String angle;


}
