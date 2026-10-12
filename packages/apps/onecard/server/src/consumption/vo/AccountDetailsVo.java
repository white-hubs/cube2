package com.guhecloud.cube.onecard.server.consumption.vo;

import com.guhecloud.cube.onecard.server.consumption.param.AmountRecord;
import com.guhecloud.cube.onecard.server.consumption.param.CopiesRecord;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class AccountDetailsVo {

    private String userCard;
    private Long userSerial;
    private String userName;
    private String userNo;
    //账户余额
    private BigDecimal accountBalance;
    //补贴金额
    private BigDecimal subsidyBalance;
    //现金余额
    private BigDecimal cashBalance;
    private List<AmountRecord> amountRecord;
    private List<CopiesRecord> copiesRecord;

}
