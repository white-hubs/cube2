package com.guhecloud.cube.biz.ouaa.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.guhecloud.cube.biz.ouaa.metadata.enumeration.TenantCategory;
import com.guhecloud.platform.database.annotation.Unique;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * 租户数据模型
 *
 * @author Woody
 * @date 2021/6/28
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Tenant extends BasicEnhancedEntity<Long> {
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 租住编号
     */
    private Integer code;

    /**
     * 租户类型
     */
    private TenantCategory category;

    /**
     * 租户名称
     */
    @Unique(message = "租户名称已存在")
    private String name;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 所在地
     */
    private String location;

    /**
     * 楼栋数
     */
    private Integer building;

    /**
     * 占地面积
     */
    private BigDecimal areaCovered;

    /**
     * 企业logo
     */
    private Long logoId;

    /**
     * 宣传图
     */
    private String file;

    /**
     * 租户备注
     */
    private String remark;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 占地总建筑面积
     */
    private BigDecimal floorArea;

    /**
     * 服务热线
     */
    private String serviceHotLine;

    /**
     * 经纬度信息
     */
    private String lntAndLat;

    /**
     * 经纬度范围
     */
    private String lntAndLatScope;

    /**
     * 经纬度范围名称
     */
    private String lntAndLatScopeName;

    /**
     * 投诉电话
     */
    private String complaintHotLine;
}
