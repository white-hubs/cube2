package com.guhecloud.cube.srv.gen.content.management.server.announcement.commandgateway.command;

import com.guhecloud.cube.srv.gen.content.management.server.noticeconfig.commandgateway.command.SubmitNoticeConfigCommand;
import lombok.Data;

import java.util.Collection;
import java.util.List;

/**
 * @author lzy
 * @date 2022/5/20
 */
@Data
public class SubmitAnnouncementCommand {

    /**
     * 通知标题
     */
    private String title;

    /**
     * 通知类别
     */
    private String type;

    /**
     * 通知级别
     */
    private String level;

    /**
     * 通知内容
     */
    private String content;

    /**
     * 消息通知信息
     */
    private List<SubmitNoticeConfigCommand> noticeConfigCommands;

    /**
     * 文件主键集合
     */
    private Collection<Long> fileIds;

    /**
     * 发布机构
     */
    private Long publishAgency;
    /**
     * 公告标签
     */
    private Long tagId;
}
