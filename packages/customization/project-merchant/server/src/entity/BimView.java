package com.guhecloud.cube.deliver.project.merchant.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author bq
 * @since 2020-11-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("bim_view")
public class BimView implements Serializable {

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
     * 视觉名称
     */
    private String name;

    /**
     * 楼层名称
     */
    private String modelLevel;

    /**
     * 专业名称
     */
    private String specialty;

    /**
     * 构建编码
     */
    private String ecode;

    /**
     * 视觉配置
     */
    private String viewconfig;

}
