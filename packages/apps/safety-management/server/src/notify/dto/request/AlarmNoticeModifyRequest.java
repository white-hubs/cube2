package com.guhecloud.cube.srv.gen.safety.management.notify.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlarmNoticeModifyRequest {

    @Size(max = 50, message = "业务类型不能为空")
    @NotEmpty(message = "业务类型不能为空")
    private String businessType;

    @Valid
    @NotNull(message = "配置信息不能为空")
    @Size(min = 1, message = "配置信息不能小于1条")
    private List<AlarmNotifySceneConfigRequest> sceneConfigDTOS;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AlarmNotifySceneConfigRequest {

        /**
         * 报警级别编号
         */
        @NotEmpty(message = "报警级别不能为空")
        @Size(max = 30, message = "报警级别编号不能大于30")
        private String alarmLevelCode;

        /**
         * 条件值
         */
        private Integer value;

        /**
         * 启用状态
         */
        @NotNull(message = "启用状态不能为空")
        private Boolean enabled;

        /**
         * 消息场景编号
         */
        @NotEmpty(message = "消息场景编号不能为空")
        @Size(max = 30, message = "消息场景编号不能大于30")
        private String sceneCode;

        /**
         * 通知方式
         */
        private List<AlarmNoticeWayRequest> noticeWays;
        /**
         * 通知对象
         */
        private List<AlarmNoticeObjRequest> noticeObjs;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AlarmNoticeWayRequest {

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
    public static class AlarmNoticeObjRequest {
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
