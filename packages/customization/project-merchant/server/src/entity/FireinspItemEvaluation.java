package com.guhecloud.cube.deliver.project.merchant.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 巡检项评价配置
 * </p>
 *
 * @author zwb
 * @since 2021-12-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("fireinsp_item_evaluation")
public class FireinspItemEvaluation implements Serializable {

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
     * 编号
     */
    private String code;

    /**
     * 标准ID
     */
    private String standardId;

    /**
     * 标准名
     */
    private String standardName;

    /**
     * 项ID
     */
    private String itemId;

    /**
     * 项名称
     */
    private String itemName;

    /**
     * 项结果编号(和项ID一样，不能修改)
     */
    private String itemResultCode;

    /**
     * 项结果名
     */
    private String itemResultName;

    /**
     * 项结果扣分
     */
    private Integer itemResultMark;

    /**
     * 启停状态
     */
    private Boolean enabled;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
