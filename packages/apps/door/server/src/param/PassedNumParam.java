package com.guhecloud.cube.biz.door.server.param;

import com.guhecloud.platform.base.model.Period;
import lombok.Data;

@Data
public class PassedNumParam {

    private String keyWord;
    private Period period;
    /**
     * 通行事件结果
     */
    private String eventResult;
}
