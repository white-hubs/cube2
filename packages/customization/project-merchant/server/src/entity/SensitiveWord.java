package com.guhecloud.cube.deliver.project.merchant.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 敏感词库
 * </p>
 *
 * @author bq
 * @since 2022-06-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sensitive_word")
public class SensitiveWord implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 排序
     */
    @TableField("`order`")
    private Integer order;

    /**
     * 名称
     */
    private String name;

}
