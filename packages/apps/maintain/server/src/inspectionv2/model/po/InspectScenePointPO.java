package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author wql
 * @describe 现场点
 * @date 2023/6/5 15:47
 */
@Data
@TableName("inspect_scene_point")
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InspectScenePointPO extends BasicEnhancedTenantEntity<Long> {
    /**
     * 现场点id
     */
    private Long id;
    /**
     * 编码
     */
    private String codeNum;
    /**
     * 名称
     */
    private String name;
    /**
     * 现场点类型id
     */
    private Long typeId;
    /**
     * 现场点类型名称
     */
    private String typeName;
    /**
     * 管理部门id
     */
    private Long depId;
    /**
     * 管理部门名称
     */
    private String depName;
    /**
     * 所属区域id
     */
    private String areaId;
    /**
     * 详细地址
     */
    private String detailAddress;
    /**
     * 定位
     */
    private String location;
    /**
     * 经纬度
     */
    private String latAndLon;
}
