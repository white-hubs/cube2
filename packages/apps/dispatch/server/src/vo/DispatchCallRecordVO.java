package com.guhecloud.cube.biz.dispatch.server.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Appet
 * @Description 通话记录分页
 * @date 2022-07-26 10:08:36
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DispatchCallRecordVO {
    private String callId;
    private String callerNumber;//主叫号码
    private String callerName;//主叫名称
    private String calleeNumber;//被叫号码
    private String calleeName;//被叫名称
    private String callType;//呼叫类型
    private String callTypeName;//呼叫类型名称
    private String groupNumber;//组编号
    private String groupName;//组名称
    private String startTime;//开始时间
    private String endTime;//结束时间
    private String acceptTime;//接听时间
    private String callTime;//拨号时间
    private String callResult;//拨号结果
    private String releaseReason;//通话结束原因
}