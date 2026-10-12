package com.guhecloud.cube.srv.gen.maintain.server.common.usertask;

import lombok.Data;

/**
 * @author pengjian
 * @date 2022/6/6 16:45
 * @description
 */
@Data
public abstract class UserTaskHandleAualificationBO {
    private Boolean isCanConfirm = false;
    private Boolean isCanHandle = false;

}
