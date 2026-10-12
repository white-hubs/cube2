package com.guhecloud.cube.deliver.project.merchant.entity.store;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 档位评价记录表
 * </p>
 *
 * @author benhuabao
 * @since 2021-12-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("stall_evaluation_record")
public class StallEvaluationRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 项目ID
     */
    private String projectId;

    private String code;

    private String mobile;
    /**
     * 档位ID
     */
    private String stallId;

    /**
     * 评价人ID
     */
    private String userId;

    /**
     * 评价人名
     */
    private String username;

    /**
     * 评价人昵称
     */
    private String nickname;

    /**
     * 商品质量评分
     */
    private Integer zScore;

    /**
     * 诚实守信评分
     */
    private Integer sScore;

    /**
     * 服务态度评分
     */
    private Integer tScore;

    /**
     * 环境卫生评分
     */
    private Integer wScore;

    /**
     * 综合评分
     */
    private Float score;

    /**
     * 诚信经营值
     */
    private Integer honestyScore;

    /**
     * 描述
     */
    private String descr;

    /**
     * 是否有图
     */
    private Boolean hasFile;

    /**
     * 评价标签
     */
    private String tags;

    /**
     * 审核状态（0:未审核，1:已通过，2:未通过）
     */
    private int examineStatus;

    /**
     * 审核备注
     */
    private String examineRemark;

    /**
     * 审核时间
     */
    private LocalDateTime examineTime;

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

    /**
     * 逻辑删除
     */
    @TableField(fill = FieldFill.INSERT)
    private Boolean deleted;


}
