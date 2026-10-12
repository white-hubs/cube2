package com.guhecloud.cube.biz.emergency.server.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmergencyFileNoticeVO {

    /**
     * 场景编号
     */
    private String sceneCode;

    /**
     * 通知通道
     */
    private Set<String> noticeWays;

    /**
     * 通知人员配置
     */
    private Set<EmergencyFileNoticeObjVO> emergencyFileNoticeObjs;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EmergencyFileNoticeObjVO {

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
