package com.guhecloud.cube.base.flow.flowable.task.interfaces.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.guhecloud.cube.base.flow.context.enumeration.RejectStrategyEnum;
import com.guhecloud.cube.base.flow.flowable.resolver.dto.FormOperates;
import com.guhecloud.cube.base.flow.flowable.resolver.dto.OperateAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * @author pengjian
 * @date 2022/10/8 17:43
 * @description
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskInstanceDetailDTO {
    private String id;
    private String flowKey;
    private String businessType;
    private String key;
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime claimTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    private String data;

    private Properties properties;

    /**
     * 流程相关信息
     */
    private ProcessInstanceRunningInfo processInstanceRunningInfo;
    /*
     * 任务剩余处理人
     */
    private Set<Long> remainderOfActive;

    /*
     * 任务已处理人
     */
    private Set<Long> handledOfActive;
    /**
     * 是否允许撤回上一级
     */
    private Boolean withdraw;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class ProcessInstanceRunningInfo {
        private String processInstanceId;
        private Long startUserId;
        private String startName;
        private String status;
        private boolean outSider;
        private List<OperateAlias> operateAlias;
        private List<FormOperates> formPerms;
        private RejectStrategyEnum rejectStrategyEnum;
    }
}
