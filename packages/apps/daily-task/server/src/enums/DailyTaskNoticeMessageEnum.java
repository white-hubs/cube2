package com.guhecloud.cube.biz.daily.task.enums;

import com.guhecloud.cube.base.bracing.dto.data.MessageSendConfig;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyTaskPO;
import com.guhecloud.cube.biz.daily.task.task.service.DailyTaskService;
import com.guhecloud.platform.base.context.ApplicationContextHolder;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/11/19 16:17
 * @Description:
 */
public enum DailyTaskNoticeMessageEnum {
    /**
     * 任务生成
     */
    DAILY_TASK_GENERATE {
        @Override
        public List<DailyTaskPO> sendMessage(List<MessageSendConfig> value) {
            return null;
        }
    },
    /**
     * 任务移交
     */
    DAILY_TASK_TRANSFER {
        @Override
        public List<DailyTaskPO> sendMessage(List<MessageSendConfig> value) {
            return null;
        }
    },
    /**
     * 任务超时
     *
     */
    DAILY_TASK_OVERDUE {
        @Override
        public List<DailyTaskPO> sendMessage(List<MessageSendConfig> value) {
            DailyTaskService dailyTaskService = ApplicationContextHolder.getBean(DailyTaskService.class);
            return dailyTaskService.getOverDueTask(value);
        }
    },
    /**
     * 任务即将超时
     */
    DAILY_TASK_WILL_OVERDUE {
        @Override
        public List<DailyTaskPO> sendMessage(List<MessageSendConfig> value) {
            DailyTaskService dailyTaskService = ApplicationContextHolder.getBean(DailyTaskService.class);
            return dailyTaskService.getTaskWillOverOrProgress(value);
        }
    },
    /**
     * 任务终止
     *
     */
    DAILY_TASK_TERMINATE {
        @Override
        public List<DailyTaskPO> sendMessage(List<MessageSendConfig> value) {
            return null;
        }
    },
    /**
     * 任务进度提醒
     *
     */
    DAILY_TASK_PROGRESS {
        @Override
        public List<DailyTaskPO> sendMessage(List<MessageSendConfig> value) {
            DailyTaskService dailyTaskService = ApplicationContextHolder.getBean(DailyTaskService.class);
            return dailyTaskService.getTaskWillOverOrProgress(value);
        }
    },
    /**
     * 任务提交
     *
     */
    DAILY_TASK_SUBMIT {
        @Override
        public List<DailyTaskPO> sendMessage(List<MessageSendConfig> value) {
            return null;
        }
    },
    /**
     * 任务完成
     *
     */
    DAILY_TASK_COMPLETE {
        @Override
        public List<DailyTaskPO> sendMessage(List<MessageSendConfig> value) {
            return null;
        }
    },
    ;

    public static List<String> getJobNoticeSceneCode() {
        return Arrays.asList(DAILY_TASK_OVERDUE.name(), DAILY_TASK_WILL_OVERDUE.name(), DAILY_TASK_PROGRESS.name());
    }

    public abstract List<DailyTaskPO> sendMessage(List<MessageSendConfig> value);
}
