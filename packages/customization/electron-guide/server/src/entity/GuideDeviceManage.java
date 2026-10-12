package com.guhecloud.cube.srv.electron.guide.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@TableName("guide_device_manage")
public class GuideDeviceManage extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 点位名称
     */
    private String name;

    /**
     * 图标主键
     */
    private String logo;

    /**
     * 标题
     */
    private String title;

    /**
     * 点位坐标
     */
    private String coordinate;

    /**
     * 层级
     */
    private String zoom;
}
