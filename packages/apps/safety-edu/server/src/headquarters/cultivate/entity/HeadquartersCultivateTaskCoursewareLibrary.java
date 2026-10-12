package com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 总部培训任务关联的课件管理
 *
 * @author lanchun
 * @date 2024/4/10 14:15
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class HeadquartersCultivateTaskCoursewareLibrary extends BasicEnhancedTenantEntity<Long> {
    private Long headquartersCultivateTaskId;
    private Long id;
    private String fileName;
    private String fileSize;
    private String fileIds;
    /**
     * 时长
     */
    private Integer duration;
}