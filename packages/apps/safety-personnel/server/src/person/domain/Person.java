package com.guhecloud.cube.srv.gen.safety.personnel.server.person.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 安全人员台账。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sp_person")
public class Person extends BasicEnhancedTenantEntity<Long> {

    /**
     * 安全人员ID。
     */
    private Long id;

    /**
     * OUAA用户ID。
     */
    private Long userId;

    /**
     * 用户姓名快照。
     */
    private String userName;

    /**
     * 手机号快照。
     */
    private String phone;

    /**
     * OUAA公司名称快照。
     */
    private String companyName;

    /**
     * OUAA部门名称快照。
     */
    private String deptName;

    /**
     * OUAA岗位名称快照。
     */
    private String postName;

    /**
     * 人员状态，ACTIVE/DISABLED/LEFT。
     */
    private String status;

    /**
     * 纳入安全人员库时间。
     */
    private LocalDateTime linkedAt;
}
