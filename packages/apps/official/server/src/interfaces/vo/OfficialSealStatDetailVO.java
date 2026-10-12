package com.guhecloud.cube.srv.gen.official.management.server.interfaces.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OfficialSealStatDetailVO {
    private String sealName;
    private String applicantName;
    private LocalDateTime applyTime;
    private LocalDateTime applyDoneTime;
}