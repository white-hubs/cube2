package com.guhecloud.cube.srv.gen.energymanagement.server.queryparam;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

/**
 * @author lrj
 * @Description 抄表周期
 * @date 2022-07-04 15:12:36
 */
@Data
public class MeterReadeCycleQueryParam extends Paging {

    //执行时间
    private String workTime;
    //周期时间
    private String readeVulue;
    //抄表周期
    private String readeType;
    //项目表示(outside外部，内部interior)
    private String tenantFlag;

}