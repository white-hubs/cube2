package com.guhecloud.cube.biz.ouaa.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 访问端数据模型
 *
 * @author Woody
 * @date 2021/7/16
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Terminal extends BasicEnhancedEntity<Long> {
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 产品主键
     */
    @Deprecated
    private Long productId;

    /**
     * 访问端编号
     */
    private String code;

    /**
     * 访问端名称
     */
    private String name;

    /**
     * 微信ID
     */
    private String appId;

    /**
     * 微信密钥
     */
    private String appSecret;

    /**
     * 访问端备注
     */
    private String remark;

    /**
     * 是否启用
     */
    private Boolean enabled;
}
