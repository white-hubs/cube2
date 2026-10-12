package com.guhecloud.cube.biz.daily.task.migration.po;

import com.guhecloud.platform.database.entity.BasicTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * <p>
 * 巡检标准
 * </p>
 *
 * @author zwb
 * @since 2022-04-26
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
//@TableName("inspect_standard")
public class MigrationInspectStandardPO extends BasicTenantEntity<Long> {

    private Long id;

    /**
     * 标准名称
     */
    private String name;

    private String remark;
    /**
     * 标准编码
     */
    private String code;

    /**
     * 1启用 0禁用
     */
    private Boolean enable;

    private String businessType;

}
