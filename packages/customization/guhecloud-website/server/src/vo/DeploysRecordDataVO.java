package com.guhecloud.cube.customized.guhecloudwebsite.vo;

import lombok.Data;

@Data
public class DeploysRecordDataVO {
    private Boolean success;
    /**
     * 返回数据对象
     */
    private OutputDataInfo data;

    @Data
    public class OutputDataInfo {
        private String id;

        private String status;
        private String error;
        private String tenantCode;
        private String createdAt;
        private String updatedAt;
        private String taskId;
        private String provider;
        private String progress;
        private String output;
    }
}
