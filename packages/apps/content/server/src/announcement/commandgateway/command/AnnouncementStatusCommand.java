package com.guhecloud.cube.srv.gen.content.management.server.announcement.commandgateway.command;

import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/7/8 15:12
 * @Description:
 */
@Data
public class AnnouncementStatusCommand {
    /**
     * 公告id
     */
    private Long id;
    /**
     * 状态
     */
    private String status;

    /**
     * 是否置顶
     */
    private Boolean isTopping;
}
