package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectTargetCategoryEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 巡检计划关联关系
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("inspect_plan_relation")
public class PlanRelationPO extends BasicEnhancedTenantEntity<Long> {

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
     * 巡检对象编码
     */
    private String targetCode;

    /**
     * 巡检对象名称
     */
    private String targetName;

    /**
     * 巡检标准
     */
    private Long standardId;

    //    /**
    //     * 巡检标准名称
    //     */
    //    private Long standardName;
    //
    /**
     * 巡检项
     */
    private Long itemId;
    //    private String itemName;
    //    private String itemContent;
    //    /**
    //     * 巡检项的结果
    //     */
    //    private Long resultId;
    //    private String resultVo;
    /**
     * 结果id
     */
    private String resultCode;

    /**
     * 是否原始巡检项(自己新增的就不是原始项)
     */
    private Boolean isRawItem;
    /**
     * 经纬度
     */
    private String latAndLon;
    /**
     * 经纬度地址
     */
    private String location;
    /**
     * 主对象id(有值表示是被关联的对象)
     */
    private Long targetParentId;
    /**
     * 巡检对象具体地址
     */
    private String targetAddress;

    /**
     * 区域ID
     */
    private String targetAreaId;

    /**
     * 区域名
     */
    private String targetAreaName;

    //    public InspectResultVO getResultVo(){
    //        if(StringUtils.isNotBlank(resultVo)){
    //            return JSON.parseObject(resultVo, InspectResultVO.class);
    //        }
    //        return null;
    //    }
}
