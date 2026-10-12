package com.guhecloud.cube.srv.gen.securitymanagement.server.surgicaldemonstration.entity;

import com.guhecloud.cube.srv.gen.securitymanagement.server.surgicaldemonstration.enumeration.CourseClassificationEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SurgicalDemonstration extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private String name;
    private String operatingRoom;
    private String chiefSurgeon;
    private LocalDateTime dateOperation;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer videoDuration;
    private CourseClassificationEnum courseClassification;
    private String subject;
    private String modality;
    private String playbackCount;
}
