package com.guhecloud.cube.srv.gen.content.management.server.noticeconfig.interfaces.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author lzy
 * @date 2022/5/23
 */
@Data
public class NoticeConfigVO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 业务主键
     */
    private Long businessKey;

    /**
     * 通知方式
     */
    private String way;

    /**
     * 设备文档
     */
    private List<InfoDTO> userIds;

    /**
     * 指定部门
     */
    private List<InfoDTO> deptIds;

    /**
     * 指定岗位
     */
    private List<InfoDTO> postIds;

    /**
     * 指定角色
     */
    private List<InfoDTO> roleIds;

    @Data
    @Builder
    public static class InfoDTO {
        private Long id;
        private String name;
    }
}
