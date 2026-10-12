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
 * 档位安全生产分值修改记录
 * </p>
 *
 * @author benhuabao
 * @since 2021-12-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("stall_safety_mark_logs")
public class StallSafetyMarkLogs implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;


    /**
     * 档位ID
     */
    private String stallId;

    /**
     * 调整分数（加|减）
     */
    private Integer point;

    /**
     * 备注
     */
    private String remark;

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
     * 删除标志（0存在，1已删除）
     */
    @TableField(fill = FieldFill.INSERT)
    private Boolean deleted=false;


}
