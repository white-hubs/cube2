package com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FaultReportVo {
    private Long id;
    //类型
    private String type;
    //类型名称
    private String typeName;
    //名称
    private String name;
    //统计时间
    private String startTime;
    //统计结束时间
    private String endTime;
    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
}
