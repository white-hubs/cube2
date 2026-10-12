package com.guhecloud.cube.onecard.server.consumption.enumeration;

import com.guhecloud.cube.onecard.server.consumption.vo.TransactionTypeVo;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum TransactionTypeEnum {

    //value奇数为金额交易类型，偶数为份交易类型
    CONSUMPTION(1, "消费"),
    RECHARGE(3, "充值金额"),
    SUBSIDY(5, "补贴"),
    SUBSIDYCLEAR(7, "补贴清零"),
    AMOUNTCORRECTION(9, "消费金额纠错"),
    REFUND(11, "退款"),
    SHARECONSUMPTION(2, "份消费"),
    SHARESUBSIDY(4, "份额补贴"),
    SHARECLEAR(6, "份清零"),
    OTHER(99, "其他");

    private Integer value;
    private String name;

    TransactionTypeEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public static List<TransactionTypeVo> list() {
        List<TransactionTypeVo> arrayList = new ArrayList<>();
        for (TransactionTypeEnum transactionTypeEnum : TransactionTypeEnum.values()) {
            TransactionTypeVo transactionTypeVo = new TransactionTypeVo();
            transactionTypeVo.setValue(transactionTypeEnum.getValue());
            transactionTypeVo.setName(transactionTypeEnum.getName());
            arrayList.add(transactionTypeVo);
        }
        return arrayList;
    }

    public static String getName(Integer value) {
        TransactionTypeEnum[] transactionTypeEnums = values();
        for (TransactionTypeEnum transactionTypeEnum : transactionTypeEnums) {
            if (transactionTypeEnum.getValue().equals(value)) {
                return transactionTypeEnum.getName();
            }
        }
        return null;
    }

    public static Integer AmountJobtransfer(Integer value) {
        switch (value) {
            default:
                return 99;
            case 0:
                return 1;
            case 1:
                return 3;
            case 2:
                return 5;
        }

    }

    public static Integer CopiesJobtransfer(Integer value) {
        switch (value) {
            default:
                return 99;
            case 1:
                return 2;
            case 2:
                return 4;
        }

    }
}
