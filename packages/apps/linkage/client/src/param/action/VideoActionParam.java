package com.guhecloud.cube.biz.linkage.client.param.action;

import com.alibaba.fastjson2.annotation.JSONType;
import com.guhecloud.apps.linkage.metadata.enumeration.ExecuteModeEnum;
import com.guhecloud.apps.linkage.metadata.enumeration.LinkageActionTypeEnum;
import com.guhecloud.apps.linkage.metadata.enumeration.VideoActionTypeEnum;
import com.guhecloud.apps.linkage.metadata.enumeration.VideoStrategyTypeEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@JSONType(typeName = "VIDEO_DEVICE_ACTION")
@NoArgsConstructor
public class VideoActionParam extends ActionParam {

    /**
     * 摄像机动作配置
     */
    @Valid
    private List<VideoActionConfigParam> actionConfig;

    public VideoActionParam(LinkageActionTypeEnum actionType, Boolean enabled, List<VideoActionConfigParam> actionConfig) {
        super(actionType, enabled);
        this.actionConfig = actionConfig;
    }

    @Data
    public static class VideoActionConfigParam {

        /**
         * 类型
         */
        private VideoStrategyTypeEnum linkageVideoStrategyType = VideoStrategyTypeEnum.CUSTOM_VIDEO;

        /**
         * 设备主键
         */
        private Long id;

        /**
         * 设备编号
         */
        private String code;

        /**
         * 设备名称
         */
        private String name;

        /**
         * 摄像机token
         */
        private String token;

        /**
         * 动作类型
         */
        private VideoActionTypeEnum type;

        /**
         * 动作时长
         */
        private Integer time;

        /**
         * 动作时长单位
         */
        private String timeUnit;

        /**
         * 间隔时间
         */
        private Integer intervalTime;

        /**
         * 持续次数
         */
        private Integer durationTime;

        /**
         * 执行方式
         */
        @NotNull(message = "执行方式不能为空")
        private ExecuteModeEnum executeMode;
    }
}
