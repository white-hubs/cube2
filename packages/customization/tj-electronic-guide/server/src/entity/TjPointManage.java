package com.guhecloud.cube.srv.tj.electronic.guide.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-08  09:56
 * @Description:
 * @Version: 1.0
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@TableName("tj_point_manage")
public class TjPointManage extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 点位名称
     */
    private String name;
    /**
     * 区域
     */
    private Long areaId;
    /**
     * 点位地址
     */
    private String address;
    /**
     * 点位坐标
     */
    private String coordinate;
    /**
     * 点位描述
     */
    private String remarks;
    /**
     * 序号
     */
    private Integer level;
    /**
     * 状态
     */
    private String state;
}
