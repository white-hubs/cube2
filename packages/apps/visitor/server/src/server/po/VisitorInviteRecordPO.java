package com.guhecloud.cube.srv.gen.visitor.management.server.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/2/24 16:48
 * @Description: 访客邀约记录实体
 */

@Data
@TableName("visitor_invite_record")
@EqualsAndHashCode(callSuper = true)
public class VisitorInviteRecordPO extends BasicEnhancedTenantEntity<Long> {

    /**
     * 主键
     */
    private Long id;

    /**
     * 访客姓名
     */
    private String visitorName;

    /**
     * 访客身份证号
     */
    private String visitorIdCard;

    /**
     * 访客电话
     */
    private String visitorPhone;

    /**
     * 访问区域
     */
    private String areaId;

    /**
     * 访问日期
     */
    private String accessDate;

    /**
     * 访问时段
     */
    private String timeStr;

    /**
     * 访问事由
     */
    private String reason;

    /**
     * 车牌号码
     */
    private String plateNumber;

    /**
     * 随访人员
     */
    private String followVisitors;

    /**
     * 状态
     */
    private String status;

    /**
     * 流程实例id
     */
    private String processInsId;

    /**
     * 预约二维码
     */
    private String barCode;

    /**
     * 预约记录
     */
    private Long reservationRecordId;
}
