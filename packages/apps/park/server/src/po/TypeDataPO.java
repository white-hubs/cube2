package com.guhecloud.cube.srv.gen.park.management.server.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 类型数据
 * </p>
 *
 * @author zwb
 * @since 2022-09-16
 */

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("type_data")
public class TypeDataPO extends BasicEnhancedTenantEntity<Long> {
    /**
     * ID
     */
    private Long id;

    /**
     * 类别标识
     */
    private String tag;

    /**
     * 名称
     */
    private String name;

}
