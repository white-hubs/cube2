package com.guhecloud.cube.srv.gen.official.management.server.dao.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OfficialSealStatDetailDTO {
    //印章名称
    private String sealName;
    //申请人
    private String applicantName;
    //申请时间
    private LocalDateTime applyTime;
    //处理时间
    private LocalDateTime applyDoneTime;
    //处理结果
    private String status;
}
