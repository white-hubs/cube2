package com.guhecloud.cube.deliver.project.merchant.entity.storeprofile;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author wangliang
 * @since 2022-01-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("wholesaler_protocol")
public class WholesalerProtocol implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String projectId;

    private String name;

    private String code;

    private String templateId;

    private String templateName;

    private String templateType;

    private String remark;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;

    private Integer processingStatus;

    private String status;

    private String xxlTaskIds;

    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT)
    private Boolean deleted=false;


}
