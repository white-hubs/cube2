package com.guhecloud.cube.biz.meattrade.server.base.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.Size;

/**
 * 抽检员信息查询
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class InspectorQueryParam extends Paging {

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字不能大于100")
    private String keyword;

    private Boolean isTrusted;
}
