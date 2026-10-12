package com.guhecloud.cube.srv.gen.content.management.server.noticeconfig.commandgateway.command;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lzy
 * @date 2022/5/23
 */
@Data
public class SubmitNoticeConfigCommand {

    /**
     * 通知方式
     */
    private String way;

    /**
     * 人员
     */
    private List<String> userIds = new ArrayList<>();

    /**
     * 指定部门
     */
    private List<String> deptIds = new ArrayList<>();

    /**
     * 指定岗位
     */
    private List<String> postIds = new ArrayList<>();

    /**
     * 指定角色
     */
    private List<String> roleIds = new ArrayList<>();

}
