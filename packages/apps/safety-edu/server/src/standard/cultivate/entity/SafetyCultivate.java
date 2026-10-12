package com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author wql
 * @describe 二维码中转页安全培训
 * @date 2024/3/19 19:30
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SafetyCultivate extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 培训主题
     */
    private String themeName;
    /**
     * 实体id
     */
    private Long objId;
    /**
     * 培训类型
     */
    private String cultivateType;
    /**
     * 培训人数
     */
    private Integer personNum;
    /**
     * 培训日期
     */
    private LocalDateTime cultivateDate;
    /**
     * 培训时长(分钟)
     */
    private Integer cultivateDuration;
    /**
     * 现场照片
     */
    private String photo;
}
