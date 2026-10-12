package com.guhecloud.cube.srv.gen.energymanagement.server.param;

import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.TreeIds;
import lombok.Data;

import java.util.List;

@Data
public class DosageAnalysisParam {
    private String instrumentType;
    //筛选标识
    private String filterFlag;

    private String period;

    private String timeFlag;

    private List<String> ids;

    private List<TreeIds> treeIds;
}
