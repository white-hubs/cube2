package com.guhecloud.cube.srv.gen.adapter.information.server.yozhiled.qo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YouZhiLedSendProgramQo {
    /**
     * 开始日期 yyyy-MM-dd
     */
    private String beginDate;
    /**
     * 结束日期 yyyy-MM-dd
     */
    private String endDate;
    /**
     * 开始时间 HH:mm:ss 格式
     */
    private String beginTime;
    /**
     * 结束时间 HH:mm:ss 格式，必须大于 “开始时间”
     */
    private String endTime;
    /**
     * 星期数组
     */
    private List<Integer> weeks;
    /**
     * 终端 Id 数组
     */
    private List<String> terminalIds;
    /**
     * 节目 Id
     */
    private String programId;
    /**
     * 优先级
     */
    private Integer priority;
}
