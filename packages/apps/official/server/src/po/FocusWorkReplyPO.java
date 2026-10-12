package com.guhecloud.cube.srv.gen.official.management.server.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.official.management.server.po.model.FlowCommonEntity;
import lombok.Data;

/**
 * @author appet
 * @date 2023/02/24
 * @description 督办事项反馈
 */
@Data
@TableName("focus_work_reply")
public class FocusWorkReplyPO extends FlowCommonEntity {

    private Long focusWorkId;//督办事项ID

    private float percentage;//进度百分比

    private String nextWorkDesc;//下一步工作描述

    private String undoneReason;//未完成原因

    private String assistProblem;//需要协同的问题

}