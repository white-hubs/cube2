package com.guhecloud.cube.base.file.server.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class FileRef extends BasicEnhancedEntity<Long> {

    /**
     * 主键
     */
    private Long id;

    /**
     * 业务主键
     */
    private Long businessKey;

    /**
     * 文件主键
     */
    private Long fileId;

    private String tag;
}
