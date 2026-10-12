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
 * 档位安全生产值
 * </p>
 *
 * @author zwb
 * @since 2021-12-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("stall_safety_mark")
public class StallSafetyMark implements Serializable {

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
     * 年份
     */
    private Integer year;

    /**
     * 分数
     */
    private Integer score;

    /**
     * 扣分巡检项数
     */
    private Integer inspTotal;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


}
