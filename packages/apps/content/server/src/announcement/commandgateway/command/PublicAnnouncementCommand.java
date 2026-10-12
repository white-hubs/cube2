package com.guhecloud.cube.srv.gen.content.management.server.announcement.commandgateway.command;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/7/9 11:31
 * @Description:
 */
@Data
public class PublicAnnouncementCommand extends Paging {

    private Long id;

    /**
     * 公告标题
     */
    private String keyword;
    /**
     * 查询数据条数
     */
    private Long num = 3L;

    /**
     * 公告类型
     */
    private String informationType;

    /**
     * 标签类型
     */
    private String tagType;
    /**
     * 是否隐藏
     * HIDE("已隐藏")
     * DISPLAY("已显示")
     */

    private String status;
}
