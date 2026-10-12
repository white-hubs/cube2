package com.guhecloud.cube.srv.gen.energymanagement.server.queryparam;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

@Data
public class EnergyAnalysisQueryParam extends Paging {
    private String bindType;
    private String itemId;
    private String period;
    private String timeFlag;
}
