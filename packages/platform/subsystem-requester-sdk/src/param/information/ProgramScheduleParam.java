package com.guhecloud.subsystem.requester.sdk.param.information;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 获取节目单列表 请求入参
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProgramScheduleParam {

    //开始时间
    private String startTime;

    //结束时间
    private String endTime;

    //终端Id集合
    private List<String> clientIds;
}
