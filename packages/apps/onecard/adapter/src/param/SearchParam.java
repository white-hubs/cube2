package com.guhecloud.cube.srv.gen.adapter.onecard.server.param;

import lombok.Data;

@Data
public class SearchParam {
    private String beginDate;
    private String endDate;
    private String userSerial;
    private String userDep;
    private String devSerial;
    private Integer type;
}
