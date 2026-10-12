package com.guhecloud.cube.srv.gen.maintain.server.scada.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * <p>
 * 二维组态资源表
 * </p>
 *
 * @author zwb
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("scada_resources")
public class ScadaResourcesPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 类别编号
     */
    private String categoryCode;

    /**
     * 名称
     */
    private String name;

    /**
     * 组态地址
     */
    private String url;

    /**
     * 描述
     */
    private String description;

}
