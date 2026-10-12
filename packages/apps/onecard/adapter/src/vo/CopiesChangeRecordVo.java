package com.guhecloud.cube.srv.gen.adapter.onecard.server.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty(value = "depName")
    private String depName;
    /**
     * 交易份数
     */
    @JsonProperty(value = "newMoney")
    private String newMoney;
    /**
     * 交易类型 1 消费 2 补贴
     */
    @JsonProperty(value = "lx")
    private Integer transactionType;
    /**
     * 交易时间
     */
    @JsonProperty(value = "jlSj")
    private String transactionTime;
    /**
     * 设备编号
     */
    @JsonProperty(value = "devSerial")
    private String devSerial;
    /**
     * 人员序号
     */
    @JsonProperty(value = "userSerial")
    private String userSerial;
    /**
     * 工号
     */
    @JsonProperty(value = "userNo")
    private String userNo;
    /**
     * 姓名
     */
    @JsonProperty(value = "userLname")
    private String userName;

}
