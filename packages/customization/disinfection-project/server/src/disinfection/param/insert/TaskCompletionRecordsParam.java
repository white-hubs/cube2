package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.insert;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class TaskCompletionRecordsParam {

    /**
     * 进度
     */
    private BigDecimal process;
    /**
     * 描述
     */
    private String taskDesc;
    /**
     * 图片文件
     */
    private String appendix;

    private List<PlaceRecordParam> placeRecords;

    @Data
    public static class PlaceRecordParam {
        private Long placeId;
        private String appendix;
    }
}
