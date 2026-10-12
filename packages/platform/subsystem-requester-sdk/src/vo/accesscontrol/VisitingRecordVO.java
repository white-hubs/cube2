package com.guhecloud.subsystem.requester.sdk.vo.accesscontrol;

import com.guhecloud.platform.base.util.StringUtils;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 访客记录
 */
@Data
@Builder
public class VisitingRecordVO {
    private String id;//用于存放第三方的ID
    private String receptionistName;//被访人
    private String visitorName;//来访人
    private String phoneNo;//来访人电话
    private String plateNo;//来访车牌号
    private LocalDateTime visitStartTime;//来访时间
    private LocalDateTime visitEndTime;//离开时间
    private String visitPurpose;//来访事由
    private String cardNo;//卡号
    //5：超期自动签离 6：已签离 7：超期未签离 8：已到达
    private String visitorStatus;//来访状态
    private String visitorStatusName;//来访状态名称

    public String getVisitorStatusName() {
        return VisitingStatus.getDisplayNameByValue(visitorStatus);
    }

    @Getter
    public enum VisitingStatus {
        OVERTIME_AUTO_LEAVE("5", "超期自动签离"),
        LEAVE("6", "已签离"),
        OVERTIME_NOT_LEAVE("7", "超期未签离"),
        ARRIVED("8", "已到达");

        private final String value;

        private final String displayName;

        VisitingStatus(String value, String displayName) {
            this.value = value;
            this.displayName = displayName;
        }

        public static String getDisplayNameByValue(String value) {
            for (VisitingStatus status : VisitingStatus.values()) {
                if (StringUtils.isNotEmpty(value) && status.getValue().equals(value)) {
                    return status.getDisplayName();
                }
            }
            return null; // 如果没有匹配的枚举项，则返回 null 或其他适当的默认值
        }
    }
}
