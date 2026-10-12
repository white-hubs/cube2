package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po;

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
 * 巡检结果  实际为巡检结果项  冗余了名称
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
@TableName("inspect_result")
public class InspectResultPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 编号
     */
    private String code;

    /**
     * 巡检结果名称
     */
    private String name;

    /**
     * 选项名称
     */
    private String itemName;

    /**
     * 选项值
     */
    private String itemValue;

    /**
     * 是否异常点
     */
    private Boolean errorFlag;

    /**
     * 1 启用 0禁用
     */
    private Boolean enable;

}
