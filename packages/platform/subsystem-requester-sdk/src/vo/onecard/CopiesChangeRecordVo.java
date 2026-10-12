package com.guhecloud.subsystem.requester.sdk.vo.onecard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CopiesChangeRecordVo {
    /**
     * 消费的场所名称
     */
    private String depName;
    /**
     * 交易份数
     */
    private String newMoney;
    /**
     * 交易类型 1 消费 2 补贴
     */
    private Integer transactionType;
    /**
     * 交易时间
     */
    private String transactionTime;
    /**
     * 设备编号
     */
    private String devSerial;
    /**
     * 人员序号
     */
    private String userSerial;
    /**
     * 工号
     */
    private String userNo;
    /**
     * 姓名
     */
    private String userName;

}
