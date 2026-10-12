package com.guhecloud.subsystem.requester.sdk.param.onecard;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

@Data
public class CardParamQuery extends Paging {
    private String userDep;
    private String keyword;
}
