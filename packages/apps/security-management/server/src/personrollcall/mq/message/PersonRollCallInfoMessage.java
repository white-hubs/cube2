package com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.mq.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 入场记录消息
 *
 * @author lanchun
 * @date 2022/10/14 14:55
 */
@Data
public class PersonRollCallInfoMessage {

    public final static String TOPIC = "PERSON_ROLL_CALL_RECORD";

    /**
     * 人员清点记录信息
     */
    private List<PersonRollCallRecord> personRollCallRecords;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PersonRollCallRecord {

        /**
         * 点名任务记录编号
         */
        private String recordCode;

        /**
         * 姓名
         */
        private String personName;

        /**
         * 编号
         */
        private String indexCode;

        /**
         * 照片
         */
        private String facePhoto;

        /**
         * 抓拍照片
         */
        private String capturePhoto;

        /**
         * 点名设备编号
         */
        private String deviceCode;

        /**
         * 点名设备名称
         */
        private String deviceName;

        /**
         * 任务类型
         */
        private String taskType;

        /**
         * 人脸比对相似度
         */
        private Float similarity;

        /**
         * 状态：0-点到，1-特殊情况
         */
        private Integer status;

        /**
         * 特殊情况说明
         */
        private String remark;

        /**
         * 点到时间
         */
        private LocalDateTime rollcallTime;

        /**
         * 场所编号
         */
        private String areaIndexCode;

        /**
         * 点名对象所在监区编号
         */
        private String departmentIndexCode;

        /**
         * 点名对象所在监区名称
         */
        private String departmentName;

        /**
         * 场所名称（保留字段，当前版本无数据）
         */
        private String areaName;

        private Integer tenant;
    }
}
