package com.guhecloud.cube.srv.gen.safety.education.server.common.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 课件库
 *
 * @author lanchun
 * @date 2023/4/18 10:38
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value = "courseware_library")
public class CoursewareLibraryPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 课件名称
     */
    private String name;

    /**
     * 课件主题主键
     */
    private Long themeId;

    /**
     * 文件主键
     */
    private Long fileId;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件大小
     */
    private Integer fileSize;

    /**
     * 备注
     */
    private String remark;

    /**
     * 时长
     */
    private Integer duration;
}
