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
 * 安全月报
 * </p>
 *
 * @author zwb
 * @since 2021-06-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("safety_report")
public class SafetyReport implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String id;

    /**
     * 项目ID
     */
    private String projectId;

    /**
     * 公司ID
     */
    private String firmId;

    /**
     * 公司名称
     */
    private String firmName;

    /**
     * 标题
     */
    private String title;

    /**
     * 填报状态
     */
    private String status;

    /**
     * 填报人ID
     */
    private String userId;

    /**
     * 填报人用户名
     */
    private String username;

    /**
     * 填报人昵称
     */
    private String nickname;

    /**
     * 填报时间
     */
    private LocalDateTime filledTime;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 所属汇总表路径
     */
    private String hqExcelPath;

    /**
     * 删除标识
     */
    @TableField(fill = FieldFill.INSERT)
    private Boolean deleted;


}
