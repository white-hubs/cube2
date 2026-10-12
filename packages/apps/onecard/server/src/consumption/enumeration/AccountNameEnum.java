package com.guhecloud.cube.onecard.server.consumption.enumeration;

import com.guhecloud.cube.onecard.server.consumption.vo.AccountNameVo;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum AccountNameEnum {

    ORDINARYSTAFF(1, "普通员工"),
    MIDDLEMANAGEMENT(2, "中层管理"),
    HIGHMANAGEMENT(3, "高层管理"),
    OFFICERECIVE(4, "办公招待"),
    SPECIALWORK(5, "特殊工种"),
    TEMPORARY(6, "临时工"),
    OUTSOURCING(7, "外协人员");

    private Integer value;
    private String description;

    AccountNameEnum(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    public Integer getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static List<AccountNameVo> list() {
        List<AccountNameVo> arrayList = new ArrayList<>();
        for (AccountNameEnum accountNameEnum : AccountNameEnum.values()) {
            AccountNameVo accountNameVo = new AccountNameVo();
            accountNameVo.setValue(accountNameEnum.getValue());
            accountNameVo.setDescription(accountNameEnum.getDescription());
            arrayList.add(accountNameVo);
        }
        return arrayList;
    }
}
