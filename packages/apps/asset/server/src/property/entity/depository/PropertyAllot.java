package com.guhecloud.cube.srv.gen.property.management.server.property.entity.depository;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 资产派发记录;
 *
 * @author : lijie
 * @date : 2023-3-10
 */
@Data
@TableName("property_allot")
public class PropertyAllot extends BasicEnhancedTenantEntity<Long> {
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 移交部门
     */
    private Long overDepId;
    /**
     * 移交部门名称
     */
    private String overDepName;
    /**
     * 接收部门id
     */
    private Long receiveDepId;
    /**
     * 接收部门名称
     */
    private String receiveDepName;
    /**
     * 接收人
     */
    private Long receiveUserId;
    /**
     * 接收日期
     */
    private LocalDateTime receiveTime;
    /**
     * 区域id
     */
    private String areaId;
    /**
     * 出库后的存放地点
     */
    private String place;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 附件
     */
    private String files;
    /**
     * 出库单号
     */
    private String allotNumbers;
    /**
     * 状态
     */
    private String status;
    /**
     * 操作类型
     */
    private String type;
    private String processInstanceId;
}
