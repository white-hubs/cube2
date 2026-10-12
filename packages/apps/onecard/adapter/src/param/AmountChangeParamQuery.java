package com.guhecloud.cube.srv.gen.adapter.onecard.server.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

@Data
public class AmountChangeParamQuery extends Paging {
    //开始
    private String beginDate;
    //结束
    private String endDate;
    //人员序号
    private String userSerial;
    //部门序号
    private String userDep;
    private String devSerial;
    //类型
    private String type;
    //工号
    private String userNo;
}
