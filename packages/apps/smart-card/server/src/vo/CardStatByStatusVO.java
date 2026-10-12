package com.guhecloud.cube.biz.smartcard.server.vo;

import lombok.Data;

@Data
public class CardStatByStatusVO {
    //正常数量
    private Integer normalNum;
    //挂失数量
    private Integer lossNum;
}
