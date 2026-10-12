package com.guhecloud.cube.onecard.server.consumption.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

@Data
public class AccountInfoQueryParam extends Paging {
    private String enterParam;
    private String accountName;
}
