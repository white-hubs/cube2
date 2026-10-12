package com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 检测依据实体类。
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("detection_basic")
public class Basic extends BasicEnhancedEntity<Long> {

    private Long id;

    /**
     * 检测依据名称。
     */
    private String name;

}