package com.guhecloud.cube.biz.daily.task.configuration.param;

import lombok.Data;

import java.util.List;

@Data
public class DailyCronBatchDeleteParam {
    private List<Long> ids;

}
