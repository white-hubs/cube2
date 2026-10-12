package com.guhecloud.cube.base.flow.flowable.common;

/**
 * @author pengjian
 * @date 2022/10/25 17:41
 * @description 任务流程的基础结果
 */
public class BaseResult {
    /**
     * 表示流程未结束 或者 任务被激活
     */
    public static final String ACTIVE = "ACTIVE";

    /**
     * 表示流程已经完成 或者任务完成
     */
    public static final String FINISHED = "FINISHED";

}
