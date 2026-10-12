package com.guhecloud.cube.base.message2.server.sat.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import jakarta.validation.constraints.Size;

@Data
public class SatMsgSceneReceiveQueryParam extends Paging {

    /**
     * 应用编号
     */
    @Size(max = 50, message = "应用编号不能大于100")
    private String applicationCode;

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字不能大于100")
    private String keyword;
}
