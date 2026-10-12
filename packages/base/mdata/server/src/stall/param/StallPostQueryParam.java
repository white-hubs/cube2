package com.guhecloud.cube.biz.mdata.server.stall.param;

import lombok.Data;

import java.util.List;

@Data
public class StallPostQueryParam {
    private List<Long> stallIdList;
}
