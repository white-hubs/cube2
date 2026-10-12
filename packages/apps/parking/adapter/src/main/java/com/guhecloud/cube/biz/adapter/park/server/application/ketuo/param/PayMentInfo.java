package com.guhecloud.cube.biz.adapter.park.server.application.ketuo.param;

import lombok.Data;

@Data
public class PayMentInfo {
    private int totalMoney;
    private int paidMoney;
    private int freeTime;
    private int freeMoney;
    private int isPostPay;
}
