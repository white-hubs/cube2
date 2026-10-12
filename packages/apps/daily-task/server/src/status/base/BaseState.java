package com.guhecloud.cube.biz.daily.task.status.base;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/11/14 15:51
 * @Description:
 */
public interface BaseState {
    Long getId();

    String getStatus();

    void setStatus(String status);
}
