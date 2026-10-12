package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("disinfection_task_check_in_record")
public class DisinfectionTaskCheckIn extends BasicEnhancedTenantEntity<Long> {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 任务ID
     */
    private Long taskId;
    private String taskName;
    /**
     * 项目ID
     */
    private Long projectId;
    /**
     * 打卡人名称
     */
    private String checkUserName;
    /**
     * 经度
     */
    private BigDecimal longitude;
    ;
    /**
     * 纬度
     */
    private BigDecimal latitude;
    private String locationName;
    private String files;

}
