package com.guhecloud.cube.deliver.project.merchant.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * bim绑定信息
 * </p>
 *
 * @author gjw
 * @since 2020-10-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("bim_info")
public class BimInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String projectId;

    private String businessType;
    private String businessKey;
    
    /**
     * 模型文件ID
     */
    private String modelFileId;

    /**
     * 楼层ID
     */
    private String modelLevelId;

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
     * 构建ID
     */
    private String objectId;
    
    /**
     * 坐标
     */
    private String x;
    private String y;
    private String z;
    
    /**
     * 模型属性
     */
    private String props;

}
