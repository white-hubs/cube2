package com.guhecloud.cube.base.message2.server.base.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import jakarta.validation.constraints.Size;

@Data
public class MsgSceneQueryParam extends Paging {

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字长度不能大于100")
    private String keyword;

    /**
     * 应用编号
     */
    @Size(message = "应用编号不能大于50")
    private String applicationCode;
}
