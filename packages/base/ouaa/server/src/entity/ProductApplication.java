package com.guhecloud.cube.biz.ouaa.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.guhecloud.platform.database.entity.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 产品（系统）应用关联数据模型
 *
 * @author Woody
 * @date 2021/7/16
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ProductApplication implements Entity<Long> {
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 产品主键
     */
    private Long productId;

    /**
     * 应用主键
     */
    private Long applicationId;
}
