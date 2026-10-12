package com.guhecloud.cube.srv.gen.securitymanagement.server.information.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 节目详情 实体类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("program")
public class ProgramEntity extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 第三方节目id
     */
    private String thirdProgramId;
    /**
     * 节目名称
     */
    private String programName;
    /**
     * 节目分类名称
     */
    private String categoryName;
}
