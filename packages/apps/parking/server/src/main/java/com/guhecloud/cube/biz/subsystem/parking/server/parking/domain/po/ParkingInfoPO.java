package com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.enumeration.ParkingSourceEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 车场信息
 *
 * @author lanchun
 * @date 2022/10/13 13:40
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("parking_info")
public class ParkingInfoPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 车场编号
     */
    private String code;

    /**
     * 车场名称
     */
    private String name;

    /**
     * 模型区域
     */
    private String modelArea;

    /**
     * 车位总数
     */
    private Integer totalNum;

    /**
     * 车位使用总数
     */
    private Integer totalInNum;

    /**
     * 剩余总数
     */
    private Integer totalSpaceNum;

    /**
     * 父级车场编号
     */
    private String parentCode;

    /**
     * 车场来源
     */
    private ParkingSourceEnum source;

    /**
     * 所在区域
     */
    private String areaId;

    /**
     * 车场地址
     */
    private String address;

    /**
     * 车场备注
     */
    private String remarks;
}
