package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectTargetCategoryEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * <p>
 * 巡检目标象包括：设备、挡位、等
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
@TableName("inspect_plan_target_refer")
public class InspectPlanTargetReferPO {

    private Long id;

    /**
     * 计划ID
     */
    private Long planId;

    /**
     * 巡检对象类型
     */
    private InspectTargetCategoryEnum targetCategory;

    /**
     * 巡检对象具体类型
     */
    private String targetType;

    /**
     * 巡检对象ID
     */
    private Long targetId;

    /**
     * 巡检对象名称
     */
    private String targetName;

    /**
     * 巡检标准
     */
    private String standardIds;
}
