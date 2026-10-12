package com.guhecloud.cube.biz.dispatch.server.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author Appet
 * @Description 调度呼叫记录分页查询
 * @date 2022-07-26 10:08:36
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class DispatchCallQueryParam extends Paging {

    //设备编号
    private String code;
    //主叫号码
    private String callerNumber;
    //被叫号码
    private String calleeNumber;
    //呼叫类型
    private String callType;
    //呼叫结果
    private String callResult;
    //查询时间段
    private Period period;

}