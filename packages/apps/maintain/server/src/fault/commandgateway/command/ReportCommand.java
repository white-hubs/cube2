package com.guhecloud.cube.srv.gen.maintain.server.fault.commandgateway.command;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReportCommand {
    //类型
    private String type;
    //时间
    private LocalDateTime reportTime;
}
