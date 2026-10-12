package com.guhecloud.cube.srv.gen.maintain.server.inspection.enumeration;

import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectTaskStatusEnum;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.Getter;
import lombok.NonNull;

/**
 * 任务通知模板枚举
 */
@Getter
public enum TaskNoticeTempleEnum {

    /**
     * 任务生成
     */
    TASK_GENERATE("您收到【${taskName}】巡检任务，任务将在【${content}】失效，请及时接收任务进行巡检"),
    /**
     * 任务失效
     */
    TASK_LAPSE("您负责的【${taskName}】巡检任务，任务将在【${content}】后失效，请及时接收任务进行巡检"),

    /**
     * 任务失效未完成
     */
    TASK_LAPSE_UNFINISHED("由【${content}】负责的【${taskName}】没有完成且当前已经失效，请知悉");

    /**
     * 描述信息
     */
    private final String description;

    TaskNoticeTempleEnum(@NonNull String description) {
        this.description = description;
    }

    public static String getDescription(String name) {
        if (ObjectUtils.isEmpty(name)) {
            return "";
        }
        return ObjectUtils.isEmpty(InspectTaskStatusEnum.valueOf(name)) ? "" :
               InspectTaskStatusEnum.valueOf(name).getDescription();
    }
}
