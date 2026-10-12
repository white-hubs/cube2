package com.guhecloud.cube.customized.guhecloudwebsite.vo;

import lombok.Data;

import java.util.List;

@Data
public class BuildRecordDataVO {
    private Boolean success;
    /**
     * 返回数据对象
     */
    private OutputDataInfo data;

    @Data
    public class OutputDataInfo {
        private String id;
        private String type;
        private String status;
        private String outputPath;
        private String error;
        private String tenantCode;
        private String createdAt;
        private String updatedAt;
    }
}
