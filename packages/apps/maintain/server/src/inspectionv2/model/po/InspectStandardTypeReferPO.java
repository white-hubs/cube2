package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.Deletable;
import com.guhecloud.platform.database.interceptor.DeletedTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * <p>
 * 巡检标准与巡检对象关联，类型包括：设备、挡位类型、等
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
@TableName("inspect_standard_type_refer")
public class InspectStandardTypeReferPO implements Deletable<Long> {

    private Long id;

    /**
     * 标准ID
     */
    private Long standardId;

    /**
     * 关联类别：asset资产 gear挡位 device设备
     */
    private String targetCategory;

    /**
     * 可能为资产、挡位、现场点的具体类型ID
     */
    private String targetType;

    private String defaultStandard;//是否是默认标准

    /**
     * 是否已删除
     */
    @TableField(typeHandler = DeletedTypeHandler.class)
    private Boolean deleted;

}
