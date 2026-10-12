package com.guhecloud.cube.biz.daily.task.status.base;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/11/18 20:07
 * @Description: 日常操作基础事件
 */
public class BaseEvent {
    //任务相关
    /**
     * 下发
     */
    public static final String ISSUE_TASK = "issueTask";

    /**
     * 存草稿
     */
    public static final String DRAFT_TASK = "draftTask";

    /**
     * 处理
     */
    public static final String TASK_HANDLE = "handle";

    /**
     * 分发
     */
    public static final String DISTRIBUTE = "distribute";

    /**
     * 移交
     */
    public static final String TRANSFER = "transfer";

    /**
     * 创建
     */
    public static final String CREATE_TARGET = "createTarget";

    /**
     * 处理
     */
    public static final String HANDLE_TARGET = "handleTarget";

    /**
     * 终止
     */
    public static final String TASK_TERMINATE = "taskTerminate";

    /**
     * 完成
     */
    public static final String TASK_COMPLETE = "taskComplete";

    // 业务表
    // tableMapper
    public static final String DAILY_TASK_TABLE_MAPPER = "dailyTaskMapper";
    public static final String DAILY_TASK_TARGET_TABLE_MAPPER = "dailyTaskTargetMapper";

    public static final Map<String, String> EVENT_TABLE_MAP = new HashMap<>();

    static {
        EVENT_TABLE_MAP.put(ISSUE_TASK, DAILY_TASK_TABLE_MAPPER);
        EVENT_TABLE_MAP.put(DRAFT_TASK, DAILY_TASK_TABLE_MAPPER);
        EVENT_TABLE_MAP.put(TASK_HANDLE, DAILY_TASK_TABLE_MAPPER);
        EVENT_TABLE_MAP.put(DISTRIBUTE, DAILY_TASK_TABLE_MAPPER);
        EVENT_TABLE_MAP.put(TRANSFER, DAILY_TASK_TABLE_MAPPER);
        EVENT_TABLE_MAP.put(TASK_COMPLETE, DAILY_TASK_TABLE_MAPPER);
        EVENT_TABLE_MAP.put(TASK_TERMINATE, DAILY_TASK_TABLE_MAPPER);
        EVENT_TABLE_MAP.put(CREATE_TARGET, DAILY_TASK_TARGET_TABLE_MAPPER);
        EVENT_TABLE_MAP.put(HANDLE_TARGET, DAILY_TASK_TARGET_TABLE_MAPPER);
    }
}
