package com.guhecloud.cube.biz.daily.task.status.base;

import com.guhecloud.cube.biz.daily.task.enums.DailyTaskBusinessTypeEnum;
import com.guhecloud.cube.biz.ouaa.client.ApplicationClient;
import com.guhecloud.cube.biz.ouaa.client.dto.ApplicationDTO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/11/11 13:56
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransitionContext {
    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 业务类型
     */
    private String businessTypeName;

    /**
     * 顶层任务ID
     */
    private Long topTaskId;

    /**
     * 任务主键
     */
    private List<Long> taskIds;

    /**
     * 业务实体
     */
    private List<BaseState> entity;

    /**
     * 业务操作
     */
    private String event;

    /**
     * 当前状态
     */
    private Map<Long, String> beforeState;

    private BaseState currentEntity;

    /**
     * 目标状态
     */
    private StateNode targetState;
    /**
     * 初始状态
     */
    private StateNode initialState;
    /**
     * 扩展参数
     */
    private Map<String, Object> params;

    public static TransitionContext create(String businessType, List<Long> taskIds,
                                           String event
                                          ) {
        ApplicationClient applicationClient = ApplicationContextHolder.getBean(ApplicationClient.class);
        ApplicationDTO application = applicationClient.getApplicationByCode(businessType);
        return TransitionContext.builder()
                .businessType(businessType)
                .businessTypeName(Objects.nonNull(application) ? application.getName()
                                                               : DailyTaskBusinessTypeEnum.valueOf(businessType).getApplicationName())
                .taskIds(taskIds)
                .event(event)
                .params(new HashMap<>())
                .build();
    }

}
