package com.guhecloud.cube.srv.electron.guide.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantTreeEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-08  11:37
 * @Description:
 * @Version: 1.0
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@TableName("guide_area_manage")
public class GuideAreaManage extends BasicEnhancedTenantTreeEntity<Long> {

    private Long id;
    /**
     * 区域名称
     */
    private String name;
    /**
     * 区域坐标
     */
    private String coordinate;
    /**
     * 图层
     */
    private String zoom;
    /**
     * 状态
     */
    private String state;
    /**
     * 序号
     */
    private Integer sort;
}
