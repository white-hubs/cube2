package com.guhecloud.cube.srv.gen.adapter.access.control.server.model.das.api.vo;

import lombok.Data;

import java.util.List;

/**
 * 数据列表
 */
@Data
public class DasDoorEventInfoResponse {
    /**
     * 错误码：0-为无错误，非0-为具体错误码
     */
    private Integer code;
    /**
     * 错误消息
     */
    private String msg;
    /**
     * 返回数据对象
     */
    private DasDoorEventPage data;

    @Data
    public static class DasDoorEventPage {
        private Integer totalCount;
        private Integer pageCount;
        private List<DasDoorEventInfo> dataList;
    }

    @Data
    public static class DasDoorEventInfo {
        /**
         * 部门编号
         */
        private String dptNo;
        /**
         * 人员编号
         */
        private String empNo;
        /**
         * 人员姓名
         */
        private String empName;
        /**
         * 部门名称
         */
        private String dptName;
        /**
         * 设备/门名称
         */
        private String devName;
        /**
         * 设备/门ID
         */
        private String devId;
        /**
         * 通行时间 2020-05-18 12:11:33.000
         */
        private String recordTime;
        /**
         * 事件类型 刷脸/刷卡
         */
        private String eventType;
        /**
         * 进出标记 0不分进出 1进 2出
         */
        private String inOutFlag;
    }
}
