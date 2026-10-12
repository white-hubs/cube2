package com.guhecloud.cube.srv.gen.attendance.management.server.repairclock.interfaces.vo;

import lombok.Data;

/**
 * @author appet
 * @date 2022/12/13
 * @description 补卡次数统计
 */
@Data
public class RepairClockNumVO {
    //异常类型
    private String exp;
    //该类异常的补卡次数
    private int num;

}