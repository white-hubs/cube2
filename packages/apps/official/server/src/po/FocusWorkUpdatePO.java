package com.guhecloud.cube.srv.gen.official.management.server.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.official.management.server.po.model.FlowCommonEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author appet
 * @date 2023/02/24
 * @description 督办事项反馈
 */
@Data
@TableName("focus_work_update")
public class FocusWorkUpdatePO extends FlowCommonEntity {

    private Long focusWorkId;//督办事项ID

    private LocalDateTime updateOverTime;//更新完成时间

    private String updateOverTimeStr;//保持表单格式的时间

    private String status;//状态

    private String updateReason;//更新理由

    private String newTarget;//调整后目标

    private String processInstanceId;//流程实例ID

}