package com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 检测标准实体类。
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("detection_standard")
public class Standard extends BasicEnhancedEntity<Long> {

    private Long id;

    /**
     * 检测依据id。
     */
    private Long basicId;

    /**
     * 标准名称。
     */
    private String name;

}