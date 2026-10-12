package com.guhecloud.cube.srv.gen.adapter.onecard.server.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

@Data
public class CardParamQuery extends Paging {
    private String userDep;
    private String keyword;
}
