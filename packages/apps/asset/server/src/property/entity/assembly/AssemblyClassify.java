package com.guhecloud.cube.srv.gen.property.management.server.property.entity.assembly;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 通用分类;
 *
 * @author :lijie
 * @date : 2023-3-2
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("assembly_classify")
public class AssemblyClassify extends BasicEnhancedTenantEntity<Long> {
    /**
     * 组件
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 类型（资产分类：property；）
     */
    private String type;
    /**
     * 名称
     */
    private String name;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 父级id
     */
    private Long parentId;
    /**
     * 编码
     */
    private String code;
    private String status;
}