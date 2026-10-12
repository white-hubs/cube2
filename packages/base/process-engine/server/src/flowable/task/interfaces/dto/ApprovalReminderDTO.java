package com.guhecloud.cube.base.flow.flowable.task.interfaces.dto;

import com.guhecloud.cube.base.flow.flowable.common.message.NotifyUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2024-05-20  16:21
 * @Description:
 * @Version: 1.0
 */
@Data
public class ApprovalReminderDTO {
    /**
     * 单位
     */
    private String unit;
    /**
     * 时间
     */
    private Double value;
    /**
     * 通知类型
     */
    private String notifyType;
    /**
     * 通知人员
     */
    private List<NotifyInfo> notifyUsers;
    /**
     * 通知角色
     */
    private List<NotifyInfo> notifyRole;
    /**
     * 通知岗位
     */
    private List<NotifyInfo> notifyPost;
    /**
     * 通知职位
     */
    private List<NotifyInfo> notifyAppointment;

    /**
     * 表单内联系人
     */
    private String formUser;

    @Data
    public static class NotifyInfo {

        private Long id;
        private String name;
    }
}
