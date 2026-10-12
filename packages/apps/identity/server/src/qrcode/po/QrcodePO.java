package com.guhecloud.cube.srv.gen.identity.server.qrcode.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author bq
 * @date 2022/7/20
 * @description 二维码
 */
@Data
@TableName("qrcode")
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class QrcodePO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 编号
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 实体类型
     */
    private String entityType;

    /**
     * 实体ID
     */
    private Long entityId;

    /**
     * 实体名称
     */
    private String entityName;

    /**
     * 样式ID
     */
    private Long styleId;

    /**
     * 样式配置Json
     */
    private String configJson;

}