package com.guhecloud.cube.srv.gen.safety.management.notify.dto.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author lanchun
 * @date 2023/9/28 14:15
 */
@Data
public class AlarmNoticeVO {

    /**
     * 报警级别编号
     */
    private String alarmLevelCode;

    /**
     * 条件值
     */
    private Integer value;

    /**
     * 消息场景编号
     */
    private String sceneCode;

    /**
     * 启用状态
     */
    private Boolean enabled;

    /**
     * 通知方式
     */
    private List<AlarmNoticeWayVO> noticeWays;
    /**
     * 通知对象
     */
    private List<AlarmNoticeObjVO> noticeObjs;

    /**
     * 租户编号
     */
    private Integer tenant;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AlarmNoticeWayVO {

        /**
         * 通知方式
         */
        private String noticeWay;

        /**
         * 模板id
         */
        private Long templateId;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AlarmNoticeObjVO {

        /**
         * 通知对象类型
         */
        private String objType;
        /**
         * 通知对象id
         */
        private String objId;

        /**
         * 通知对象json信息
         */
        private String objInfo;

    }

}
