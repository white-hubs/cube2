package com.guhecloud.cube.deliver.project.merchant.dto.message;

import lombok.Data;

import java.util.List;

@Data
public class ProjectMerchantAssemblyNoticeParam {
    /**
     * 通知条件：XX分钟
     */
    private List<KeyValueVO> condition;
    /**
     * ELECTRON_TASK_RECEIVE("任务接收提醒"),
     * ELECTRON_TASK_EXPIRE("任务到期提醒"),
     * ELECTRON_TASK_ABNORMAL("任务异常");
     */
    private String moduleType;
    private String businessType;
}
