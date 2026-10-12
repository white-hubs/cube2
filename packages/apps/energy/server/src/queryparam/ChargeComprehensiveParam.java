package com.guhecloud.cube.srv.gen.energymanagement.server.queryparam;

import lombok.Data;

import java.util.List;

@Data
public class ChargeComprehensiveParam {
    //时间
    private String period;
    //年月日
    private String timeFlag;
    //类型
    private String instrumentType;
    //area或者route
    private String filterFlag;
    private List<String> ids;
    //树状id
    private List<TreeIds> treeIds;
}
