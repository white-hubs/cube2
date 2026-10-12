package com.guhecloud.cube.srv.electron.guide.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 点位分类
 *
 * @author lanchun
 * @date 2024/9/3 11:41
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@TableName("point_type")
public class PointType extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 分类编号
     */
    private String code;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 文件地址
     */
    private String filePath;
}
