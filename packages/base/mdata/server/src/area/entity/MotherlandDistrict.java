package com.guhecloud.cube.biz.mdata.server.area.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 国家区域表实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MotherlandDistrict extends BasicEnhancedEntity<Long> {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 父级地区id
     */
    private Long pid;

    /**
     * 区划编码
     */
    private String code;

    /**
     * 区划名称
     */
    private String name;

    /**
     * 级别: 1:省/自治区/直辖市, 2:市级, 3:县级, 4:街道
     */
    private Integer level;

    /**
     * 经纬度（格式: 经度,纬度）
     */
    private String center;

    /**
     * 城市编码
     */
    private String cityCode;

    /**
     * 状态: 0=弃用, 1=使用
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;
}
