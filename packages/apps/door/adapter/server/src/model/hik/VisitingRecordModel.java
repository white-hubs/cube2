package com.guhecloud.cube.srv.gen.adapter.access.control.server.model.hik;

import lombok.Data;

/**
 * 访客记录
 */
@Data
public class VisitingRecordModel {
    private String orderId;//用于存放第三方的ID
    private String receptionistName;//被访人
    private String visitorName;//来访人
    private String phoneNo;//来访人电话
    private String plateNo;//来访车牌号
    private String visitStartTime;//来访时间
    private String visitEndTime;//离开时间
    private String visitPurpose;//来访事由
    private String cardNo;//卡号
    //5：超期自动签离 6：已签离 7：超期未签离 8：已到达
    private String visitorStatus;//来访状态
}
