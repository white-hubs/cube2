package com.guhecloud.cube.biz.emergency.server.param;

import lombok.Data;

import java.util.Set;

@Data
public class EmergencyFileNoticeModifyParam {

    /**
     * 通知通道
     */
    private Set<String> noticeWays;

    /**
     * 通知人员配置
     */
    private Set<EmergencyFileNoticeObjParam> emergencyFileNoticeObjs;

    @Data
    public static class EmergencyFileNoticeObjParam {

        /**
         * 文件类型主键
         */
        private Long emergencyFileTypeId;

        /**
         * 发送对象类型
         */
        private String objType;

        /**
         * 发送对象主键
         */
        private Set<Long> objId;
    }
}
