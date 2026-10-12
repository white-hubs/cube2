package com.guhecloud.cube.srv.gen.safety.education.server.common.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 主题管理
 *
 * @author lanchun
 * @date 2023/4/18 10:21
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value = "theme")
public class ThemePO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 主题描述
     */
    private String remark;
}
