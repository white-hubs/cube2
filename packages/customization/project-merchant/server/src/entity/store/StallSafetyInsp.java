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
 * 档位安全生产巡检扣分记录
 * </p>
 *
 * @author zwb
 * @since 2021-12-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("stall_safety_insp")
public class StallSafetyInsp implements Serializable {

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
     * 任务名
     */
    private String taskName;

    /**
     * 记录ID
     */
    private String recordId;

    /**
     * 记录编号
     */
    private String recordCode;

    /**
     * 巡检记录是否存在
     */
    private Integer recordExist;

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
     * 项结果名
     */
    private String itemResultName;

    /**
     * 项结果扣分
     */
    private Integer itemResultMark;

    /**
     * 巡检时间
     */
    private LocalDateTime time;

    /**
     * 巡检人ID
     */
    private String userId;

    /**
     * 巡检人名
     */
    private String username;

    /**
     * 巡检人昵称
     */
    private String nickname;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


}
