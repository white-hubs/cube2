package com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Appet
 * @Description 培训课件管理
 * @date 2023-04-18 15:08:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CultivateTaskCoursewareLibrary extends BasicEnhancedTenantEntity<Long> {
    private Long cultivateTaskId;
    private Long id;
    private String fileName;
    private String fileSize;
    private String fileIds;
    /**
     * 时长
     */
    private Integer duration;
}