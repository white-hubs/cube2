package com.guhecloud.cube.biz.dispatch.server.model;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * @author Appet
 * @Description 调度呼叫记录分页查询
 * @date 2022-07-26 10:08:36
 */
@Data
@SuperBuilder
public class DispatchCallQueryModel extends Paging {

    //设备编号（号码）
    private String code;
    //主叫号码
    private String callerNumber;
    //被叫号码
    private String calleeNumber;
    //呼叫类型
    private String callType;
    //呼叫结果
    private String callResult;
    //开始时间
    private String startTime;
    //结束时间
    private String endTime;
    //        //当前页
    //    @Builder.Default
    //    private int current = 1;
    //        //页大小
    //    @Builder.Default
    //    private int size = 10;

}