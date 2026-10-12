package com.guhecloud.cube.biz.ouaa.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.guhecloud.platform.database.annotation.Unique;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 客户端数据模型
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
public class Client extends BasicEnhancedEntity<Long> {
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 客户端编号
     */
    @Unique(message = "客户端编号已存在")
    private String code;

    /**
     * 客户端名称
     */
    @Unique(message = "客户端名称已存在")
    private String name;

    /**
     * 客户端备注
     */
    private String remark;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 租户
     */
    private String tenant;

    /**
     * 访问端密钥
     */
    private String secret;

    /**
     * 密钥明文
     */
    private String secretPlain;

    /**
     * 资源范围（多个范围之间使用","号隔开）
     */
    private String scopes;

    /**
     * 授权类型（多个类型之间使用","号隔开）
     */
    private String grantTypes;

    /**
     * 访问令牌超时时间（秒）
     */
    private Integer accessTokenTimeout;

    /**
     * 刷新令牌超时时间（秒）
     */
    private Integer refreshTokenTimeout;
}
