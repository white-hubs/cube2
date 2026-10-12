package com.guhecloud.cube.srv.gen.official.management.server.dao.dto;

import lombok.Data;

@Data
public class OfficialSealStatDTO {
    //印章ID
    private Long sealId;
    //印章名称
    private String sealName;
    //申请人数
    private int applicantNum;
    //申请次数
    private int applyNum;
}
