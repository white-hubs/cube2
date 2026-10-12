package com.guhecloud.cube.biz.work.center.server.param;

import lombok.Data;

import java.util.List;

/**
 * 交班扩展参数
 */
@Data
public class HandoverRecordExtendParam {

    private String attInfo1;

    private List<Long> file1;

    private String attInfo2;

    private List<Long> file2;

    private String attInfo3;

    private List<Long> file3;

    private String attInfo4;

    private List<Long> file4;
}
