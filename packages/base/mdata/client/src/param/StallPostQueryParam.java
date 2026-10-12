package com.guhecloud.cube.biz.mdata.client.param;

import lombok.Data;

import java.util.List;

@Data
public class StallPostQueryParam {
    private List<Long> stallIdList;
}
