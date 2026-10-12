package com.guhecloud.cube.biz.smartcard.server.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 访客记录
 */
@Data
@Builder
public class VisitingRecord extends BasicEnhancedTenantEntity<String> {
    private String id;
    private String receptionistName;//被访人
    private String visitorName;//来访人
    private String phoneNo;//来访人电话
    private String plateNo;//来访车牌号
    private LocalDateTime visitStartTime;//来访时间
    private LocalDateTime visitEndTime;//离开时间
    private String visitPurpose;//来访事由
    private String cardNo;//卡号
    private String visitorStatus;//来访状态
    private String visitorStatusName;//来访状态名称
}
