package com.guhecloud.cube.srv.gen.content.management.server.technicalnotice.commandgateway.command;

import com.guhecloud.cube.srv.gen.content.management.server.noticeconfig.commandgateway.command.SubmitNoticeConfigCommand;
import lombok.Data;

import java.util.List;

/**
 * @author lzy
 * @date 2022/5/20
 */
@Data
public class SubmitTechnicalNoticeCommand {
    /**
     * 通报标题
     */
    private String title;

    /**
     * 通报说明
     */
    private String illustrate;

    /**
     * 文件主键
     */
    private Long fileId;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 消息通知信息
     */
    private List<SubmitNoticeConfigCommand> noticeConfigCommands;
}
