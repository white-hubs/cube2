package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 巡检记录手动修复
 */
@Data
@TableName("inspect_record_repair")
@EqualsAndHashCode(callSuper = true)
public class InspectRecordRepairPO extends BasicEnhancedTenantEntity<Long> {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 巡检记录ID，关联巡检记录主表
     */
    private Long recordId;

    /**
     * 处理人ID，表示执行修复操作的用户编号
     */
    private Long userId;

    /**
     * 处理人名称，记录操作用户的姓名或登录名
     */
    private String userName;

    /**
     * 修复状态，例如：已修复、处理中、无法修复等
     */
    private String repairStatus;

    /**
     * 处理内容，描述具体的修复操作或处理过程
     */
    private String content;

    /**
     * 备注，补充说明信息
     */
    private String remark;

    /**
     * 业务类型，标识该记录所属的业务场景
     */
    private String businessType;
}
