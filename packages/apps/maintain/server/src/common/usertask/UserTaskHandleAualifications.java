package com.guhecloud.cube.srv.gen.maintain.server.common.usertask;

/**
 * @author pengjian
 * @date 2022/6/21 19:21
 * @description
 */
public interface UserTaskHandleAualifications<T> {
    /**
     * 是否有资格
     *
     * @param data
     *
     * @return
     */
    boolean isAualified(T data);
}
