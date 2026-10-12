package com.guhecloud.cube.base.flow.flowable.common;

public interface ProcessService {

    /*
     *本地变量，必须加前缀存储（为了和流程变量区别开来）
     * */
    String LOCAL_VAR_PREFIX = "_";

    /**
     * 内置的本地变量
     */
    String VAR_TASK_COMMENT = LOCAL_VAR_PREFIX + "task_comment";
    String VAR_TASK_RESULT = LOCAL_VAR_PREFIX + "task_result";

    String TASK_RESULT_PASSED = "task_passed";
    String TASK_RESULT_RETURNED = "task_returned";
    String TASK_RESULT_REJECTED = "task_rejected";
    String TASK_RESULT_REVOKED = "task_revoked";
    String TASK_RESULT_CLOSED = "task_closed";

    String PROCESS_STATUS_INPROGRESS = "process_inprogress";
    String PROCESS_STATUS_PASSED = "process_passed";
    String PROCESS_STATUS_REJECTED = "process_rejected";
    String PROCESS_STATUS_REVOKED = "process_revoked";
    String PROCESS_STATUS_CLOSED = "process_closed";

}
